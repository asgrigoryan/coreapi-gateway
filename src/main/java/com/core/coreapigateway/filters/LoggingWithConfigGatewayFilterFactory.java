package com.core.coreapigateway.filters;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingWithConfigGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingWithConfigGatewayFilterFactory.Config>
{

	public LoggingWithConfigGatewayFilterFactory()
	{
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config)
	{
		return new OrderedGatewayFilter(((exchange, chain) ->
		{
			log.info("Request from the client {} accepted by gateway.", config.getParam());
			return chain.filter(exchange).doOnError((ex) -> log.error("Error processing request: {}", ex.getMessage())).then(Mono.fromRunnable(() ->
			{
				HttpStatusCode responseStatus = exchange.getResponse().getStatusCode();
				if (responseStatus != null)
				{
					log.info("Response from the service returned to the gateway with status code: {}", responseStatus.value());
				}
				else
				{
					log.warn("Response status code could not be determined");
				}
			}));
		}), -1);
	}

	@Setter
	@Getter
	public static class Config
	{
		private String param;
	}
}
