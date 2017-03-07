package org.infinispan.microservices.sessions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.infinispan.commons.api.BasicCache;
import org.infinispan.spring.provider.SpringRemoteCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

@WebServlet("/sessions")
public class SessionsServlet extends HttpServlet {

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      PrintWriter writer = resp.getWriter();
      String sessionId = req.getSession(true).getId();
      resp.getWriter().println("");
      resp.getWriter().println("Number of transactions: " + getCache("transactions").size());
      resp.getWriter().println("Created session: " + sessionId);
      resp.getWriter().println("Number Sessions in cache: " + getCache("sessions").size());
      resp.getWriter().println("Sessions in cache: " + getCache("sessions").keySet());
      writer.close();
   }

   private BasicCache getCache(String cacheName) {
      ApplicationContext ac = (ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
      SpringRemoteCacheManager cacheManager = ac.getBean(SpringRemoteCacheManager.class);
      return cacheManager.getCache(cacheName).getNativeCache();
   }
}
