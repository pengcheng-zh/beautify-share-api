package com.pacal.share.service;

import com.pacal.share.common.Constants;
import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.dao.UserDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.request.CommonListRequest;
import com.pacal.share.entity.vo.ListVO;
import com.pacal.share.entity.vo.SessionUserVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员服务: 用户管理 + 启用/停用
 */
@Slf4j
@Service
public class AdminService {

    @Resource
    UserDao userDao;

    public ListVO<SessionUserVO> queryUsers(Integer roleId, String status, CommonListRequest req) {
        List<UserPO> users = userDao.queryList( req.getKeyword(), status, roleId,
                req.getPageSize(), req.getOffset() );
        int total = userDao.count( req.getKeyword(), status, roleId );
        List<SessionUserVO> data = users.stream().map( SessionUserVO::from ).toList();
        return ListVO.of( total, req.getPageIndex(), req.getPageSize(), data );
    }

    public void changeStatus(Integer operatorId, Integer targetUserId, String status) {
        if ( !Constants.ACTIVE_STATUS.equals( status ) && !Constants.INACTIVE_STATUS.equals( status ) ) {
            throw new PacalException( ErrorCode.PARAMS_ERROR );
        }
        if ( targetUserId.equals( operatorId ) ) {
            throw new PacalException( ErrorCode.NO_RIGHT );
        }
        UserPO user = userDao.getById( targetUserId );
        if ( user == null ) {
            throw new PacalException( ErrorCode.USER_NOT_EXIST );
        }
        user.setStatus( status );
        userDao.update( user );
    }

    public void changeRole(Integer operatorId, Integer targetUserId, Integer roleId) {
        UserPO user = userDao.getById( targetUserId );
        if ( user == null ) {
            throw new PacalException( ErrorCode.USER_NOT_EXIST );
        }
        if ( targetUserId.equals( operatorId ) ) {
            throw new PacalException( ErrorCode.NO_RIGHT );
        }
        user.setRoleId( roleId );
        userDao.update( user );
    }
}