package software.plusminus.authorization.service.authorizer;

import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.utils.AuthorizationUtils;

import java.lang.annotation.Annotation;
import javax.servlet.http.HttpServletRequest;

public interface AnnotationAuthorizer<A extends Annotation> extends Authorizer {
    
    Class<A> annotationType();
    
    @Override
    default void authorize(HttpServletRequest request, HandlerMethod handlerMethod) {
        A annotation = AuthorizationUtils.findAnnotation(handlerMethod, annotationType());
        if (annotation != null) {
            authorize(request, handlerMethod, annotation);
        }
    }

    void authorize(HttpServletRequest request, HandlerMethod handlerMethod, A annotation);

}
