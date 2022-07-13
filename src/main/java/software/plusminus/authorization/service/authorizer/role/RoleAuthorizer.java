package software.plusminus.authorization.service.authorizer.role;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.exception.AuthorizationException;
import software.plusminus.authorization.service.authorizer.AnnotationAuthorizer;
import software.plusminus.authorization.utils.AuthorizationUtils;
import software.plusminus.security.SecurityRequest;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;

@Component
public class RoleAuthorizer implements AnnotationAuthorizer<Role> {

    @Override
    public Class<Role> annotationType() {
        return Role.class;
    }

    @Override
    public void authorize(HttpServletRequest request, HandlerMethod handlerMethod, Role role) {
        SecurityRequest securityRequest = AuthorizationUtils.toSecurityRequest(request);
        Set<String> roles = securityRequest.getSecurity().getRoles().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        
        boolean containsAnyRole = Stream.of(role.value())
                .anyMatch(r -> roles.contains(r.toLowerCase()));
        if (!containsAnyRole) {
            throw new AuthorizationException("User '" + securityRequest.getRemoteUser()
                    + "' does not have at least one of the required roles: " + Arrays.toString(role.value()));
        }
    }
}
