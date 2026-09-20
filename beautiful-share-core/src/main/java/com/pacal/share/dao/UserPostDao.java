package com.pacal.share.dao;

import com.pacal.share.entity.po.UserPostPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPostDao {
    /** 发文 */
    Integer insert(UserPostPO po);

    /** 更新文章(用户编辑) */
    void update(UserPostPO po);

    /** 按 id 查询 */
    UserPostPO getById(@Param("id") Integer id);

    /** 按 id 列表批量查询 */
    List<UserPostPO> queryByIds(@Param("ids") List<Integer> ids);

    /** 删除文章(作者/管理员) */
    void deleteById(@Param("id") Integer id);

    /** 审核(管理员) */
    void audit(@Param("id") Integer id,
               @Param("status") String status,
               @Param("auditUserId") Integer auditUserId,
               @Param("auditReason") String auditReason,
               @Param("auditTime") String auditTime);

    /** 用户中心: 我的文章 */
    List<UserPostPO> queryByUserId(@Param("userId") Integer userId,
                                  @Param("status") String status,
                                  @Param("limit") Integer limit,
                                  @Param("offset") Integer offset);

    int countByUserId(@Param("userId") Integer userId,
                      @Param("status") String status);

    /** 文章广场(已通过审核的文章) */
    List<UserPostPO> queryFeed(@Param("keyword") String keyword,
                               @Param("userId") Integer userId,
                               @Param("status") String status,
                               @Param("limit") Integer limit,
                               @Param("offset") Integer offset);

    int countFeed(@Param("keyword") String keyword,
                  @Param("userId") Integer userId,
                  @Param("status") String status);

    /** 管理员: 审核列表(待审核/全部) */
    List<UserPostPO> queryForAudit(@Param("status") String status,
                                   @Param("keyword") String keyword,
                                   @Param("limit") Integer limit,
                                   @Param("offset") Integer offset);

    int countForAudit(@Param("status") String status,
                      @Param("keyword") String keyword);
}