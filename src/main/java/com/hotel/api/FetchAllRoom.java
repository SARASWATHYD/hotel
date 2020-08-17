package com.hotel.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Created by saraswathy
on 2020-08-16 16:00 */

@WebServlet(urlPatterns = { "/fetch/room" })
public class FetchAllRoom extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setContentType("text/plain");
            response.getWriter().println("demo demo");
        }catch (Exception e){
            log(e.getMessage());
        }

    }

}
