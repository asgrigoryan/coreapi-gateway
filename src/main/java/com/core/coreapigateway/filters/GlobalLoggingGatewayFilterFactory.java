package com.core.coreapigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalLoggingGatewayFilterFactory implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Request from the client accepted by gateway.");
        return chain.filter(exchange).then(
                Mono.fromRunnable(()->log.info("Response from the service returned to the gateway"))
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
