package com.example.yin.controller;

import cn.hutool.json.JSONObject;
import com.example.yin.constant.Constants;
import com.example.yin.domain.User;
import com.example.yin.service.HistoryService;
import com.example.yin.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private HistoryService historyService;

    /**
     * 用户注册
     */
    @ResponseBody
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public Object addUser(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        String avator = req.getParameter("avator").trim();

        if ("".equals(username)) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "用户名不能为空喔");
            return jsonObject;
        }
        User user = new User();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        if ("".equals(phone_num)) {
            user.setPhoneNum(null);
        } else {
            user.setPhoneNum(phone_num);
        }

        if ("".equals(email)) {
            user.setEmail(null);
        } else {
            user.setEmail(email);
        }
        user.setBirth(myBirth);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setAvatar(avator);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());


        try {
            boolean res = userService.addUser(user);
            if (res) {
                jsonObject.put("code", 1);
                jsonObject.put("success", true);
                jsonObject.put("msg", "注册成功");
                jsonObject.put("type", "success");
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("success", false);
                jsonObject.put("msg", "注册失败");
                jsonObject.put("type", "error");
            }
            return jsonObject;
        } catch (DuplicateKeyException e) {
            jsonObject.put("code", 2);
            jsonObject.put("success", false);
            jsonObject.put("msg", "用户名已存在");
            jsonObject.put("type", "error");
            return jsonObject;
        }
    }

    /**
     * 判断是否登录成功
     */
    @ResponseBody
    @RequestMapping(value = "/user/login/status", method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest req, HttpSession session) {

        JSONObject jsonObject = new JSONObject();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean res = userService.verityPasswd(username, password);

        if (res) {
            jsonObject.put("code", 1);
            jsonObject.put("success", true);
            jsonObject.put("msg", "登录成功");
            jsonObject.put("type", "success");
            jsonObject.put("userMsg", userService.loginStatus(username));
            session.setAttribute("username", username);
        } else {
            jsonObject.put("code", 0);
            jsonObject.put("success", false);
            jsonObject.put("msg", "用户名或密码错误");
            jsonObject.put("type", "error");
        }
        return jsonObject;

    }

    /**
     * 返回所有用户
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Object allUser() {
        return userService.allUser();
    }

    /**
     * 返回指定ID的用户
     */
    @RequestMapping(value = "/user/detail", method = RequestMethod.GET)
    public Object userOfId(HttpServletRequest req) {
        String id = req.getParameter("id");
        return userService.userOfId(Integer.parseInt(id));
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.GET)
    public Object deleteUser(HttpServletRequest req) {
        String id = req.getParameter("id");
        return userService.deleteUser(Integer.parseInt(id));
    }

    /**
     * 更新用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public Object updateUserMsg(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        // String avator = req.getParameter("avator").trim();
        // System.out.println(username+"  "+password+"  "+sex+"   "+phone_num+"     "+email+"      "+birth+"       "+introduction+"      "+location);

        if ("".equals(username)) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "用户名或密码错误");
            return jsonObject;
        }
        User user = new User();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setId(Integer.parseInt(id));
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        user.setPhoneNum(phone_num);
        user.setEmail(email);
        user.setBirth(myBirth);
        user.setIntroduction(introduction);
        user.setLocation(location);
        // consumer.setAvator(avator);
        user.setUpdateTime(new Date());

        boolean res = userService.updateUserMsg(user);
        if (res) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "修改成功");
        } else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "修改失败");
        }
        return jsonObject;
    }

    /**
     * 更新用户头像
     */
    @ResponseBody
    @RequestMapping(value = "/user/avatar/update", method = RequestMethod.POST)
    public Object updateUserPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();

        if (avatarFile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "文件上传失败！");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis() + avatarFile.getOriginalFilename();
        String storeAvatarPath = "/img/avatarImages/" + fileName;
        try {
            avatarFile.transferTo(Constants.AVATAR_IMAGES_PATH.resolve(fileName));
            User user = new User();
            user.setId(id);
            user.setAvatar(storeAvatarPath);
            boolean res = userService.updateUserAvatar(user);
            if (res) {
                jsonObject.put("code", 1);
                jsonObject.put("avatar", storeAvatarPath);
                jsonObject.put("msg", "上传成功");
                return jsonObject;
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "上传失败");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "上传失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }
    
    @PostMapping("/user/record/{userId}/{songId}")
    public void recordListeningHistory(@PathVariable Integer userId, @PathVariable Integer songId) {
        historyService.record(userId, songId);
    }
    
    
}
