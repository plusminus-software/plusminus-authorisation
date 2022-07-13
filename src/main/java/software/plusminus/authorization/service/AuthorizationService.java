package software.plusminus.authorization.service;

import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.service.authorizer.Authorizer;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Service
public class AuthorizationService {

    private List<Authorizer> authorizers;
    
    public void authorize(HttpServletRequest request, HandlerMethod handlerMethod) {
        authorizers.forEach(a -> a.authorize(request, handlerMethod));
    }
}
