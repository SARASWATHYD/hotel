package com.hotel.endpoint;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Created by saraswathy
on 2020-08-15 17:16

 */

@WebServlet(urlPatterns = { "/test" })
public class BaseEndPoint  extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/plain");
            response.getWriter().println("demo demo");
        }catch (Exception e){
            log(e.getMessage());
        }

    }
}
