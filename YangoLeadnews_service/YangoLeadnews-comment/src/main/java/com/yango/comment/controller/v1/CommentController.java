package com.yango.comment.controller.v1;

import com.yango.comment.service.CommentService;
import com.yango.model.comment.dto.CommentDto;
import com.yango.model.comment.dto.CommentLikeDto;
import com.yango.model.comment.dto.CommentSaveDto;
import com.yango.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CommentController
 * Package: com.yango.comment.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/6-20:25
 */
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public ResponseResult saveComment(@RequestBody CommentSaveDto dto){
        return commentService.saveComment(dto);
    }

    @PostMapping("/like")
    public ResponseResult like(@RequestBody CommentLikeDto dto){
        return commentService.like(dto);
    }

    @PostMapping("/load")
    public ResponseResult loadCommentsByArticleId(@RequestBody CommentDto dto){
        return commentService.loadCommentsByArticleId(dto);
    }
}
