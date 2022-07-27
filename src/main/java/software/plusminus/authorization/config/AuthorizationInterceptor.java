package software.plusminus.authorization.config;

import lombok.AllArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import software.plusminus.authorization.service.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private AuthorizationService service;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        HandlerMethod handlerMethod = getHandlerMethod(handler);
        service.authorize(request, handlerMethod);
        return true;
    }
    
    private HandlerMethod getHandlerMethod(Object handler) {
        if (handler instanceof HandlerMethod) {
            return (HandlerMethod) handler;
        } else {
            throw new IllegalStateException("Expected " + handler.getClass() + " to be an instance of HandlerMethod");
        }
    }

}
