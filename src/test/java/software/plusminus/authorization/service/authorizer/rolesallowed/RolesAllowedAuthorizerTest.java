package software.plusminus.authorization.service.authorizer.rolesallowed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.method.HandlerMethod;
import software.plusminus.authorization.exception.AuthorizationException;
import software.plusminus.security.SecurityRequest;

import javax.annotation.security.RolesAllowed;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RolesAllowedAuthorizerTest {

    @Mock
    private SecurityRequest request;
    @Mock
    private HandlerMethod method;
    @Mock
    private RolesAllowed rolesAllowed;

    private RolesAllowedAuthorizer rolesAllowedAuthorizer = new RolesAllowedAuthorizer();

    @Test
    public void userHasRole() throws Exception {
        when(method.getMethodAnnotation(RolesAllowed.class))
                .thenReturn(rolesAllowed);
        when(rolesAllowed.value())
                .thenReturn(new String[]{"role_value"});
        when(request.isUserInRole("role_value"))
                .thenReturn(true);

        rolesAllowedAuthorizer.authorize(request, method);
    }

    @Test(expected = AuthorizationException.class)
    public void userHasNoRole() throws Exception {
        when(method.getMethodAnnotation(RolesAllowed.class))
                .thenReturn(rolesAllowed);
        when(rolesAllowed.value())
                .thenReturn(new String[]{"role_value"});

        rolesAllowedAuthorizer.authorize(request, method);
    }
}