package org.jetblue.jetblue.Config.Filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

//
//@Component
//@Order(1)
public class corsFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(corsFilter.class);



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.error("We logg the info from the filter...");
        log.error("We logg the info from the cors filter...");
        log.error("We logg the info heated by the pattern /specification/*...");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
