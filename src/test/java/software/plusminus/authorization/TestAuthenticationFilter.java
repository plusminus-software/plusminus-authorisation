package software.plusminus.authorization;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import software.plusminus.inject.NoInject;
import software.plusminus.security.Security;
import software.plusminus.security.SecurityRequest;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TestAuthenticationFilter extends OncePerRequestFilter {
    
    @NoInject
    private Security security;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        chain.doFilter(new SecurityRequest(request, security), response);
        security = null;
    }
    
    public void useSecurity(Security security) {
        this.security = security;
    }
}
