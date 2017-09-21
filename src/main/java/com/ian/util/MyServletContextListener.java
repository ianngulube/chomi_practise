package com.ian.util;

import com.ian.service.UssdSessionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MyServletContextListener implements ServletContextListener {

    @Autowired
    UssdSessionService ussdSessionService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        //This is how you initialize a bean inside the ServletContextListener
        WebApplicationContext servletContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        ussdSessionService = (UssdSessionService) servletContext.getBean("ussdSessionService");

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    ussdSessionService.deleteOldUssdSessions();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }, 0, 45, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("The servlet context has been destroyed");
    }

}
