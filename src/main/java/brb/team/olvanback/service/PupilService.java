package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.PagePupilsResponse;
import brb.team.olvanback.dto.PupilRequest;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.AppService;
import brb.team.olvanback.specs.PupilSpecification;
import brb.team.olvanback.utils.jwt.JwtGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PupilService {

    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final AppService appService;

    public CommonResponse createPupil(PupilRequest pupilRequest) throws JsonProcessingException {
        if (userRepository.existsByUsername(pupilRequest.getUsername()))
            throw new UsernameNotFoundException("Username " + pupilRequest.getUsername() + " already exists");
        User pupil = userRepository.save(User.builder()
                .username(pupilRequest.getUsername())
                .password(passwordEncoder.encode(pupilRequest.getPassword()))
                .role(UserRole.ROLE_PUPIL)
                .fullName(pupilRequest.getFullName())
                .parentsFullName(pupilRequest.getParentsFullName())
                .phoneNumber(pupilRequest.getPupilPhoneNumber())
                .parentsPhoneNumber(pupilRequest.getParentsPhoneNumber())
                .enrollType(pupilRequest.getEnrollType())
                .dateBegin(pupilRequest.getDateBegin())
                .isActive(pupilRequest.getStatus())
                .attendance(pupilRequest.getAttendance())
                .courseType(objectMapper.writeValueAsString(pupilRequest.getCourseType()))
                .orgId(appService.getOrgId())
                .teacherName(pupilRequest.getTeacherName())
                .build());
        log.warn("Pupil created: {}", objectMapper.writeValueAsString(pupil));
        return CommonResponse.builder()
                .success(true)
                .message("Pupil created successfully!")
                .build();
    }

    public CommonResponse getOnePupil(Long id) throws JsonProcessingException {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Pupil not found with id: " + id));
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapPupil(user))
                .build();
    }

    public CommonResponse getAllPupils(int page,
                                       int size,
                                       Long id,
                                       Boolean status,
                                       String fullName,
                                       List<String> courseTypes) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<User> spec = Specification.where(PupilSpecification.hasRole(UserRole.ROLE_PUPIL))
                .and(PupilSpecification.hasOrgId(appService.getOrgId()))
                .and(PupilSpecification.hasId(id))
                .and(PupilSpecification.hasFullName(fullName))
                .and(PupilSpecification.hasCourseTypes(courseTypes))
                .and(PupilSpecification.isActive(status));
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(PagePupilsResponse.builder()
                        .page(page)
                        .size(size)
                        .totalElements(userPage.getTotalElements())
                        .contents(Mapper.mapPupils(userPage.getContent()))
                        .build())
                .build();
    }

    public CommonResponse updatePupil(PupilRequest pupilRequest, Long id) throws JsonProcessingException {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Pupil not found with id: " + id));
        if (!user.getUsername().equals(pupilRequest.getUsername())) {
            if (userRepository.existsByUsername(pupilRequest.getUsername())) {
                throw new UsernameNotFoundException("Username " + pupilRequest.getUsername() + " already exists");
            }
        }
        user.setUsername(pupilRequest.getUsername());
        user.setPassword(passwordEncoder.encode(pupilRequest.getPassword()));
        user.setFullName(pupilRequest.getFullName());
        user.setParentsFullName(pupilRequest.getParentsFullName());
        user.setPhoneNumber(pupilRequest.getPupilPhoneNumber());
        user.setParentsPhoneNumber(pupilRequest.getParentsPhoneNumber());
        user.setEnrollType(pupilRequest.getEnrollType());
        user.setDateBegin(pupilRequest.getDateBegin());
        user.setActive(pupilRequest.getStatus());
        user.setAttendance(pupilRequest.getAttendance());
        user.setCourseType(objectMapper.writeValueAsString(pupilRequest.getCourseType()));
        user.setTeacherName(pupilRequest.getTeacherName());
        User updatedPupil = userRepository.save(user);
        log.warn("Pupil updated: {}", objectMapper.writeValueAsString(updatedPupil));
        return CommonResponse.builder()
                .success(true)
                .message("Pupil updated successfully with id: " + id)
                .build();
    }

    public CommonResponse deletePupil(Long id) throws JsonProcessingException {
        User user = userRepository.findByIdAndRole(id, UserRole.ROLE_PUPIL).orElseThrow(() -> new DataNotFoundException("Pupil not found with id: " + id));
        userRepository.delete(user);
        log.warn("Pupil deleted: {}", objectMapper.writeValueAsString(user));
        return CommonResponse.builder()
                .success(true)
                .message("Pupil deleted successfully with id: " + id)
                .build();
    }


}
