package com.lg.config;

import com.lg.InvalidTokenException;
import com.lg.utils.MemberInfoUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 鉴权使用的Filter
 */
@Component
public class AccessFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            String currentMemberId = MemberInfoUtil.getCurrentMemberId();
            //todo 继续在这里判断和鉴权,如果鉴权失败了，也会返回earlyResponse;
        } catch (InvalidTokenException e) {
            return earlyResponse(e.getMessage(), exchange);
        }
        //返回这个chain.filter会使得继续执行下面的filter
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> earlyResponse(String message, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json;charset=utf-8");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String message2 = "{\"message\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(message2.getBytes());
        //返回这个，就不会再执行filter了，就是真的返回了
        return response.writeWith(Mono.just(buffer));
    }
}
