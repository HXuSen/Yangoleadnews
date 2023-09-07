package com.yango.comment.service.impl;

import com.alibaba.fastjson.JSON;
import com.yango.apis.user.IUserClient;
import com.yango.apis.wemedia.IWemediaClient;
import com.yango.comment.pojo.ApComment;
import com.yango.comment.pojo.ApCommentRepay;
import com.yango.comment.pojo.ApCommentRepayLike;
import com.yango.comment.pojo.CommentRepayVo;
import com.yango.comment.service.CommentRepayService;
import com.yango.model.comment.dto.CommentRepayDto;
import com.yango.model.comment.dto.CommentRepayLikeDto;
import com.yango.model.comment.dto.CommentRepaySaveDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: CommentRepayServiceImpl
 * Package: com.yango.comment.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-16:22
 */
@Service
@Slf4j
public class CommentRepayServiceImpl implements CommentRepayService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IUserClient userClient;
    @Override
    public ResponseResult saveCommentRepay(CommentRepaySaveDto dto) {
        if (dto == null || dto.getCommentId() == null || StringUtils.isBlank(dto.getContent())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (dto.getContent().length() > 140){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"评论内容不能超过140字");
        }
        if(handleSensitiveScan(dto.getContent())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"评论内容存在非法词语");
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        ApUser dbUser = userClient.findUserById(user.getId());
        if (dbUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "当前登录信息有误");
        }
        ApCommentRepay apCommentRepay = new ApCommentRepay();
        apCommentRepay.setCommentId(dto.getCommentId());
        apCommentRepay.setContent(dto.getContent());
        apCommentRepay.setLikes(0);
        apCommentRepay.setCreatedTime(new Date());
        apCommentRepay.setAuthorId(dbUser.getId());
        apCommentRepay.setAuthorName(dbUser.getName());
        apCommentRepay.setUpdatedTime(new Date());
        mongoTemplate.save(apCommentRepay);

        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        apComment.setReply(apComment.getReply() + 1);
        mongoTemplate.save(apComment);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult loadCommentRepayByCommentId(CommentRepayDto dto) {
        if (dto == null || dto.getCommentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        int size = 20;

        Query query = Query.query(Criteria.where("commentId").is(dto.getCommentId()).and("createdTime").lt(dto.getMinDate()));
        query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
        List<ApCommentRepay> commentRepays = mongoTemplate.find(query, ApCommentRepay.class);
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.okResult(commentRepays);
        }

        List<String> ids = commentRepays.stream()
                .map(commentRepay -> commentRepay.getCommentId())
                .collect(Collectors.toList());
        Query query1 = Query.query(Criteria.where("commentRepayId").in(ids).and("authorId").is(user.getId()));
        List<ApCommentRepayLike> commentRepayLikes = mongoTemplate.find(query1, ApCommentRepayLike.class);
        if (commentRepayLikes.size() == 0){
            return ResponseResult.okResult(commentRepays);
        }

        List<CommentRepayVo> result = new ArrayList();
        commentRepays.forEach(commentRepay -> {
            CommentRepayVo vo = new CommentRepayVo();
            BeanUtils.copyProperties(commentRepay,vo);
            for (ApCommentRepayLike like : commentRepayLikes) {
                if (commentRepay.getId().equals(like.getCommentRepayId())){
                    vo.setOperation((short)0);
                    break;
                }
            }
            result.add(vo);
        });
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult like(CommentRepayLikeDto dto) {
        if (dto == null || dto.getCommentRepayId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        ApCommentRepay commentRepay = mongoTemplate.findById(dto.getCommentRepayId(), ApCommentRepay.class);
        if (commentRepay != null && dto.getOperation() == 0) {
            commentRepay.setLikes(commentRepay.getLikes() + 1);
            mongoTemplate.save(commentRepay);

            ApCommentRepayLike commentRepayLike = new ApCommentRepayLike();
            commentRepayLike.setCommentRepayId(commentRepay.getId());
            commentRepayLike.setAuthorId(user.getId());
            mongoTemplate.save(commentRepayLike);
        }else{
            int likes = commentRepay.getLikes() - 1;
            likes = likes < 1 ? 0 : likes;
            commentRepay.setLikes(likes);
            mongoTemplate.save(commentRepay);

            Query query = Query.query(Criteria.where("commentRepayId").is(dto.getCommentRepayId()).and("authorId").is(user.getId()));
            mongoTemplate.remove(query,ApCommentRepayLike.class);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("likes",commentRepay.getLikes());
        return ResponseResult.okResult(map);
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
}
