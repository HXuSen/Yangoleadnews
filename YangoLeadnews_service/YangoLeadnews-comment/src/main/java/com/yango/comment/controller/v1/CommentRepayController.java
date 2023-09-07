package com.yango.comment.controller.v1;

import com.yango.comment.service.CommentRepayService;
import com.yango.model.comment.dto.CommentRepayDto;
import com.yango.model.comment.dto.CommentRepayLikeDto;
import com.yango.model.comment.dto.CommentRepaySaveDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ClassName: CommentRepayController
 * Package: com.yango.comment.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-16:19
 */
@RestController
@RequestMapping("/api/v1/comment_repay")
public class CommentRepayController {
    @Autowired
    private CommentRepayService commentRepayService;

    @PostMapping("/save")
    public ResponseResult saveCommentRepay(@RequestBody CommentRepaySaveDto dto){
        return commentRepayService.saveCommentRepay(dto);
    }

    @PostMapping("/load")
    public ResponseResult loadCommentRepayByCommentId(@RequestBody CommentRepayDto dto){
        return commentRepayService.loadCommentRepayByCommentId(dto);
    }

    @PostMapping("/like")
    public ResponseResult like(@RequestBody CommentRepayLikeDto dto){
        return commentRepayService.like(dto);
    }
}
