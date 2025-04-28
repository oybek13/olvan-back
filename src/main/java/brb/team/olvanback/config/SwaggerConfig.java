package brb.team.olvanback.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Value("${env.url.name}")
    private String name;
    @Value("${env.url.description}")
    private String description;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer-Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer-Authentication", createAPIKeyScheme()))
                .info(new Info()
                        .title("Olvan Backend")
                        .description("The service was created for schools and educational centers!")
                        .version("1.0")
                        .contact(new Contact()
                                .name("OYBEK KARIMJANOV")
                                .email("oybek.karimjanov13@gmail.com")
                                .url("olvan.uz")))
                .servers(Arrays.asList(
                        new Server().url(name).description(description)));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
