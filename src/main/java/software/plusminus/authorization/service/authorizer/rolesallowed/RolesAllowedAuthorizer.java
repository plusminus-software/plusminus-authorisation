package software.plusminus.authorization.service.authorizer.rolesallowed;

import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.exception.AuthorizationException;
import software.plusminus.authorization.service.authorizer.AnnotationAuthorizer;

import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

public class RolesAllowedAuthorizer implements AnnotationAuthorizer<RolesAllowed> {
    
    @Override
    public Class<RolesAllowed> annotationType() {
        return RolesAllowed.class;
    }

    @Override
    public void authorize(HttpServletRequest request, HandlerMethod handlerMethod, RolesAllowed annotation) {
        List<String> declaredRoles = Arrays.asList(annotation.value());
        boolean rolePresent = declaredRoles.stream().anyMatch(request::isUserInRole);
        if (!rolePresent) {
            throw new AuthorizationException("User '" + request.getRemoteUser()
                    + "' does not have at least one of the required roles: " + declaredRoles);
        }
    }
}
