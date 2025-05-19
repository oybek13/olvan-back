package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.TeacherRequest;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.utils.jwt.JwtGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    public CommonResponse createTeacher(TeacherRequest teacherRequest) throws JsonProcessingException {
        Long orgId = jwtGenerator.extractOrgId(request.getHeader("Authorization").substring(7));
        if (userRepository.existsByUsername(teacherRequest.getUsername())) {
            throw new UsernameAlreadyExistException("Username " + teacherRequest.getUsername() + " already exist");
        }
        User savedTeacher = userRepository.save(User.builder()
                .username(teacherRequest.getUsername())
                .password(passwordEncoder.encode(teacherRequest.getPassword()))
                .role(UserRole.ROLE_TEACHER)
                .fullName(teacherRequest.getFullName())
                .degree(teacherRequest.getDegree())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .gender(teacherRequest.getGender())
                .email(teacherRequest.getEmail())
                .dateBegin(teacherRequest.getDateBegin())
                .isActive(teacherRequest.getStatus())
                .experience(teacherRequest.getExperience())
                .orgId(orgId)
                .build());
        log.warn("Teacher created: {}", objectMapper.writeValueAsString(savedTeacher));
        return CommonResponse.builder()
                .success(true)
                .message("Teacher created successfully!")
                .build();
    }


}
