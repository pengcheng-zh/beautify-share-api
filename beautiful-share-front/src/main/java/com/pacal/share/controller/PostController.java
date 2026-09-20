package com.pacal.share.controller;

import com.pacal.share.annotation.NeedLogin;
import com.pacal.share.common.BaseResponse;
import com.pacal.share.dao.UserDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.request.CommonListRequest;
import com.pacal.share.entity.request.CreatePostRequest;
import com.pacal.share.entity.vo.ListVO;
import com.pacal.share.entity.vo.MineSummaryVO;
import com.pacal.share.entity.vo.UserPostVO;
import com.pacal.share.service.PostService;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
public class PostController {

    @Resource
    PostService postService;

    /** 发表文章 */
    @NeedLogin
    @PostMapping("create")
    public BaseResponse<String> create(@RequestBody CreatePostRequest req) {
        postService.createOrUpdatePost( req );
        return BaseResponse.SUCCESS( );
    }

    /** 文章详情 */
    @GetMapping("detail/{id}")
    public BaseResponse<UserPostVO> detail(@PathVariable("id") Integer id) {
        return BaseResponse.success( postService.getPost( id ) );
    }

    /** 文章广场(已通过审核) */
    @GetMapping("feed")
    public BaseResponse<ListVO<UserPostVO>> feed(CommonListRequest req) {
        return BaseResponse.success( postService.feedPosts( req ) );
    }

    /** 我的文章 */
    @NeedLogin
    @GetMapping("mine")
    public BaseResponse<ListVO<UserPostVO>> mine(CommonListRequest req) {
        return BaseResponse.success( postService.feedMinePosts( req ) );
    }

    @NeedLogin
    @GetMapping("mine-summary")
    public BaseResponse<MineSummaryVO> mineSummary() {
        return BaseResponse.success( postService.getMineSummary( ) );
    }

    /** 删除文章 */
    @NeedLogin
    @DeleteMapping("/post/{id}")
    public BaseResponse<Void> delete(@PathVariable("id") Integer id) {
        postService.deletePost( id );
        return BaseResponse.success();
    }

    @NeedLogin
    @GetMapping("like-list")
    public BaseResponse<ListVO<UserPostVO>> likeList(CommonListRequest req) {
        return BaseResponse.success( postService.myLikedPosts( req ) );
    }

    @NeedLogin
    @GetMapping("favorite-list")
    public BaseResponse<ListVO<UserPostVO>> favoriteList(CommonListRequest req) {
        return BaseResponse.success( postService.myFavoritePosts( req ) );
    }

}