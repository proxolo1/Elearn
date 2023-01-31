package com.learn.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-learn Application",
                description = "",
                version = "1.0",
//                termsOfServiceUrl = "https://example.com/tos",
                contact = @Contact(
                        name = "Ajay k santhosh",
                        email = "ajayksanthosh.15@gmail.com",
                        url = "https://proxolo.tk"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = @Server(
                url = "http://localhost:8080/",
                description = "Production Server"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
public class SwaggerConfig {

}
