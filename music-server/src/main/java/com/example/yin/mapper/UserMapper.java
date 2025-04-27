package com.example.yin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.yin.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    int insert(User record);

    /**
     * 增加新用户
     * @param record - 用户信息
     * @return int
     */
    int insertSelective(User record);

    int updateByPrimaryKey(User record);

    int verifyPassword(@Param("username") String username, @Param("password") String password);

    int existUsername(String username);

    int updateUserMsg(User record);

    int updateUserAvatar(User record);

    int deleteUser(Integer id);

    List<User> allUser();

    List<User> userOfId(Integer id);

    List<User> loginStatus(String username);

}
