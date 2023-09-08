package com.yango.comment.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yango.apis.article.IArticleClient;
import com.yango.apis.user.IUserClient;
import com.yango.apis.wemedia.IWemediaClient;
import com.yango.comment.pojo.ApComment;
import com.yango.comment.pojo.ApCommentLike;
import com.yango.comment.pojo.CommentVo;
import com.yango.comment.service.CommentService;
import com.yango.common.constants.HotArticleConstants;
import com.yango.model.article.pojos.ApArticleConfig;
import com.yango.model.comment.dto.CommentDto;
import com.yango.model.comment.dto.CommentLikeDto;
import com.yango.model.comment.dto.CommentSaveDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.message.UpdateArticleMess;
import com.yango.model.user.pojos.ApUser;
import com.yango.utils.common.SensitiveWordUtil;
import com.yango.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: CommentServiceImpl
 * Package: com.yango.comment.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-20:36
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private IUserClient userClient;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Override
    public ResponseResult saveComment(CommentSaveDto dto) {
        //检查参数
        if (dto == null || dto.getArticleId() == null || StringUtils.isBlank(dto.getContent())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检查文章评论权限
        if(!checkParam(dto.getArticleId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"该文章评论权限已关闭");
        }
        //检查文章内容
        if (dto.getContent().length() > 140) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"评论内容不能超过140字");
        }
        if(handleSensitiveScan(dto.getContent())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"评论内容存在非法词语");
        }
        //检查用户是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //保存评论
        ApUser dbUser = userClient.findUserById(user.getId());
        if (dbUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "当前登录信息有误");
        }
        ApComment comment = new ApComment();
        comment.setAuthorId(dbUser.getId());
        comment.setContent(dto.getContent());
        comment.setCreatedTime(new Date());
        comment.setEntryId(dto.getArticleId());
        comment.setImage(dbUser.getImage());
        comment.setAuthorName(dbUser.getName());
        comment.setLikes(0);
        comment.setReply(0);
        comment.setType((short) 0);
        comment.setFlag((short) 0);
        mongoTemplate.save(comment);
        //更新文章的评论数,kafkaStream
        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMess.UpdateArticleType.COMMENT);
        mess.setAdd(1);
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult like(CommentLikeDto dto) {
        if (dto == null || dto.getCommentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);

        if (apComment != null && dto.getOperation() == 0) {
            apComment.setLikes(apComment.getLikes() + 1);
            mongoTemplate.save(apComment);

            ApCommentLike apCommentLike = new ApCommentLike();
            apCommentLike.setCommentId(apComment.getId());
            apCommentLike.setAuthorId(user.getId());
            mongoTemplate.save(apCommentLike);
        }else {
            int like = apComment.getLikes() - 1;
            like = like < 1 ? 0 : like;
            apComment.setLikes(like);
            mongoTemplate.save(apComment);

            Query query = Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(user.getId()));
            mongoTemplate.remove(query, ApCommentLike.class);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("likes",apComment.getLikes());
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult loadCommentsByArticleId(CommentDto dto) {
        if (dto == null || dto.getArticleId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        int size = 10;
        Query query = Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("createdTime").lt(dto.getMinDate()));
        query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
        List<ApComment> apComments = mongoTemplate.find(query, ApComment.class);

        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.okResult(apComments);
        }

        List<String> commentIds = apComments.stream()
                .map(comment -> comment.getId())
                .collect(Collectors.toList());
        Query query1 = Query.query(Criteria.where("commentId").in(commentIds).and("authorId").is(user.getId()));
        List<ApCommentLike> apCommentLikes = mongoTemplate.find(query1, ApCommentLike.class);
        if (apCommentLikes.size() == 0){
            return ResponseResult.okResult(apComments);
        }
        List<CommentVo> commentVos = new ArrayList<>();
        apComments.forEach(comment -> {
            CommentVo vo = new CommentVo();
            BeanUtils.copyProperties(comment,vo);
            for (ApCommentLike like : apCommentLikes) {
                if (comment.getId().equals(like.getCommentId())){
                    vo.setOperation((short) 0);
                    break;
                }
            }
            commentVos.add(vo);
        });

        return ResponseResult.okResult(commentVos);
    }

    @Autowired
    private IWemediaClient wemediaClient;
    private boolean handleSensitiveScan(String content) {
        ResponseResult sensitiveResult = wemediaClient.getAllSensitive();
        if (!sensitiveResult.getCode().equals(200) || sensitiveResult.getData() == null) {
            return true;
        }
        List<String> sensitives = JSON.parseArray(JSON.toJSONString(sensitiveResult.getData()), String.class);
        if (sensitives == null){
            return true;
        }
        SensitiveWordUtil.initMap(sensitives);
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);

        return map.size() > 0;
    }

    @Autowired
    private IArticleClient articleClient;
    private boolean checkParam(Long articleId) {
        ResponseResult configResult = articleClient.findArticleConfigByArticleId(articleId);
        if (!configResult.getCode().equals(200) || configResult.getData() == null){
            return false;
        }
        ApArticleConfig articleConfig = JSON.parseObject(JSON.toJSONString(configResult.getData()), ApArticleConfig.class);
        if (articleConfig == null || !articleConfig.getIsComment()) {
            return false;
        }
        return true;
    }
}
