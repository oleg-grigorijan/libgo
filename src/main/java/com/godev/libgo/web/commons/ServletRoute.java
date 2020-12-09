package com.godev.libgo.web.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@AllArgsConstructor
public class ServletRoute {

    private final RouteMethod method;
    private final String pathPattern;
    private final List<String> queryParams;

    @Getter
    private final ServletHandler handler;

    public boolean matches(HttpServletRequest request) {
        return matchesMethod(request)
                && matchesPathPattern(request)
                && matchesQueryParams(request);
    }

    private boolean matchesMethod(HttpServletRequest request) {
        return method == RouteMethod.valueOf(request.getMethod());
    }

    private boolean matchesPathPattern(HttpServletRequest request) {
        // TODO: More complex patterns with path variables
        return request.getRequestURI().equals(pathPattern);
    }

    private boolean matchesQueryParams(HttpServletRequest request) {
        return queryParams.stream().allMatch(requiredParam -> request.getParameter(requiredParam) != null);
    }
}
