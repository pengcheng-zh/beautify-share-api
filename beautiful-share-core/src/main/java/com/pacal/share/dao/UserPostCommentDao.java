package com.pacal.share.dao;

import com.pacal.share.entity.dto.IdCountDTO;
import com.pacal.share.entity.po.UserPostCommentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPostCommentDao {
    /** 发评论 */
    Integer insert(UserPostCommentPO po);

    void update(UserPostCommentPO po);

    /** 按 id 查询 */
    UserPostCommentPO getById(@Param("id") Integer id);


    /** 文章详情: 该文章下已通过的评论 */
    List<UserPostCommentPO> queryByPostId(@Param("postId") Integer postId,
                                          @Param("status") String status,
                                          @Param("limit") Integer limit,
                                          @Param("offset") Integer offset);

    int countByPostId(@Param("postId") Integer postId,
                      @Param("status") String status);

    /** 批量统计各文章下已通过的评论数 */
    List<IdCountDTO> countByPostIds(@Param("postIds") List<Integer> postIds,
                                    @Param("status") String status);

    /** 用户中心: 我的评论 */
    List<UserPostCommentPO> queryByUserId(@Param("userId") Integer userId,
                                          @Param("status") String status,
                                          @Param("limit") Integer limit,
                                          @Param("offset") Integer offset);

    int countByUserId(@Param("userId") Integer userId,
                      @Param("status") String status);

    /** 管理员: 审核列表 */
    List<UserPostCommentPO> queryForAudit(@Param("status") String status,
                                          @Param("keyword") String keyword,
                                          @Param("limit") Integer limit,
                                          @Param("offset") Integer offset);

    int countForAudit(@Param("status") String status,
                      @Param("keyword") String keyword);
}