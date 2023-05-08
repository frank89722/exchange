package me.frankv.exchange.api.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

//    @Bean
//    public ConfigurableServletWebServerFactory webServerFactory() {
//        var factory = new JettyServletWebServerFactory();
//        var customizer = (JettyServerCustomizer) server -> {
//            for (Connector connector : server.getConnectors()) {
//                if (connector instanceof ServerConnector serverConnector) {
//                    var serverConnectorFactory = new HTTP3ServerConnectionFactory(new HttpConfiguration());
//                    serverConnector.addConnectionFactory(serverConnectorFactory);
//                }
//            }
//        };
//
//        factory.addServerCustomizers(customizer);
//        factory.setThreadPool(new ExecutorThreadPool());
//        return factory;
//    }


}
