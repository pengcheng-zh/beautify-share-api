package com.pacal.share.controller;

import com.pacal.share.annotation.NeedAdmin;
import com.pacal.share.annotation.NeedNormalAdmin;
import com.pacal.share.common.BaseResponse;
import com.pacal.share.dao.UserDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.request.AuditRequest;
import com.pacal.share.entity.request.CommonListRequest;
import com.pacal.share.entity.vo.ListVO;
import com.pacal.share.entity.vo.SessionUserVO;
import com.pacal.share.entity.vo.UserPostCommentVO;
import com.pacal.share.entity.vo.UserPostVO;
import com.pacal.share.service.AdminService;
import com.pacal.share.service.CommentService;
import com.pacal.share.service.PostService;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @Resource
    PostService postService;

    @Resource
    CommentService commentService;

    @Resource
    UserDao userDao;

    /* ================ 文章审核 ================ */

    /** 待审核文章列表 */
    @NeedNormalAdmin
    @GetMapping("/post/list")
    public BaseResponse<ListVO<UserPostVO>> listPostsForAudit(CommonListRequest req) {
        return BaseResponse.success( postService.queryForAudit( req ) );
    }

    /** 审核文章 */
    @NeedAdmin
    @PostMapping("/post/audit")
    public BaseResponse<Void> auditPost(@RequestBody AuditRequest req) {
        postService.auditPost( req );
        return BaseResponse.success();
    }

    /** 删除任意文章 */
    @NeedAdmin
    @PostMapping("/post/delete/{id}")
    public BaseResponse<Void> deletePost(@PathVariable("id") Integer id) {
        postService.deletePost( id );
        return BaseResponse.success();
    }

    /* ================ 评论审核 ================ */

    /** 待审核评论列表 */
    @NeedNormalAdmin
    @GetMapping("/comment/list")
    public BaseResponse<ListVO<UserPostCommentVO>> listCommentsForAudit(CommonListRequest req) {
        return BaseResponse.success( commentService.queryForAudit( req ) );
    }

    /** 审核评论 */
    @NeedAdmin
    @PostMapping("/comment/audit")
    public BaseResponse<Void> auditComment(@RequestBody AuditRequest req) {
        Integer operatorId = RequestUtil.getUserIdInt();
        commentService.auditComment( operatorId, req.getId(), req.getStatus(), req.getReason() );
        return BaseResponse.success();
    }

    /* ================ 用户管理 ================ */

    /** 查询用户 */
    @NeedAdmin
    @GetMapping("/user/list")
    public BaseResponse<ListVO<SessionUserVO>> listUsers(
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) String status,
            CommonListRequest req) {
        return BaseResponse.success( adminService.queryUsers( roleId, status, req ) );
    }

    /** 启停用用户 */
    @NeedAdmin
    @PostMapping("/user/status")
    public BaseResponse<Void> changeStatus(@RequestBody UserPO body) {
        Integer operatorId = RequestUtil.getUserIdInt();
        adminService.changeStatus( operatorId, body.getId(), body.getStatus() );
        return BaseResponse.success();
    }

    /* ----------- 私有 helper ----------- */

    private Integer getOperatorRoleId(Integer operatorId) {
        UserPO user = userDao.getById( operatorId );
        return user == null ? null : user.getRoleId();
    }

}