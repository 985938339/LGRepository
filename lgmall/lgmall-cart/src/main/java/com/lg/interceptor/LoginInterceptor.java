package com.lg.interceptor;


import com.lg.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();

    public static Long getMemberId() {
        Map<String,String> map=threadLocal.get();
        String id=null;
        if (!ObjectUtils.isEmpty(map)){
            id=map.get("memberId");
        }
       if (id==null){
           return 1L;
       }
       return  Long.parseLong(id) ;
    }
    public static String getMemberName() {
        Map<String,String> map=threadLocal.get();
        String name=null;
        if (!ObjectUtils.isEmpty(map)){
            name=map.get("name");
        }
        if (name==null){
            return null;
        }
        return name;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        String uri = request.getRequestURI();
//        AntPathMatcher antPathMatcher = new AntPathMatcher();
//        boolean match = antPathMatcher.match("/order/order/status/**", uri);
//        boolean match1 = antPathMatcher.match("/payed/notify", uri);
//        if (match || match1) {
//            return true;
//        }

        //获取登录的用户信息
        HttpSession session=request.getSession();
        String token=null;
        if (session!=null){
            token = (String) session.getAttribute("token");
        }
        if (token != null) {
            //把登录后用户的信息放在ThreadLocal里面进行保存
            Map<String, String> map = JwtUtils.GetMemberTokenInfo(token);
            threadLocal.set(map);
            return true;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
