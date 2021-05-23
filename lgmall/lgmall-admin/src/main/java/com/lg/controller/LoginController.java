package com.lg.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.lg.interceptor.LoginInterceptor;
import com.lg.entity.UmsMember;
import com.lg.service.UmsMemberService;
import com.lg.utils.MemberInfoUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class LoginController {
    private final String AUTH="auth";
    @Resource
    public UmsMemberService umsMemberService;


    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R<String> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        if (MemberInfoUtil.getCurrentMemberName() !=null){
            return R.ok("已经登录成功");
        }
        UmsMember member=new UmsMember();
        member.setUsername(username);
        member.setPassword(password);
        String token= umsMemberService.login(member);
        //todo 添加权限token到header中，带验证
        response.addHeader(AUTH,token);
        return R.ok("登录成功");
    }

    @ApiOperation(value = "注册账号")
    @PostMapping("/register")
    public R<String> register(@RequestParam String username,@RequestParam String password) {
        UmsMember member=new UmsMember();
        member.setUsername(username);
        member.setPassword(password);
        umsMemberService.register(member);
        return R.ok("注册成功");
    }
}
