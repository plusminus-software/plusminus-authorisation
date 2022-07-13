package software.plusminus.authorization.service.authorizer;

import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

public interface Authorizer {
    
    void authorize(HttpServletRequest request, HandlerMethod handlerMethod);

}
