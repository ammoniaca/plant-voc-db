package org.cnr.plantvocdb.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// http://localhost:8080/swagger-ui/index.html


@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI defineOpenApi() {
//        Server server = new Server();
//        server.setUrl("http://localhost:8080");
//        server.setDescription("Development");
//
//
//        Map<String, Object> contactExtensions = new HashMap<>();
//        contactExtensions.put("additionalContacts", new String[][] {
//                {"Maria Ivagnes", "luca.bianchi@example.com"},
//                {"Francesco Loreto", "giulia.verdi@example.com"}
//        });
//
//
//        Contact myContact = new Contact();
//        myContact.setName("Alessandro Montaghi");
//        myContact.setEmail("alessandro.montaghi@cnr.it");
//        myContact.setUrl("https://www.cnr.it");
//        myContact.extensions(contactExtensions);
//
//        License mitLicense = new License()
//                .name("MIT License")
//                .url("https://choosealicense.com/licenses/mit/");
//
//
//        Info information = new Info()
//                .title("Plant isoprene database")
//                .version("1.0")
//                .description("This API exposes endpoints to manage the plant isoprene database.")
//                .license(mitLicense)
//                .contact(myContact);
//
//        return new OpenAPI().info(information).servers(List.of(server));
        return new OpenAPI()
                .info(new Info()
                        .title("Plant isoprene database")
                        .version("1.0.0")
                        .license(new License()
                                .name("MIT License")
                                .url("https://choosealicense.com/licenses/mit/")
                        )
                        .description("""
                        This API exposes endpoints to manage the plant isoprene database.
                        ### Authors
                        - **Maria Ivagnes** - Institute for Sustainable Plant Protection (IPSP) of CNR, Italy
                        - **Alessandro Montaghi** -  Institute for BioEconomy (IBE) of CNR, Italy
                        - **Silvia Fineschi** - Institute for Sustainable Plant Protection (IPSP) of CNR, Italy
                        - **Francesco Loreto** - University of Naples Federico, Italy 
                        """)
                        .version("1.0")
                        .contact(new Contact()
                                .name("Alessandro Montaghi")
                                .email("alessandro.montaghi@cnr.it")
                                .url("https://www.cnr.it"))
                );
    }

}
