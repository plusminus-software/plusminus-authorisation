package software.plusminus.authorization.service.authorizer.nonpublic;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.exception.NonPublicException;
import software.plusminus.authorization.service.authorizer.Authorizer;
import software.plusminus.security.Security;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Order(0)
@Component
public class PublicApiAuthorizer implements Authorizer<Public> {
    
    private static final List<Class<?>> PUBLIC_CONTROLLERS
            = Arrays.asList(ErrorController.class);

    @Override
    public Class<Public> annotationType() {
        return Public.class;
    }

    @Override
    public boolean callEvenIfAnnotationIsMissed() {
        return true;
    }

    @Override
    public void authorize(HttpServletRequest request, @Nullable Security security,
                          HandlerMethod handlerMethod, @Nullable Public annotation) {
        if (isPublicController(handlerMethod)) {
            return;
        }
        if (security == null && annotation == null) {
            throw new NonPublicException();
        }
    }
    
    private boolean isPublicController(HandlerMethod handlerMethod) {
        return PUBLIC_CONTROLLERS.stream()
                .anyMatch(p -> p.isAssignableFrom(handlerMethod.getBeanType()));
    }
}
