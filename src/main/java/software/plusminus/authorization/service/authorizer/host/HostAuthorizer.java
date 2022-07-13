package software.plusminus.authorization.service.authorizer.host;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.exception.AuthorizationException;
import software.plusminus.authorization.service.authorizer.Authorizer;
import software.plusminus.authorization.utils.AuthorizationUtils;
import software.plusminus.security.SecurityRequest;

import javax.servlet.http.HttpServletRequest;

@Component
public class HostAuthorizer implements Authorizer {

    @Override
    public void authorize(HttpServletRequest request, HandlerMethod handlerMethod) {
        SecurityRequest securityRequest = AuthorizationUtils.toNullableSecurityRequest(request);
        if (securityRequest == null) {
            return;
        }
        
        String allowedHost = securityRequest.getSecurity().getOthers().get("host");
        if (allowedHost == null) {
            return;
        }
        
        String currentHost = AuthorizationUtils.getHost(request);
        if (!allowedHost.equals(currentHost)) { 
            throw new AuthorizationException("The current host '" + currentHost 
                    + "' does not equal to host '" + allowedHost + "' from token");
        }
    }
}
