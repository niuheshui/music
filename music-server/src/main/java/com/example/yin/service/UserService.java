package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.domain.User;

import java.util.List;

public interface UserService extends IService<User> {

    boolean addUser(User user);

    boolean updateUserMsg(User user);

    boolean updateUserAvatar(User user);

    boolean existUser(String username);

    boolean verityPasswd(String username, String password);

    boolean deleteUser(Integer id);

    List<User> allUser();

    List<User> userOfId(Integer id);

    List<User> loginStatus(String username);

}
