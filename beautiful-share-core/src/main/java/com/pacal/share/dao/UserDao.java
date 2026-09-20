package com.pacal.share.dao;

import com.pacal.share.entity.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    /** 创建用户,返回自增 id */
    Integer insert(UserPO po);

    /** 通用按字段更新用户 */
    void update(UserPO po);

    /** 按 id 查询 */
    UserPO getById(@Param("id") Integer id);

    /** 按 id 查询(兼容旧名称) */
    UserPO getUser(@Param("id") Integer id);

    /** 按 openId 查询 */
    UserPO getByWxOpenId(@Param("wxOpenId") String wxOpenId);

    /** 按 id 列表批量查询 */
    List<UserPO> queryByIds(@Param("ids") List<Integer> ids);

    /** 按关键字分页查询(管理员) */
    List<UserPO> queryList(@Param("keyword") String keyword,
                           @Param("status") String status,
                           @Param("roleId") Integer roleId,
                           @Param("limit") Integer limit,
                           @Param("offset") Integer offset);

    /** 按条件计数 */
    int count(@Param("keyword") String keyword,
              @Param("status") String status,
              @Param("roleId") Integer roleId);
}