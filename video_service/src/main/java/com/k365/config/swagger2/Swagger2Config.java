package com.k365.config.swagger2;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "custom-attr", name = "swagger2-ui-switch", havingValue = "true")
public class Swagger2Config {

    @Bean
    public Docket shopRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.k365"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();

    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Video Box API文档")
                .description("用户API")
                .termsOfServiceUrl("http://localhost:8081/videomanager/swagger-ui.html")
                .contact(new Contact("Gavin", "www.365k.com", "gavin365k@outloot.com"))
                .version("1.0")
                .build();

    }

}
