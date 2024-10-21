package io.squad.apisec.config;

import io.squad.apisec.filter.MdcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class ApplConf {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> mdcFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<OncePerRequestFilter>();
        registrationBean.setFilter(new MdcFilter());
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
