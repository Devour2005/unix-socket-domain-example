package com.unix_socket.demo.springbootexample.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class UnixDomainSocketClient extends SpringBootServletInitializer {

    @SuppressWarnings("resource")
    public static void main(final String[] args) {
        SpringApplication application = new SpringApplication(UnixDomainSocketClient.class);
        application.setDefaultProperties(Collections
                .singletonMap("server.port", "5556"));
        application.run(args);
    }

    /**
     * A configuration for testing unit-socket without REST
     *
     */
  /*  @SuppressWarnings("resource")
    public static void main(final String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(UnixDomainSocketClient.class, args);
        context.getBean(MessageSenderServiceNolib.class).connectAndSendMessage();
        //Uncomment to use receiver with library
        context.getBean(MessageSenderService.class).receive();
    }*/

    @Bean(name = "unixRestTemplate")
    @Primary
    public RestTemplate unixRestTemplate() {
        return new RestTemplate();
    }
}
