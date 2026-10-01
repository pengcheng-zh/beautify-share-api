package com.pacal.share.service;

import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.dao.UserDao;
import com.pacal.share.dao.UserPostCommentDao;
import com.pacal.share.dao.UserPostDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.po.UserPostCommentPO;
import com.pacal.share.entity.po.UserPostPO;
import com.pacal.share.entity.request.CommonListRequest;
import com.pacal.share.entity.request.CreateCommentRequest;
import com.pacal.share.entity.vo.ListVO;
import com.pacal.share.entity.vo.UserPostCommentVO;
import com.pacal.share.enumm.ApproveStatusEnum;
import com.pacal.share.enumm.UserRoleEnum;
import com.pacal.share.utils.DateUtil;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {

    @Resource
    UserPostCommentDao commentDao;

    @Resource
    UserPostDao userPostDao;

    @Resource
    UserDao userDao;

    private static final int MAX_CONTENT_LEN = 500;

    /** 评论文章 */
    public void createComment(CreateCommentRequest req) {
        int userId = RequestUtil.getUserIdInt();
        if ( req == null || req.getPostId() == null ) {
            throw new PacalException( ErrorCode.COMMENT_POST_NOT_EXIST );
        }
        if ( StringUtils.isBlank( req.getContent() ) ) {
            throw new PacalException( ErrorCode.COMMENT_CONTENT_EMPTY );
        }
        if ( req.getContent().length() > MAX_CONTENT_LEN ) {
            throw new PacalException( ErrorCode.COMMENT_CONTENT_TOO_LONG );
        }
        // 文章存在 & 已通过审核
        UserPostPO post = userPostDao.getById( req.getPostId() );
        if ( post == null ) {
            throw new PacalException( ErrorCode.COMMENT_POST_NOT_EXIST );
        }

        UserPostCommentPO po = new UserPostCommentPO();
        po.setUserId( userId );
        po.setPostId( req.getPostId() );
        po.setContent( req.getContent().trim() );
        po.setStatus( ApproveStatusEnum.WAITING_APPROVE.status );
        commentDao.insert( po );
    }

    /** 删除评论 (作者本人/文章作者/管理员) */
    public void deleteComment(Integer commentId) {
        UserPostCommentPO commentPO = commentDao.getById( commentId );
        if ( commentPO == null ) {
            throw new PacalException( ErrorCode.COMMENT_NOT_EXIST );
        }
        if (ApproveStatusEnum.DISAPPROVED.status.equals( commentPO.getStatus() )) {
            return;
        }
        int userId = RequestUtil.getUserIdInt();
        if ( !commentPO.getUserId().equals( userId ) ) {
            UserPO userPO = userDao.getUser( userId );
            if (!UserRoleEnum.ADMIN.roleId.equals( userPO.getRoleId() )) {
                throw new PacalException( ErrorCode.NO_RIGHT );
            }
        }
        UserPostCommentPO updatePO = new UserPostCommentPO();
        updatePO.setId(commentId);
        updatePO.setStatus( ApproveStatusEnum.DELETED.status );
        commentDao.update( updatePO );
    }

    /** 文章详情页: 拉取已通过的评论 */
    public ListVO<UserPostCommentVO> listByPost(Integer postId, CommonListRequest req) {
        String status = ApproveStatusEnum.APPROVED.status;
        List<UserPostCommentPO> list = commentDao.queryByPostId( postId, status,
                req.getPageSize(), req.getOffset() );
        int total = commentDao.countByPostId( postId, status );
        return ListVO.of( total, req.getPageIndex(), req.getPageSize(), fillList( list ) );
    }

    /** 我的评论 */
    public ListVO<UserPostCommentVO> myComments(CommonListRequest req) {
        int userId = RequestUtil.getUserIdInt();
        List<UserPostCommentPO> list = commentDao.queryByUserId( userId, null,
                req.getPageSize(), req.getOffset() );
        int total = commentDao.countByUserId( userId, null );
        return ListVO.of( total, req.getPageIndex(), req.getPageSize(), fillList( list ) );
    }

    /** 管理员: 审核列表 */
    public ListVO<UserPostCommentVO> queryForAudit(CommonListRequest req) {
        List<UserPostCommentPO> list = commentDao.queryForAudit( ApproveStatusEnum.WAITING_APPROVE.status, req.getKeyword(), req.getPageSize(), req.getOffset() );
        int total = commentDao.countForAudit( ApproveStatusEnum.WAITING_APPROVE.status, req.getKeyword() );
        return ListVO.of( total, req.getPageIndex(), req.getPageSize(), fillList( list ) );
    }

    /** 管理员审核评论 */
    public void auditComment(Integer operatorId, Integer commentId, String status, String reason) {
        if ( !ApproveStatusEnum.isValid( status ) ) {
            throw new PacalException( ErrorCode.APPROVE_STATUS_INVALID );
        }
        if ( ApproveStatusEnum.DISAPPROVED.status.equals( status ) && StringUtils.isBlank( reason ) ) {
            throw new PacalException( ErrorCode.POST_REJECT_REASON );
        }
        UserPostCommentPO commentPO = commentDao.getById( commentId );
        if ( commentPO == null ) {
            throw new PacalException( ErrorCode.COMMENT_NOT_EXIST );
        }

        UserPostCommentPO updatePO = new UserPostCommentPO();
        updatePO.setId( commentId );
        updatePO.setStatus( status );
        updatePO.setAuditUserId( operatorId );
        updatePO.setAuditReason( reason );
        updatePO.setAuditTime( DateUtil.getCurrentDate() );
        commentDao.update( updatePO );

        if ( ApproveStatusEnum.APPROVED.status.equals( status ) ) {
            // 增加文章评论数
            userPostDao.increaseCommentCount( commentPO.getPostId() );
        }
    }

    /* ---------- helpers ---------- */

    private UserPostCommentVO fillVO(UserPostCommentPO po) {
        UserPostCommentVO vo = UserPostCommentVO.from( po );
        if ( po.getUserId() != null ) {
            UserPO u = userDao.getById( po.getUserId() );
            if ( u != null ) {
                vo.setUsername( u.getUsername() );
                vo.setAvatar( u.getAvatar() );
            }
        }
        return vo;
    }

    private List<UserPostCommentVO> fillList(List<UserPostCommentPO> list) {
        if ( CollectionUtils.isEmpty( list ) ) return List.of();
        List<Integer> userIds = list.stream().map( UserPostCommentPO::getUserId )
                .filter( java.util.Objects::nonNull ).distinct().collect( Collectors.toList() );
        Map<Integer, UserPO> userMap = new HashMap<>();
        if ( !userIds.isEmpty() ) {
            List<UserPO> users = userDao.queryByIds( userIds );
            for ( UserPO u : users ) userMap.put( u.getId(), u );
        }
        List<UserPostCommentVO> result = new ArrayList<>( list.size() );
        for ( UserPostCommentPO po : list ) {
            UserPostCommentVO vo = UserPostCommentVO.from( po );
            UserPO author = userMap.get( po.getUserId() );
            if ( author != null ) {
                vo.setUsername( author.getUsername() );
                vo.setAvatar( author.getAvatar() );
            }
            result.add( vo );
        }
        return result;
    }
}