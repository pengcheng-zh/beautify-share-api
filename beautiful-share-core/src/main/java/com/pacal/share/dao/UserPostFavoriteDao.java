package com.pacal.share.dao;

import com.pacal.share.entity.dto.IdCountDTO;
import com.pacal.share.entity.po.UserPostFavoritePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPostFavoriteDao {

    /** 收藏 */
    int insert(UserPostFavoritePO po);
    void updateById(UserPostFavoritePO po);

    UserPostFavoritePO getByUserIdAndPostId(@Param("userId") Integer userId, @Param("postId") Integer postId);

    /** 取消收藏(返回影响行数) */
    int deleteByUserAndPost(@Param("userId") Integer userId, @Param("postId") Integer postId);

    /** 文章被删除时清理收藏记录 */
    int deleteByPostId(@Param("postId") Integer postId);

    /** 是否已收藏 */
    int countByUserAndPost(@Param("userId") Integer userId, @Param("postId") Integer postId);

    /** 单篇文章收藏数 */
    int countByPostId(@Param("postId") Integer postId);

    /** 批量统计收藏数 */
    List<IdCountDTO> countByPostIds(@Param("postIds") List<Integer> postIds);

    /** 批量查询当前用户已收藏的文章id */
    List<Integer> queryFavoritedPostIds(@Param("userId") Integer userId,
                                        @Param("postIds") List<Integer> postIds);

    /** 我收藏的文章id(按时间倒序分页) */
    List<Integer> queryPostIdsByUserId(@Param("userId") Integer userId,
                                       @Param("limit") Integer limit,
                                       @Param("offset") Integer offset);

    int countByUserId(@Param("userId") Integer userId);
}
