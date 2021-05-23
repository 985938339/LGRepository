package com.lg.utils;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.lg.InvalidTokenException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class MemberInfoUtil {
    private static final String AUTH = "auth";

    public static String getCurrentMemberId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            String token = requestAttributes.getRequest().getHeader(AUTH);
            if (token != null) {
                String memberId = null;
                try {
                    memberId = JwtUtils.GetMemberId(token);
                } catch (TokenExpiredException e) {
                    throw new InvalidTokenException("Token已经过期，请重新登录");
                } catch (AlgorithmMismatchException e) {
                    throw new InvalidTokenException("Token算法不一致，请重新登录");
                } catch (SignatureVerificationException e) {
                    throw new InvalidTokenException("无效的签名，请重新登录");
                } catch (Exception e) {
                    throw new InvalidTokenException("无效的Token，请重新登录");
                }
                return memberId;
            } else {
                throw new InvalidTokenException("无效Token，请重新登录");
            }
        } else {
            throw new InvalidTokenException("无效Token，请重新登录");
        }
    }

    public static String getCurrentMemberName() throws InvalidTokenException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            String token = requestAttributes.getRequest().getHeader(AUTH);
            if (token != null) {
                String memberName = null;
                try {
                    memberName = JwtUtils.GetMemberName(token);
                } catch (Exception e) {
                    throw new InvalidTokenException("无效Token，请重新登录");
                }
                return memberName;
            } else {
                throw new InvalidTokenException("无效Token，请重新登录");
            }
        } else {
            throw new InvalidTokenException("无效Token，请重新登录");
        }
    }
}
