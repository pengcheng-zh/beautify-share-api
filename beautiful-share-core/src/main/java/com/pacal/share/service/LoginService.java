package com.pacal.share.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.pacal.share.common.Constants;
import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.dao.UserDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.request.LoginRequest;
import com.pacal.share.entity.request.UserUpdateRequest;
import com.pacal.share.entity.vo.SessionUserVO;
import com.pacal.share.enumm.UserRoleEnum;
import com.pacal.share.utils.DateUtil;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LoginService {

    @Resource
    UserDao userDao;

    @Resource
    TokenService tokenService;

    @Resource
    WxMaService wxMaService;

    /**
     * 微信小程序登录 (返回 token)
     */
    public SessionUserVO loginByWxMa(LoginRequest request) {
        if ( StringUtils.isBlank( request.getCode() ) ) {
            throw new PacalException( ErrorCode.WX_ACCESS_CODE_EMPTY );
        }
        String openId;
        String unionId = "";
        try {
            WxMaJscode2SessionResult session = wxMaService.jsCode2SessionInfo( request.getCode() );
            if ( session == null || StringUtils.isBlank( session.getOpenid() ) ) {
                throw new PacalException( ErrorCode.WX_ACCESS_CODE_INVALID );
            }
            openId = session.getOpenid();
            unionId = StringUtils.defaultString( session.getUnionid() );
        } catch ( PacalException ex ) {
            throw ex;
        } catch ( Exception ex ) {
            log.error( "wxMa login failed", ex );
            throw new PacalException( ErrorCode.WX_ACCESS_CODE_INVALID );
        }

        UserPO user = userDao.getByWxOpenId( openId );
        String now = DateUtil.getCurrentDate();
        if ( user == null ) {
            user = new UserPO();
            user.setWxOpenId( openId );
            user.setWxUnionId( unionId );
            user.setUsername( StringUtils.defaultIfBlank( request.getUsername(),
                    StringUtils.defaultIfBlank( request.getNickName(), "新用户" ) ) );
            user.setAvatar( StringUtils.defaultIfBlank( request.getAvatarUrl(), "" ) );
            user.setGender( request.getGender() == null ? 0 : request.getGender() );
            user.setRoleId( UserRoleEnum.NORMAL.roleId );
            user.setStatus( Constants.ACTIVE_STATUS );
            user.setLastLoginTime( now );
            userDao.insert( user );
        } else {
            // 更新登录时间 / 微信信息
            user.setLastLoginTime( now );
            if ( StringUtils.isNotBlank( unionId ) ) user.setWxUnionId( unionId );
            if ( StringUtils.isNotBlank( request.getAvatarUrl() ) ) user.setAvatar( request.getAvatarUrl() );
            if ( StringUtils.isNotBlank( request.getNickName() ) ) user.setUsername( request.getNickName() );
            if ( request.getGender() != null ) user.setGender( request.getGender() );
            userDao.update( user );
            if ( Constants.INACTIVE_STATUS.equals( user.getStatus() ) ) {
                throw new PacalException( ErrorCode.USER_DISABLED );
            }
        }

        String token = tokenService.getToken(
                user.getId().toString(),
                user.getRoleId() == null ? "" : user.getRoleId().toString() );
        SessionUserVO vo = SessionUserVO.from( user );
        vo.setToken( token );
        return vo;
    }

    public void updateUserGender(int sex) {
        List<String> avatar = List.of("https://image.xianshu.site/2026-10-01/de58b40f-d7db-4af3-884d-ff0fa99ed5a2.png", "https://image.xianshu.site/2026-10-01/0d840e18-5ede-4076-b4e8-31aeef06f323.png");
        int userId = RequestUtil.getUserIdInt();
        UserPO userPO = new UserPO();
        userPO.setId(userId);
        userPO.setGender(sex);
        userPO.setAvatar(avatar.get(sex));
        userDao.update( userPO );
    }

    public void updateUserProfile(UserUpdateRequest request) {
        int userId = RequestUtil.getUserIdInt();
        UserPO userPO = new UserPO();
        userPO.setId(userId);
        userPO.setUsername(request.getUsername());
        userPO.setAvatar(request.getAvatar());
        userPO.setDescription(request.getDescription());
        userDao.update(userPO);
    }
    /**
     * 通过 userId 模拟一个超级管理员登录 token (本地调试使用)
     */
    public String generateAdminToken(Integer userId) {
        return tokenService.getToken( userId.toString(),
                Integer.toString( com.pacal.share.enumm.UserRoleEnum.ADMIN.roleId ) );
    }
}