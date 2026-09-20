package com.pacal.share.auto_config;

import com.pacal.share.config.CORSConfig;
import com.pacal.share.context.PacalRequestListener;
import jakarta.servlet.ServletRequestListener;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;


@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebApplicationInitializer.class)
@AutoConfigureBefore(MessageSourceAutoConfiguration.class)
@Import({CORSConfig.class})
public class PacalAutoConfiguration {

    @Bean
    @Order(-1)
    public ServletListenerRegistrationBean<ServletRequestListener> registerRequestListener() {
        ServletListenerRegistrationBean<ServletRequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(new PacalRequestListener());
        return servletListenerRegistrationBean;
    }
}
