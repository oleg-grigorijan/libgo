package com.godev.libgo.web.commons;

import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ServletHandlerRegistryImpl implements ServletHandlerRegistry {

    private final List<ServletRoute> routes = new ArrayList<>();
    private final ServletHandler notFoundHandler;

    @Override
    public ServletHandlerRegistry register(ServletRoute route) {
        routes.add(route);
        return this;
    }

    @Override
    public ServletHandler get(HttpServletRequest request) {
        return routes.stream()
                .filter(route -> route.matches(request))
                .map(ServletRoute::getHandler)
                .findAny()
                .orElse(notFoundHandler);
    }
}
