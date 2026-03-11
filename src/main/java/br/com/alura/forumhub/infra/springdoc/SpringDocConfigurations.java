package br.com.alura.forumhub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfigurations {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ForumHub API")
                        .description("API REST do projeto ForumHub, contendo as funcionalidades de tópicos, respostas e usuários.")
                        .version("v1"))
                .tags(List.of(
                        new Tag().name("1. API Status").description("Endpoints para monitoramento e verificação de integridade da API"),
                        new Tag().name("2. Autenticação").description("Operações relacionadas ao processo de login e geração de tokens JWT."),
                        new Tag().name("3. Usuários").description("Gestão de cadastros de usuários e listagem de membros do fórum."),
                        new Tag().name("4. Tópicos").description("Criação, listagem, detalhamento, atualização e exclusão de tópicos."),
                        new Tag().name("5. Respostas").description("Gestão de respostas e sistema de marcação de solução em tópicos.")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}