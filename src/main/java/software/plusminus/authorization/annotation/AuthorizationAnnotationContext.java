package software.plusminus.authorization.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import software.plusminus.authorization.service.authorizer.Authorizer;
import software.plusminus.inject.NoInject;
import software.plusminus.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

@Component
public class AuthorizationAnnotationContext {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private List<Authorizer<? extends Annotation>> authorizers;
    @NoInject
    private Map<Method, Map<Class<? extends Annotation>, Annotation>> annotations;

    @PostConstruct
    void init() {
        Set<Class<? extends Annotation>> supportedAnnotations = authorizers.stream()
                .map(Authorizer::annotationType)
                .collect(Collectors.toSet());
        Set<Class<? extends Annotation>> annotationsWithMissedAuthorizers = new HashSet<>();
        
        annotations = handlerMapping.getHandlerMethods().values().stream()
                .collect(Collectors.toMap(HandlerMethod::getMethod, 
                        handlerMethod -> AnnotationUtils.findMergedAnnotationsOnMethodAndClass(
                                handlerMethod.getMethod(),
                                annotation -> {
                                    if (supportedAnnotations.contains(annotation.annotationType())) {
                                        return true;
                                    }
                                    if (annotation.annotationType()
                                            .isAnnotationPresent(AuthorizationAnnotation.class)) {
                                        annotationsWithMissedAuthorizers.add(annotation.annotationType());
                                    }
                                    return false;
                                })
                                .stream()
                                .collect(Collectors.toMap(Annotation::annotationType, Function.identity()))));

        if (!annotationsWithMissedAuthorizers.isEmpty()) {
            throw new ApplicationContextException(
                    "Can't find authorizers for the following annotations marked as @AuthorizationAnnotation: "
                            + annotationsWithMissedAuthorizers);
        }
    }

    public Map<Class<? extends Annotation>, Annotation>  getAnnotations(HandlerMethod handlerMethod) {
        Map<Class<? extends Annotation>, Annotation> methodsAnnotations = this.annotations.get(
                handlerMethod.getMethod());
        if (methodsAnnotations == null) {
            throw new IllegalStateException("Unknown method: " + handlerMethod.getMethod());
        }
        return methodsAnnotations;
    }
}
