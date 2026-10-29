package com.labattendance.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class ActiveUserListener implements HttpSessionListener {

    private static final AtomicInteger activeUsers = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = activeUsers.incrementAndGet();
        ServletContext context = se.getSession().getServletContext();
        context.setAttribute("activeUsersCount", count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = activeUsers.decrementAndGet();
        if (count < 0) {
            count = 0;
            activeUsers.set(0);
        }
        ServletContext context = se.getSession().getServletContext();
        context.setAttribute("activeUsersCount", count);
    }
}
