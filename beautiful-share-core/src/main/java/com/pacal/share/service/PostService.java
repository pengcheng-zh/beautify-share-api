package com.pacal.share.service;

import com.pacal.share.common.Constants;
import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.dao.UserDao;
import com.pacal.share.dao.UserPostDao;
import com.pacal.share.dao.UserPostFavoriteDao;
import com.pacal.share.dao.UserPostLikeDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.entity.po.UserPostLikePO;
import com.pacal.share.entity.po.UserPostPO;
import com.pacal.share.entity.request.AuditRequest;
import com.pacal.share.entity.request.CommonListRequest;
import com.pacal.share.entity.request.CreatePostRequest;
import com.pacal.share.entity.vo.ListVO;
import com.pacal.share.entity.vo.MineSummaryVO;
import com.pacal.share.entity.vo.UserPostVO;
import com.pacal.share.entity.vo.UserSimpleVO;
import com.pacal.share.enumm.ApproveStatusEnum;
import com.pacal.share.enumm.UserRoleEnum;
import com.pacal.share.utils.CommUtil;
import com.pacal.share.utils.DateUtil;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

    @Resource
    UserPostDao userPostDao;

    @Resource
    UserDao userDao;

    @Resource
    UserPostLikeDao postLikeDao;

    @Resource
    UserPostFavoriteDao favoriteDao;

    private static final int MAX_CONTENT_LEN = 5000;
    private static final int MAX_LOCATION_LEN = 100;
    private static final int MAX_VOICE_LEN = 500;
    private static final int MAX_PICTURES = 9;


    /** 发表文章 */
    public void createOrUpdatePost(CreatePostRequest req) {
        int userId = RequestUtil.getUserIdInt();
        if ( req == null || StringUtils.isBlank( req.getContent() ) ) {
            throw new PacalException( ErrorCode.POST_CONTENT_EMPTY );
        }
        if ( req.getContent().length() > MAX_CONTENT_LEN ) {
            throw new PacalException( ErrorCode.POST_CONTENT_TOO_LONG );
        }
        if ( req.getPictures() != null && req.getPictures().size() > MAX_PICTURES ) {
            throw new PacalException( ErrorCode.POST_PICTURES_TOO_MANY );
        }
        if ( StringUtils.length( req.getLocation() ) > MAX_LOCATION_LEN ) {
            throw new PacalException( ErrorCode.POST_LOCATION_TOO_LONG );
        }
        if ( StringUtils.length( req.getVoice() ) > MAX_VOICE_LEN ) {
            throw new PacalException( ErrorCode.POST_VOICE_TOO_LONG );
        }

        UserPostPO po = new UserPostPO();
        po.setUserId( userId );
        po.setContent( req.getContent().trim() );
        po.setLocation( req.getLocation() );
        po.setLatitude( req.getLatitude() );
        po.setLongitude( req.getLongitude() );
        po.setVoice( req.getVoice() );
        po.setDuration( req.getDuration() );
        po.setPictures(CommUtil.strListToStr(req.getPictures()));
        po.setStatus( ApproveStatusEnum.WAITING_APPROVE.status );
        if (Objects.isNull(req.getId())) {
            userPostDao.insert( po );
        } else {
            UserPostPO oldPo = userPostDao.getById( req.getId() );
            if (Objects.isNull(oldPo)) {
                throw new PacalException( ErrorCode.POST_NOT_EXIST );
            }
            if (!oldPo.getUserId().equals(userId)) {
                throw new PacalException( ErrorCode.NO_RIGHT );
            }
            userPostDao.update( po );
        }

    }

    /** 删除文章 (本人或管理员) */
    public void deletePost(Integer postId) {
        int userId = RequestUtil.getUserIdInt();

        UserPostPO post = userPostDao.getById( postId );
        if ( Objects.isNull(post) ) {
            throw new PacalException( ErrorCode.POST_NOT_EXIST );
        }
        if (Constants.POST_DELETED.equals(post.getStatus())) {
            return;
        }
        if (!post.getUserId().equals(userId)) {
            UserPO user = userDao.getById( userId );
            if (!user.getRoleId().equals(UserRoleEnum.ADMIN.roleId)) {
                throw new PacalException( ErrorCode.NO_RIGHT );
            }
        }
        userPostDao.deleteById( postId );
    }

    /** 文章详情 */
    public UserPostVO getPost(Integer postId) {
        UserPostPO po = userPostDao.getById( postId );
        if ( po == null ) {
            throw new PacalException( ErrorCode.POST_NOT_EXIST );
        }
        int viewerId = RequestUtil.getUserIdInt();
        return fillList(List.of( po ), viewerId ).getFirst();
    }

    /**
     * 文章列表
     *
     */
    public ListVO<UserPostVO> feedPosts(CommonListRequest req) {
        int viewerId = RequestUtil.getUserIdInt();
        String status = ApproveStatusEnum.APPROVED.status;
        List<UserPostPO> posts = userPostDao.queryFeed( req.getKeyword(), null, status,
                req.getPageSize(), req.getOffset() );

        return ListVO.of( 0, req.getPageIndex(), req.getPageSize(),
                fillList( posts, viewerId ) );
    }

    public ListVO<UserPostVO> feedMinePosts(CommonListRequest req) {
        int userId = RequestUtil.getUserIdInt();
        // 用户中心: 看自己
        List<UserPostPO> posts = userPostDao.queryByUserId( userId, req.getStatus(),
                req.getPageSize(), req.getOffset() );
        int total = userPostDao.countByUserId( userId, null );
        return ListVO.of( total, req.getPageIndex(), req.getPageSize(),
                fillList( posts, userId ) );
    }

    /** 我点赞过的文章 */
    public ListVO<UserPostVO> myLikedPosts(CommonListRequest req) {
        int userId = RequestUtil.getUserIdInt();
        List<Integer> postIds = postLikeDao.queryUserLikedPostIds( userId, req.getPageSize(), req.getOffset() );
        if (CollectionUtils.isEmpty(postIds)) {
            return ListVO.of(0, req.getPageIndex(), req.getPageSize(), List.of());
        }
        return buildByPostIds( postIds, 0, userId, req );
    }

    /** 我收藏的文章 */
    public ListVO<UserPostVO> myFavoritePosts(CommonListRequest req) {
        int userId = RequestUtil.getUserIdInt();
        List<Integer> postIds = favoriteDao.queryPostIdsByUserId( userId,
                req.getPageSize(), req.getOffset() );
        int total = favoriteDao.countByUserId( userId );
        return buildByPostIds( postIds, total, userId, req );
    }

    /** 管理员 / 作者本人: 列出指定状态的文章 */
    public ListVO<UserPostVO> queryForAudit(CommonListRequest req) {
        int userId = RequestUtil.getUserIdInt();
        List<UserPostPO> posts = userPostDao.queryForAudit( ApproveStatusEnum.WAITING_APPROVE.status, req.getKeyword(),
                req.getPageSize(), req.getOffset() );
        int total = userPostDao.countForAudit( ApproveStatusEnum.WAITING_APPROVE.status, req.getKeyword() );
        return ListVO.of( total, req.getPageIndex(), req.getPageSize(),
                fillList( posts, userId ) );
    }

    /** 管理员审核文章 */
    public void auditPost(AuditRequest req) {
        int operatorId = RequestUtil.getUserIdInt();
        if ( !ApproveStatusEnum.isValid( req.getStatus() ) ) {
            throw new PacalException( ErrorCode.APPROVE_STATUS_INVALID );
        }
        if ( ApproveStatusEnum.DISAPPROVED.status.equals( req.getStatus() ) && StringUtils.isBlank( req.getReason() ) ) {
            throw new PacalException( ErrorCode.POST_REJECT_REASON );
        }
        UserPostPO post = userPostDao.getById( req.getId() );
        if ( post == null ) {
            throw new PacalException( ErrorCode.POST_NOT_EXIST );
        }
        UserPostPO updatePO = new UserPostPO();
        updatePO.setId( req.getId() );
        updatePO.setStatus( req.getStatus() );
        updatePO.setAuditUserId( operatorId );
        updatePO.setAuditReason( req.getReason() );
        updatePO.setAuditTime( DateUtil.getCurrentDate() );
        userPostDao.update( updatePO );
    }

    /* ----------- 私有 helper ----------- */

    /** 按文章 id 顺序组装列表(用于点赞/收藏列表, 已删除的文章会被跳过) */
    private ListVO<UserPostVO> buildByPostIds(List<Integer> postIds, int total,
                                              Integer viewerId, CommonListRequest req) {
        if ( CollectionUtils.isEmpty( postIds ) ) {
            return ListVO.of( total, req.getPageIndex(), req.getPageSize(), List.of() );
        }
        List<UserPostPO> posts = userPostDao.queryByIds( postIds );
        return ListVO.of( total, req.getPageIndex(), req.getPageSize(),
                fillList( posts, viewerId ) );
    }

    private List<UserPostVO> fillList(List<UserPostPO> posts, Integer viewerId) {
        if ( CollectionUtils.isEmpty( posts ) ) {
            return List.of();
        }
        List<Integer> postIds = posts.stream().map( UserPostPO::getId ).toList();
        // 批量获取作者信息
        List<Integer> userIds = posts.stream().map( UserPostPO::getUserId )
                .filter( Objects::nonNull ).distinct().collect( Collectors.toList() );
        List<UserPO> users = userDao.queryByIds( userIds );
        Map<Integer, UserPO> userMap = users.stream()
                .collect( Collectors.toMap( UserPO::getId, Function.identity(), (a, b) -> a ) );

        // 浏览用户点赞过的
        List<Integer> likedPostIds = postLikeDao.queryLikedPostIds( viewerId, postIds );
        List<Integer> favoritePostIds = favoriteDao.queryFavoritedPostIds( viewerId, postIds );

        // 每个post最近的三个点赞过的用户
        Map<Integer, List<UserSimpleVO>> likeUserMap = appendLikeUsers( postIds );

        List<UserPostVO> result = new ArrayList<>( posts.size() );
        for ( UserPostPO po : posts ) {
            UserPostVO vo = UserPostVO.from( po );
            UserPO author = userMap.get( po.getUserId() );
            if ( author != null ) {
                vo.setUsername( author.getUsername() );
                vo.setAvatar( author.getAvatar() );
            }
            vo.setLiked(likedPostIds.contains(po.getId()));
            vo.setFavorited(favoritePostIds.contains(po.getId()));
            vo.setLikedUserList(likeUserMap.get(po.getId()));
            result.add( vo );
        }
        return result;
    }

    private Map<Integer, List<UserSimpleVO>> appendLikeUsers(List<Integer> postIds) {
        List<UserPostLikePO> postLikePos = postLikeDao.queryLikeUsers( postIds );
        if (CollectionUtils.isEmpty(postLikePos)) {
            return Collections.emptyMap();
        }
        List<Integer> likeUserIds = postLikePos.stream().map( UserPostLikePO::getUserId ).distinct().toList();
        List<UserPO> userPOList = userDao.queryByIds( likeUserIds );
        Map<Integer, UserPO> userPOMap = userPOList.stream().collect( Collectors.toMap( UserPO::getId, Function.identity(), (a, b) -> a ) );

        Map<Integer, List<UserSimpleVO>> userSimpleVOListMap = new HashMap<>();
        postLikePos.forEach( likePO -> {
            userSimpleVOListMap.computeIfAbsent( likePO.getPostId(), k -> new ArrayList<>() ).add( UserSimpleVO.from( userPOMap.get( likePO.getUserId() ) ) );
        } );

        return userSimpleVOListMap;
    }

    public MineSummaryVO getMineSummary() {
        int userId = RequestUtil.getUserIdInt();
        int likeCount = postLikeDao.countByUserId( userId );
        int favoriteCount = favoriteDao.countByUserId( userId );
        int postCount = userPostDao.countByUserId( userId, null );

        return new MineSummaryVO( postCount, likeCount, favoriteCount );
    }
}
