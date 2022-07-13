package software.plusminus.authorization.service.authorizer;

import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.utils.AuthorizationUtils;
import software.plusminus.security.SecurityRequest;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticatedAuthorizer extends Authorizer {

    @Override
    default void authorize(HttpServletRequest request, HandlerMethod handlerMethod) {
        SecurityRequest securityRequest = AuthorizationUtils.toSecurityRequest(request);
        authorize(securityRequest, handlerMethod);
    }

    void authorize(SecurityRequest request, HandlerMethod handlerMethod);

}
