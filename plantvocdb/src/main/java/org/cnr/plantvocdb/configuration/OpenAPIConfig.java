package org.cnr.plantvocdb.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
// http://localhost:8080/swagger-ui/index.html


@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Alessandro Montaghi");
        myContact.setEmail("alessandro.montaghi@cnr.it");
        myContact.setUrl("https://www.cnr.it");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");


        Info information = new Info()
                .title("Plants VOC (Volatile Organic Compounds) database")
                .version("1.0")
                .description("This API exposes endpoints to manage Plants VOC database.")
                .license(mitLicense)
                .contact(myContact);

        return new OpenAPI().info(information).servers(List.of(server));
    }

}
