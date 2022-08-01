package com.huifu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huifu.reggie.common.R;
import com.huifu.reggie.entity.User;
import com.huifu.reggie.service.UserService;
import com.huifu.reggie.utils.SMSUtils;
import com.huifu.reggie.utils.ValidateCodeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        //生成随机4位验证吗
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云服务API完成
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);
            //保存验证吗到阿里云
            session.setAttribute(phone, code);
            return R.success("发送成功");
        }


        return R.success("短信发送失败");
    }

    @PostMapping("/login1")
    public R<User> login1(@RequestBody Map map, HttpSession session) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        String codeInSession = (String) session.getAttribute(phone);
        if (codeInSession != null && codeInSession.equals(code)) {
            //对比成功，需要判断是否是新用户
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("短信验证失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = (String) map.get("phone");
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(userLambdaQueryWrapper);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user",user.getId());
        return R.success(user);
    }
}
