package org.typhon.apigateway;
	
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
	
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
	
@Configuration
@SpringBootApplication
//@EnableSwagger2
public class ApigatewayApplication {

	@Bean
	RouteLocator gatewayArtifactServiceRouter(RouteLocatorBuilder builder){
        return builder.routes()
				.route(r -> r.path("/app/**").and().method("GET")
                        .filters(f-> f.rewritePath("/app","/app"))
                        .uri("http://localhost:8091"))
                .route(r -> r.path("/app").and().method("POST")
                        .filters(f-> f.rewritePath("/app","/app"))
                        .uri("http://localhost:8091"))
                .route(r -> r.path("/app/**").and().method("PUT")
                        .filters(f-> f.rewritePath("/app","/app"))
                        .uri("http://localhost:8091"))
                .route(r -> r.path("/app/**").and().method("DELETE")
                        .filters(f-> f.rewritePath("/app","/app"))
                        .uri("http://localhost:8091"))

				.route(r -> r.path("/textwarning/**").and().method("GET")
                        .filters(f-> f.rewritePath("/textwarning","/textwarning"))
                        .uri("http://localhost:8092"))
                .route(r -> r.path("/textwarning").and().method("POST")
                        .filters(f-> f.rewritePath("/textwarning","/textwarning"))
                        .uri("http://localhost:8092"))
                .route(r -> r.path("/textwarning/**").and().method("PUT")
                        .filters(f-> f.rewritePath("/textwarning","/textwarning"))
                        .uri("http://localhost:8092"))
                .route(r -> r.path("/textwarning/**").and().method("DELETE")
                        .filters(f-> f.rewritePath("/textwarning","/textwarning"))
                        .uri("http://localhost:8092"))

				.route(r -> r.path("/warning/**").and().method("GET")
                        .filters(f-> f.rewritePath("/warning","/warning"))
                        .uri("http://localhost:8093"))
                .route(r -> r.path("/warning").and().method("POST")
                        .filters(f-> f.rewritePath("/warning","/warning"))
                        .uri("http://localhost:8093"))
                .route(r -> r.path("/warning/**").and().method("PUT")
                        .filters(f-> f.rewritePath("/warning","/warning"))
                        .uri("http://localhost:8093"))
                .route(r -> r.path("/warning/**").and().method("DELETE")
                        .filters(f-> f.rewritePath("/warning","/warning"))
                        .uri("http://localhost:8093"))
				.build();
    }
		
	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}
}
