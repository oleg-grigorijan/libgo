package com.godev.libgo.web.commons;

import javax.servlet.http.HttpServletRequest;

public interface ServletHandlerRegistry {

    ServletHandlerRegistry register(ServletRoute route);

    ServletHandler get(HttpServletRequest request);
}
