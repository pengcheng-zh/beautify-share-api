package com.pacal.share.controller;

import com.pacal.share.annotation.NeedLogin;
import com.pacal.share.common.BaseResponse;
import com.pacal.share.dao.UserDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.request.LoginRequest;
import com.pacal.share.entity.request.UserUpdateRequest;
import com.pacal.share.entity.vo.SessionUserVO;
import com.pacal.share.service.LoginService;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    LoginService loginService;

    @Resource
    UserDao userDao;

    /** 微信小程序登录 */
    @PostMapping("/login")
    public BaseResponse<SessionUserVO> login(@RequestBody LoginRequest request) {
        return BaseResponse.success( loginService.loginByWxMa( request ) );
    }

    /** 当前用户信息 */
    @NeedLogin
    @GetMapping("/me")
    public BaseResponse<SessionUserVO> me() {
        Integer userId = RequestUtil.getUserIdInt();
        UserPO user = userDao.getById( userId );
        if ( user == null ) {
            return BaseResponse.success();
        }
        SessionUserVO vo = SessionUserVO.from( user );
        return BaseResponse.success( vo );
    }

    @NeedLogin
    @PostMapping("update-gender")
    public BaseResponse<String> updateGender(@RequestBody UserUpdateRequest request) {
        loginService.updateUserGender( request.getGender() );
        return BaseResponse.SUCCESS();
    }

    @NeedLogin
    @PostMapping("update-profile")
    public BaseResponse<String> updateProfile(@RequestBody UserUpdateRequest request) {
        loginService.updateUserProfile( request );
        return BaseResponse.SUCCESS();
    }

    /** 健康检查 */
    @GetMapping("/ping")
    public BaseResponse<String> ping() {
        return BaseResponse.success( "pong" );
    }
}