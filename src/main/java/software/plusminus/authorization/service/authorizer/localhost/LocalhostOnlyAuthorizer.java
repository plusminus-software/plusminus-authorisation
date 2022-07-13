package software.plusminus.authorization.service.authorizer.localhost;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.exception.AuthorizationException;
import software.plusminus.authorization.service.authorizer.AnnotationAuthorizer;

import javax.servlet.http.HttpServletRequest;

@Component
public class LocalhostOnlyAuthorizer implements AnnotationAuthorizer<LocalhostOnly> {

    @Override
    public Class<LocalhostOnly> annotationType() {
        return LocalhostOnly.class;
    }

    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    @Override
    public void authorize(HttpServletRequest request, HandlerMethod handlerMethod, LocalhostOnly annotation) {
        String ip = IpUtils.getClientIpAddress(request);
        if (!"localhost".equals(ip) && !"127.0.0.1".equals(ip)) {
            throw new AuthorizationException("Resource is accessible from local network only");
        }
    }
}
