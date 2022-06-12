package software.plusminus.authorization.service.authorizer;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.security.Security;

import java.lang.annotation.Annotation;
import javax.servlet.http.HttpServletRequest;

public interface Authorizer<A extends Annotation> {
    
    Class<A> annotationType();
    
    default boolean callEvenIfAnnotationIsMissed() {
        return false;
    }

    void authorize(HttpServletRequest request, @Nullable Security security,
                   HandlerMethod handlerMethod, A annotation);

}
