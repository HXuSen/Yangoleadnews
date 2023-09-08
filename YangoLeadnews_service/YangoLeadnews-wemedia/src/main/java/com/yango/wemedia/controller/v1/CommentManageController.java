package com.yango.wemedia.controller.v1;

import com.yango.model.article.dtos.ArticleCommentDto;
import com.yango.model.comment.dto.CommentConfigDto;
import com.yango.model.comment.dto.CommentLikeDto;
import com.yango.model.comment.dto.CommentManageDto;
import com.yango.model.comment.dto.CommentRepaySaveDto;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.wemedia.service.CommentManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: CommentManageController
 * Package: com.yango.wemedia.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/7-18:43
 */
@RestController
@RequestMapping("/api/v1/comment/manage")
public class CommentManageController {
    @Autowired
    private CommentManageService commentManageService;

    @PostMapping("/find_news_comments")
    public ResponseResult findNewsComments(@RequestBody ArticleCommentDto dto){
        return commentManageService.findNewsComments(dto);
    }

    @PostMapping("/update_comment_status")
    public ResponseResult updateCommentStatus(@RequestBody CommentConfigDto dto){
        return commentManageService.updateCommentStatus(dto);
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody CommentManageDto dto){
        return commentManageService.list(dto);
    }

    @PostMapping("/comment_repay")
    public ResponseResult saveCommentRepay(@RequestBody CommentRepaySaveDto dto){
        return commentManageService.saveCommentRepay(dto);
    }

    @PostMapping("/like")
    public ResponseResult like(@RequestBody CommentLikeDto dto){
        return commentManageService.like(dto);
    }

    @DeleteMapping("/del_comment/{commentId}")
    public ResponseResult delComment(@PathVariable("commentId") String commentId){
        return commentManageService.delComment(commentId);
    }

    @DeleteMapping("/del_comment_repay/{commentRepayId}")
    public ResponseResult delCommentRepay(@PathVariable("commentRepayId") String commentRepayId){
        return commentManageService.delCommentRepay(commentRepayId);
    }
}
