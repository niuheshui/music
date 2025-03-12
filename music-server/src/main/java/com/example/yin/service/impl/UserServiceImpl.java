package com.example.yin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.domain.User;
import com.example.yin.mapper.UserMapper;
import com.example.yin.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    /**
     * 新增用户
     */
    @Override
    public boolean addUser(User user) {
        return getBaseMapper().insertSelective(user) > 0;
    }

    @Override
    public boolean updateUserMsg(User user) {
        return getBaseMapper().updateUserMsg(user) > 0;
    }

    @Override
    public boolean updateUserAvatar(User user) {

        return getBaseMapper().updateUserAvator(user) > 0;
    }

    @Override
    public boolean existUser(String username) {
        return getBaseMapper().existUsername(username) > 0;
    }

    @Override
    public boolean verityPasswd(String username, String password) {

        return getBaseMapper().verifyPassword(username, password) > 0;
    }

    //    删除用户
    @Override
    public boolean deleteUser(Integer id) {
        return getBaseMapper().deleteUser(id) > 0;
    }

    @Override
    public List<User> allUser() {
        return getBaseMapper().allUser();
    }

    @Override
    public List<User> userOfId(Integer id) {

        return getBaseMapper().userOfId(id);
    }

    @Override
    public List<User> loginStatus(String username) {

        return getBaseMapper().loginStatus(username);
    }
}
