package software.plusminus.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import software.plusminus.authorization.service.AuthorizationService;

@Configuration
@ComponentScan("software.plusminus.authorization")
public class AuthorizationAutoconfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationService authorizationService;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor(authorizationService));
    }
    
}
