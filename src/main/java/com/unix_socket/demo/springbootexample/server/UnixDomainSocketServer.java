package com.unix_socket.demo.springbootexample.server;

import com.unix_socket.demo.springbootexample.server.receiver.MessageReceiverNolib;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class UnixDomainSocketServer extends SpringBootServletInitializer {

    @SuppressWarnings("resource")
    public static void main(final String[] args) throws Exception {
        SpringApplication application = new SpringApplication(UnixDomainSocketServer.class);
        application.setDefaultProperties(Collections
                .singletonMap("server.port", "5555"));
        ConfigurableApplicationContext applicationContext = application.run(args);
        applicationContext.getBean(MessageReceiverNolib.class).receive();

        //Uncomment to use receiver with library
//        context.getBean(MessageReceiver.class).receive();
    }

/*       @SuppressWarnings("resource")
    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationServer.class, args);
        context.getBean(MessageReceiverNolib.class).receive();
        //Uncomment to use receiver with library
//        context.getBean(MessageReceiver.class).receive();
    }*/




   /* @Bean
    @Primary
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return new UnixDomainSocketTomcatCustomizer();
    }

    private static class UnixDomainSocketTomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            Connector connector = new Connector();
            connector.setPort(0);
            connector.setScheme("http");
            connector.setProperty("socketFactoryClassName", "org.apache.tomcat.jni.SocketFactory");
            connector.setProperty("socketFactoryEncoding", "UTF-8");
            factory.addAdditionalTomcatConnectors(connector);
        }
    }*/
}
