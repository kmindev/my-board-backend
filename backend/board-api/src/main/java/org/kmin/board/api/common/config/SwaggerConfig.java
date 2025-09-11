package org.kmin.board.api.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("게시판 API 서버")
                .description("게시판 API 문서입니다.")
                .version("0.0.1-SNAPSHOT");

        SecurityScheme basicAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("JSESSIONID")
                .description("JSESSIONID 세션 쿠키를 통한 인증");
        SecurityRequirement basicsecurityRequirement = new SecurityRequirement().addList("basicAuth");

        return new OpenAPI()
                .info(info)
                .components(new Components()
                        .addSecuritySchemes("basicAuth", basicAuthScheme)
                )
                .addSecurityItem(basicsecurityRequirement);
    }

}
