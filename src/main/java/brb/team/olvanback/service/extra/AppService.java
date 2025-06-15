package brb.team.olvanback.service.extra;

import brb.team.olvanback.utils.jwt.JwtGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppService {

    private final JwtGenerator jwtGenerator;
    private final HttpServletRequest request;

    public Long getOrgId() {
        return jwtGenerator.extractOrgId(request.getHeader("Authorization").substring(7));
    }

    public String getRole() {
        return jwtGenerator.extractRole(request.getHeader("Authorization").substring(7)).getFirst();
    }

    public String getUsername() {
        return jwtGenerator.extractUserName(request.getHeader("Authorization").substring(7));
    }
}
