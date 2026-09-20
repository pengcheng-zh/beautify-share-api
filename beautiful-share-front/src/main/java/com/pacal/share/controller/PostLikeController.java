package com.pacal.share.controller;

import com.pacal.share.annotation.NeedLogin;
import com.pacal.share.common.BaseResponse;
import com.pacal.share.entity.vo.PostInteractionVO;
import com.pacal.share.service.PostLikeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post-like")
public class PostLikeController {
    @Resource
    PostLikeService postLikeService;

    /** 点赞 / 取消点赞 (重复调用即取反) */
    @NeedLogin
    @PostMapping("{postId}")
    public BaseResponse<PostInteractionVO> toggleLike(@PathVariable("postId") Integer postId) {
        postLikeService.toggleLike( postId );
        return BaseResponse.SUCCESS();
    }
}
