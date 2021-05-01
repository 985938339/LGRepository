package com.lg.interceptor;


import com.lg.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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
            return "liuguo";
        }
        return name;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //这里库存服务会在rabbitmq的消费者里feign调用查询是否存在订单，因此这个请求是不带用户信息的，直接放行
        boolean match = antPathMatcher.match("/order/omsOrder/exist", uri);
        if (match) {
            return true;
        }

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
