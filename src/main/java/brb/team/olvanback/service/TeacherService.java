package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.TeacherRequest;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.utils.jwt.JwtGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final JwtGenerator jwtGenerator;

    public CommonResponse createTeacher(TeacherRequest teacherRequest) {
        Long orgId = jwtGenerator.extractOrgId(request.getHeader("Authorization").substring(7));
        return null;
    }
}
