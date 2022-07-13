package software.plusminus.authorization.service.authorizer.role;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.plusminus.authorization.TestAuthenticationFilter;
import software.plusminus.security.Security;
import software.plusminus.security.SecurityRequest;
import software.plusminus.test.IntegrationTest;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static software.plusminus.check.Checks.check;

public class RoleAuthorizerIntegrationTest extends IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestAuthenticationFilter filter;
    @SpyBean
    private RoleAuthorizer roleAuthorizer;
    @Captor
    private ArgumentCaptor<HttpServletRequest> requestCaptor;
    @Captor
    private ArgumentCaptor<Role> annotationCaptor;
    
    @Test
    public void roleAllowed() throws Exception {
        Security security = Security.builder()
                .username("TestUser")
                .roles(Collections.singleton("admin"))
                .build();
        filter.useSecurity(security);
        
        ResponseEntity<String> result = restTemplate.getForEntity("/", String.class);

        check(result.getBody()).is("test");
        check(result.getStatusCode()).is(HttpStatus.OK);
        verifyAuthorizerIsCalled(security);
    }
    
    @Test
    public void roleIsCaseInsensitive() throws Exception {
        Security security = Security.builder()
                .username("TestUser")
                .roles(Collections.singleton("ADMIN"))
                .build();
        filter.useSecurity(security);
        
        ResponseEntity<String> result = restTemplate.getForEntity("/", String.class);
        
        check(result.getBody()).is("test");
        check(result.getStatusCode()).is(HttpStatus.OK);
        verifyAuthorizerIsCalled(security);
    }
    
    @Test
    public void roleNotAllowed() throws Exception {
        Security security = Security.builder()
                .username("TestUser")
                .roles(Collections.singleton("user"))
                .build();
        filter.useSecurity(security);

        ResponseEntity<Map> response = restTemplate.getForEntity("/", Map.class);

        checkTimestamp(response.getBody());
        check(response.getBody()).is("/forbidden-message.json");
        check(response.getStatusCode()).is(HttpStatus.FORBIDDEN);
        verifyAuthorizerIsCalled(security);
    }
    
    private void verifyAuthorizerIsCalled(Security security) {
        verify(roleAuthorizer).authorize(requestCaptor.capture(), any(), annotationCaptor.capture());
        check(annotationCaptor.getValue().value()).is("admin");
        check(requestCaptor.getValue() instanceof SecurityRequest).isTrue();
        SecurityRequest securityRequest = (SecurityRequest) requestCaptor.getValue();
        check(securityRequest.getSecurity()).isSame(security);
    }
    
    private void checkTimestamp(Map<String, String> errors) {
        String timestamp = errors.get("timestamp");
        ZonedDateTime time = ZonedDateTime.parse(timestamp.replace("+0000", "Z"));
        check(time).recent();
        errors.remove("timestamp");
    }
}