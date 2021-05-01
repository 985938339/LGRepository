package com.lg.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//JWT工具类
public class JwtUtils {
    /*
     *这个token的随机盐
     */
    private static final String SIGN ="uefhy!@$%^HG";

    /*
     *传入payload的键值对,生成token字符串
     */
    public static String GetMemberToken(String memberId, String name){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,10);//设置过期时间为10分钟
        JWTCreator.Builder token= JWT.create();
        return token.withClaim("memberId",memberId)
                .withClaim("name",name)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SIGN));
    }

    /*
     *token验证,验证失败的话会抛出异常
     */
    public static void Verify(String token){
        JWT.require(Algorithm.HMAC256(SIGN)).build();
    }

    /*
     *获取Token信息，信息全都存在DecodedJWT里面
     */
    public static Map<String,String> GetMemberTokenInfo(String token){
        Map<String,String> map=new HashMap<>();
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        map.put("memberId",decodedJWT.getClaim("memberId").asString());
        map.put("name",decodedJWT.getClaim("name").asString());
        return map;
    }
    public static String getToken(Map<String,String> claims,Integer expireSeconds){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.SECOND,expireSeconds);
        JWTCreator.Builder token= JWT.create();
        for (Map.Entry<String,String> entry:claims.entrySet()){
            token.withClaim(entry.getKey(),entry.getValue());
        }
        return token.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SIGN));
    }
}
