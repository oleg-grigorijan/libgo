package com.godev.libgo.web.commons;

import lombok.RequiredArgsConstructor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO: Use ServletContextListener to inject RoutesRegistry
// @WebServlet(urlPatterns = "/*")
@RequiredArgsConstructor
public class DispatcherServlet extends HttpServlet {

    private final ServletHandlerRegistry handlers;

    private void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handlers.get(request)
                .handle(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handle(request, response);
    }
}
