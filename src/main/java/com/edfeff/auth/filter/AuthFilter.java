package com.edfeff.auth.filter;


import com.edfeff.auth.user.CurrentUserHolder;
import com.edfeff.auth.user.User;
import com.edfeff.auth.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthFilter extends OncePerRequestFilter {
    private UserService userService;
    private WhiteRequestAllow whiteRequestAllow;
    private AuthKeyLoader authKeyLoader;
    private NoAuthKeyHandler noAuthKeyHandler;

    public AuthFilter() {
        this.whiteRequestAllow = new DefaultWhiteRequestAllow(Arrays.asList("/login"));
        this.authKeyLoader = new DefaultAuthKeyLoader("token", true, true, true);
        this.noAuthKeyHandler = new DefaultNoAuthKeyHandler();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (whiteRequestAllow.allow(request, response)) {
            filterChain.doFilter(request, response);
            return;
        }
        String key = authKeyLoader.loadAuthKey(request);
        if (key == null) {
            noAuthKeyHandler.handleNoAuthKeyRequest(request, response);
            return;
        }

        User user = userService.getFromToken(key);
        if (user == null) {
            noAuthKeyHandler.handleAuthKeyInvalidRequest(request, response);
            return;
        }
        CurrentUserHolder.setUser(user);
        filterChain.doFilter(request, response);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAuthKeyLoader(AuthKeyLoader authKeyLoader) {
        this.authKeyLoader = authKeyLoader;
    }

    public void setNoAuthKeyHandler(NoAuthKeyHandler noAuthKeyHandler) {
        this.noAuthKeyHandler = noAuthKeyHandler;
    }

    public void setWhiteRequestAllow(WhiteRequestAllow whiteRequestAllow) {
        this.whiteRequestAllow = whiteRequestAllow;
    }

    public interface AuthKeyLoader {
        String loadAuthKey(HttpServletRequest request);
    }

    public interface NoAuthKeyHandler {
        public void handleNoAuthKeyRequest(HttpServletRequest request, HttpServletResponse response);

        public void handleAuthKeyInvalidRequest(HttpServletRequest request, HttpServletResponse response);
    }

    public interface WhiteRequestAllow {
        boolean allow(HttpServletRequest request, HttpServletResponse response);
    }

    public static class DefaultWhiteRequestAllow implements WhiteRequestAllow {
        private List<String> whitePaths;
        private AntPathMatcher pathMatcher;

        public DefaultWhiteRequestAllow(List<String> whitePaths) {
            this.whitePaths = whitePaths;
            this.pathMatcher = new AntPathMatcher();
        }

        @Override
        public boolean allow(HttpServletRequest request, HttpServletResponse response) {
            String requestURI = request.getRequestURI();
            for (String path : whitePaths) {
                if (pathMatcher.match(path, requestURI)) {
                    return true;
                }
            }
            return false;
        }
    }


    public static class DefaultAuthKeyLoader implements AuthKeyLoader {
        private boolean loadAuthKeyFromParam = true;
        private boolean loadAuthKeyFromHeader = true;
        private boolean loadAuthKeyFromCookie = true;
        private String authKey;

        public DefaultAuthKeyLoader(String authKey, boolean loadAuthKeyFromParam, boolean loadAuthKeyFromHeader, boolean loadAuthKeyFromCookie) {
            this.authKey = authKey;
            this.loadAuthKeyFromParam = loadAuthKeyFromParam;
            this.loadAuthKeyFromHeader = loadAuthKeyFromHeader;
            this.loadAuthKeyFromCookie = loadAuthKeyFromCookie;
        }

        @Override
        public String loadAuthKey(HttpServletRequest request) {
            String key = null;
            if (loadAuthKeyFromParam) {
                key = request.getParameter(authKey);
            }
            if (key == null && loadAuthKeyFromHeader) {
                key = request.getHeader(authKey);
            }
            if (key == null && loadAuthKeyFromCookie && request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(authKey)) {
                        key = cookie.getValue();
                    }
                }
            }
            return key;
        }
    }


    static class DefaultNoAuthKeyHandler implements NoAuthKeyHandler {
        @Override
        public void handleNoAuthKeyRequest(HttpServletRequest request, HttpServletResponse response) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            try {
                response.getWriter().println("No Auth Key Found.");
                response.getWriter().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void handleAuthKeyInvalidRequest(HttpServletRequest request, HttpServletResponse response) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            try {
                response.getWriter().println("Invalid Auth Key.");
                response.getWriter().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
