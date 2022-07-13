package software.plusminus.authorization.service.authorizer.host;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.plusminus.authorization.utils.AuthorizationUtils;
import software.plusminus.security.SecurityParameterProvider;

import java.util.AbstractMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Component
public class HostParameterProvider implements SecurityParameterProvider {

    @Autowired
    private HttpServletRequest request;

    @Override
    public Map.Entry<String, String> provideParameter() {
        String host = AuthorizationUtils.getHost(request);
        return new AbstractMap.SimpleEntry<>("host", host);
    }
}
