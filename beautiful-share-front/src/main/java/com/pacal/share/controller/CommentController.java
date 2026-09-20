package com.pacal.share.controller;

import com.pacal.share.annotation.NeedLogin;
import com.pacal.share.common.BaseResponse;
import com.pacal.share.dao.UserDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.request.CommonListRequest;
import com.pacal.share.entity.request.CreateCommentRequest;
import com.pacal.share.entity.vo.ListVO;
import com.pacal.share.entity.vo.UserPostCommentVO;
import com.pacal.share.service.CommentService;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post-comment")
public class CommentController {

    @Resource
    CommentService commentService;

    @Resource
    UserDao userDao;

    /** 发表评论 */
    @NeedLogin
    @PostMapping("create")
    public BaseResponse<String> create(@RequestBody CreateCommentRequest req) {
        commentService.createComment( req );
        return BaseResponse.SUCCESS();
    }

    /** 文章详情下的评论列表 */
    @GetMapping("list")
    public BaseResponse<ListVO<UserPostCommentVO>> list(@RequestParam("postId") Integer postId,
                                                       CommonListRequest req) {
        return BaseResponse.success( commentService.listByPost( postId, req ) );
    }

    /** 我的评论 */
    @NeedLogin
    @GetMapping("mine")
    public BaseResponse<ListVO<UserPostCommentVO>> mine(CommonListRequest req) {
        return BaseResponse.success( commentService.myComments( req ) );
    }

    /** 删除评论 */
    @NeedLogin
    @DeleteMapping("{id}")
    public BaseResponse<Void> delete(@PathVariable("id") Integer id) {
        commentService.deleteComment( id );
        return BaseResponse.success();
    }
}