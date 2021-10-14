package com.waracle.cake_manager;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * https://stackoverflow.com/questions/30896234/how-set-up-spring-boot-to-run-https-http-ports
 * <p>
 * https://medium.com/javarevisited/how-to-enable-https-locally-without-getting-annoying-browser-privacyerrors-in-a-spring-boot-a6137dacbc0a
 * or look at...
 * https://github.com/sophea/ssl-spring-boot
 */
@Component
public class HttpServer {

    @Bean
    public ServletWebServerFactory servletContainer(@Value("${server.http.port}") int httpPort) {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(httpPort);

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(connector);

        return tomcat;
    }
}
