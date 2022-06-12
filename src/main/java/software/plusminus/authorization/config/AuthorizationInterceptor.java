package software.plusminus.authorization.config;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import software.plusminus.authorization.service.AuthorizationService;
import software.plusminus.security.Security;
import software.plusminus.security.SecurityRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private AuthorizationService service;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Security security = getSecurity(request);
        HandlerMethod handlerMethod = getHandlerMethod(handler);
        
        service.authorize(request, security, handlerMethod);
        return true;
    }
    
    @Nullable
    private Security getSecurity(HttpServletRequest request) {
        if (request instanceof SecurityRequest) {
            return ((SecurityRequest) request).getSecurity();
        } else {
            return null;
        }
    }
    
    private HandlerMethod getHandlerMethod(Object handler) {
        if (handler instanceof HandlerMethod) {
            return (HandlerMethod) handler;
        } else {
            throw new IllegalStateException("Expected " + handler.getClass() + " to be an instance of HandlerMethod");
        }
    }

}
