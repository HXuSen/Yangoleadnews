package com.yango.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.apis.article.IArticleClient;
import com.yango.apis.user.IUserClient;
import com.yango.common.constants.HotArticleConstants;
import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.comment.dto.CommentConfigDto;
import com.yango.model.comment.dto.CommentLikeDto;
import com.yango.model.comment.dto.CommentManageDto;
import com.yango.model.comment.dto.CommentRepaySaveDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.message.UpdateArticleMess;
import com.yango.model.user.pojos.ApUser;
import com.yango.model.wemedia.pojos.WmUser;
import com.yango.utils.thread.WmThreadLocalUtil;
import com.yango.wemedia.mapper.WmUserMapper;
import com.yango.wemedia.pojos.*;
import com.yango.wemedia.service.CommentManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: CommentManageServiceImpl
 * Package: com.yango.wemedia.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-18:44
 */
@Service
@Slf4j
public class CommentManageServiceImpl implements CommentManageService {
    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult findNewsComments(ArticleCommentDto dto) {
        WmUser user = WmThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.setWmUserId(user.getId());
        return articleClient.findNewsComments(dto);
    }

    @Override
    public ResponseResult updateCommentStatus(CommentConfigDto dto) {
        WmUser user = WmThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        WmUser wmUser = wmUserMapper.selectById(user.getId());
        Integer apUserId = wmUser.getApUserId();

        List<ApComment> apComments = mongoTemplate.find(Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("authorId").is(apUserId)), ApComment.class);
        for (ApComment apComment : apComments) {
            List<ApCommentRepay> apCommentRepays = mongoTemplate.find(Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(apUserId)), ApCommentRepay.class);
            List<String> commentRepayIds = apCommentRepays.stream().map(ApCommentRepay::getId).distinct().collect(Collectors.toList());
            //删除所有的评论回复点赞数据
            mongoTemplate.remove(Query.query(Criteria.where("commentRepayId").in(commentRepayIds).and("authorId").is(apUserId)), ApCommentRepayLike.class);
            //删除该评论的所有的回复内容
            mongoTemplate.remove(Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(apUserId)), ApCommentRepay.class);
            //删除评论的点赞
            mongoTemplate.remove(Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(apUserId)), ApCommentLike.class);
        }
        //删除评论
        mongoTemplate.remove(Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("authorId").is(apUserId)),ApComment.class);
        //更新文章的评论数,kafkaStream
        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMess.UpdateArticleType.COMMENT);
        mess.setAdd(-1);
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC, JSON.toJSONString(mess));

        return articleClient.updateCommentStatus(dto);
    }

    @Autowired
    private IUserClient userClient;
    @Override
    public ResponseResult saveCommentRepay(CommentRepaySaveDto dto) {
        if (dto == null || dto.getCommentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(dto.getContent().length() > 140){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"评论内容不能超过140字");
        }
        //安全检查

        WmUser user = WmThreadLocalUtil.getUser();
        WmUser wmUser = wmUserMapper.selectById(user.getId());
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        ApUser apUser = userClient.findUserById(wmUser.getId());
        //save
        ApCommentRepay apCommentRepay = new ApCommentRepay();
        apCommentRepay.setCommentId(dto.getCommentId());
        apCommentRepay.setAuthorName(apUser.getName());
        apCommentRepay.setContent(dto.getContent());
        apCommentRepay.setAuthorId(apUser.getId());
        apCommentRepay.setCreatedTime(new Date());
        apCommentRepay.setUpdatedTime(new Date());
        apCommentRepay.setLikes(0);
        mongoTemplate.save(apCommentRepay);

        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        apComment.setReply(apComment.getReply() + 1);
        mongoTemplate.save(apComment);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult like(CommentLikeDto dto) {
        if (dto == null || dto.getCommentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        WmUser user = WmThreadLocalUtil.getUser();
        WmUser wmUser = wmUserMapper.selectById(user.getId());
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        ApUser apUser = userClient.findUserById(wmUser.getApUserId());

        if (apComment != null && dto.getOperation() == 0) {
            apComment.setLikes(apComment.getLikes() + 1);
            mongoTemplate.save(apComment);

            ApCommentLike apCommentLike = new ApCommentLike();
            apCommentLike.setCommentId(apCommentLike.getCommentId());
            apCommentLike.setAuthorId(apUser.getId());
            mongoTemplate.save(apCommentLike);
        }else{
            int likes = apComment.getLikes() - 1;
            likes = likes < 1 ? 0 : likes;
            apComment.setLikes(likes);
            mongoTemplate.save(apComment);

            Query query = Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(apUser.getId()));
            mongoTemplate.find(query, ApCommentLike.class);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("likes",apComment.getLikes());
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult delComment(String commentId) {
        if (StringUtils.isBlank(commentId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApComment apComment = mongoTemplate.findById(commentId, ApComment.class);
        mongoTemplate.remove(Query.query(Criteria.where("id").is(commentId)),ApComment.class);
        mongoTemplate.remove(Query.query(Criteria.where("commentId").is(commentId)),ApCommentRepay.class);
        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(apComment.getEntryId());
        mess.setType(UpdateArticleMess.UpdateArticleType.COMMENT);
        mess.setAdd(-1);
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult delCommentRepay(String commentRepayId) {
        if (StringUtils.isBlank(commentRepayId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        mongoTemplate.remove(Query.query(Criteria.where("id").is(commentRepayId)),ApCommentRepay.class);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult list(CommentManageDto dto) {
        if (dto == null || dto.getArticleId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        log.info("dto:{}",dto);
        List<CommentRepayListVo> commentRepayListVos = new ArrayList<>();
        log.info("IdToString:{}",dto.getArticleId().toString());
        Query query = Query.query(Criteria.where("entryId").is(dto.getArticleId().toString()));
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        query.with(pageable);
        query.with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApComment> apComments = mongoTemplate.find(query, ApComment.class);

        for (ApComment apComment : apComments) {
            CommentRepayListVo vo = new CommentRepayListVo();
            vo.setApComments(apComment);
            Query query1 = Query.query(Criteria.where("commentId").is(apComment.getId()));
            query1.with(Sort.by(Sort.Direction.DESC,"createdTime"));
            List<ApCommentRepay> apCommentRepays = mongoTemplate.find(query, ApCommentRepay.class);
            vo.setApCommentRepays(apCommentRepays);
            commentRepayListVos.add(vo);
        }

        return ResponseResult.okResult(commentRepayListVos);
    }


}
