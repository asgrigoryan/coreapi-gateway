package com.core.coreapigateway.filters;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingWithConfigGatewayFilterFactor extends AbstractGatewayFilterFactory<LoggingWithConfigGatewayFilterFactor.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter(((exchange, chain) -> {
            log.info("Request from the client {} accepted by gateway.", config.getName());
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(()->log.info("Response from the service returned to the gateway.")));
        }), -1);
    }

    @Setter
    @Getter
    public static class Config{
        private String name;
    }
}
