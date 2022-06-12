package software.plusminus.authorization.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.annotation.AuthorizationAnnotationContext;
import software.plusminus.authorization.service.authorizer.Authorizer;
import software.plusminus.security.Security;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Service
public class AuthorizationService {

    private List<Authorizer<? extends Annotation>> authorizers;
    private AuthorizationAnnotationContext annotationContext;
    

    public void authorize(HttpServletRequest request, @Nullable Security security,
                          HandlerMethod handlerMethod) {
        Map<Class<? extends Annotation>, Annotation> annotations = annotationContext.getAnnotations(handlerMethod);
        authorizers.stream()
                .filter(authorizer -> annotations.containsKey(authorizer.annotationType()) 
                        || authorizer.callEvenIfAnnotationIsMissed())
                .forEach(authorizer -> call(request, security, handlerMethod, authorizer, annotations));
    }
    
    private <A extends Annotation> void call(HttpServletRequest request, @Nullable Security security, 
                                             HandlerMethod handlerMethod, Authorizer<A> authorizer,
                                             Map<Class<? extends Annotation>, Annotation> annotations) {
        Annotation annotation = annotations.get(authorizer.annotationType());
        authorizer.authorize(request, security, handlerMethod, authorizer.annotationType().cast(annotation));
    }
}
