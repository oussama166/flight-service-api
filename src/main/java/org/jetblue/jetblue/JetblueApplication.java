package org.jetblue.jetblue;

import org.jetblue.jetblue.Config.Filter.corsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JetblueApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetblueApplication.class, args);
    }


    // Register new bean pattern

    /*
    * This function is for make filter with specific route
    * */
    /**
     * Register specific pattern filter
     * @return FilterRegistrationBean
     * */
    @Bean
    FilterRegistrationBean<corsFilter> corsFilterRegistration() {
        FilterRegistrationBean<corsFilter> bean = new FilterRegistrationBean<>(new corsFilter());
        bean.setFilter(new corsFilter());
        bean.addUrlPatterns("/specification/*");

        return bean;
    }

}
