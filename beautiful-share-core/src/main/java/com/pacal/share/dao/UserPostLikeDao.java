package com.pacal.share.dao;

import com.pacal.share.entity.dto.IdCountDTO;
import com.pacal.share.entity.po.UserPostLikePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPostLikeDao {

    /** 点赞 */
    int insert(UserPostLikePO po);
    void updateById(UserPostLikePO po);

    UserPostLikePO getByUserIdAndPostId(@Param("userId") Integer userId, @Param("postId") Integer postId);


    List<Integer> queryUserLikedPostIds(@Param("userId") Integer userId, @Param("limit") Integer limit, @Param("offset") Integer offset);
    /** 批量查询当前用户已点赞的文章id */
    List<Integer> queryLikedPostIds(@Param("userId") Integer userId,
                                    @Param("postIds") List<Integer> postIds);

    /** 我点赞过的文章id(按时间倒序分页) */
    List<Integer> queryPostIdsByUserId(@Param("userId") Integer userId,
                                       @Param("limit") Integer limit,
                                       @Param("offset") Integer offset);

    List<UserPostLikePO> queryLikeUsers(@Param("postIds") List<Integer> postIds);

    int countByUserId(int userId);
}
