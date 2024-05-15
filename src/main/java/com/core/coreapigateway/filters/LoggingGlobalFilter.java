package com.core.coreapigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Request from the client accepted by gateway: {}", exchange.getRequest().getURI());

        return chain.filter(exchange)
                .doOnError((ex) -> log.error("Error processing request: {}", ex.getMessage()))
                .then(Mono.fromRunnable(() -> {
                    HttpStatusCode responseStatus = exchange.getResponse().getStatusCode();
                    if (responseStatus != null) {
                        log.info("Response from the service returned to the gateway with status code: {}", responseStatus.value());
                    } else {
                        log.warn("Response status code could not be determined");
                    }
                }));
    }

    @Override
    public int getOrder() {
        return -1; // Adjust this value based on when you want the filter to execute relative to others
    }
}
