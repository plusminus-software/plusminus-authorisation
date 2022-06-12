package software.plusminus.authorization.service.authorizer.localhost;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.exception.AuthorizationException;
import software.plusminus.authorization.service.authorizer.Authorizer;
import software.plusminus.security.Security;

import javax.servlet.http.HttpServletRequest;

@Component
public class LocalhostOnlyAuthorizer implements Authorizer<LocalhostOnly> {

    @Override
    public Class<LocalhostOnly> annotationType() {
        return LocalhostOnly.class;
    }

    @Override
    public void authorize(HttpServletRequest request, @Nullable Security security,
                          HandlerMethod handlerMethod, LocalhostOnly annotation) {
        String ip = IpUtils.getClientIpAddress(request);
        if (!"localhost".equals(ip) && !"127.0.0.1".equals(ip)) {
            throw new AuthorizationException("Resource is accessible from local network only");
        }
    }

}
