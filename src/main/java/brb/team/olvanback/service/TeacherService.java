package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.PageTeachersResponse;
import brb.team.olvanback.dto.TeacherAccountRequest;
import brb.team.olvanback.dto.TeacherRequest;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.AppService;
import brb.team.olvanback.specs.TeacherSpecification;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final AppService appService;

    public CommonResponse createTeacher(TeacherRequest teacherRequest) throws JsonProcessingException {
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
                .studentCount(teacherRequest.getStudentCount())
                .courseType(objectMapper.writeValueAsString(teacherRequest.getCourseType()))
                .orgId(appService.getOrgId())
                .build());
        log.warn("Teacher created: {}", objectMapper.writeValueAsString(savedTeacher));
        return CommonResponse.builder()
                .success(true)
                .message("Teacher created successfully!")
                .build();
    }

    public CommonResponse getOneTeacher(Long id) throws JsonProcessingException {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Teacher not found with id: " + id));
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapTeacher(user))
                .build();
    }

    public CommonResponse getAllTeachers(int page,
                                         int size,
                                         String fullName,
                                         String phoneNumber,
                                         Boolean status,
                                         String dateBegin,
                                         Integer studentCount) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<User> spec = Specification.where(TeacherSpecification.hasRole(UserRole.ROLE_TEACHER))
                .and(TeacherSpecification.hasOrgId(appService.getOrgId()))
                .and(TeacherSpecification.hasFullName(fullName))
                .and(TeacherSpecification.hasPhoneNumber(phoneNumber))
                .and(TeacherSpecification.hasDateBegin(dateBegin))
                .and(TeacherSpecification.hasStudentCount(studentCount))
                .and(TeacherSpecification.isActive(status));
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(PageTeachersResponse.builder()
                        .page(page)
                        .size(size)
                        .totalElements(userPage.getTotalElements())
                        .contents(Mapper.mapTeachers(userPage.getContent()))
                        .build())
                .build();
    }

    public CommonResponse deleteTeacher(Long id) throws JsonProcessingException {
        User user = userRepository.findByIdAndRole(id, UserRole.ROLE_TEACHER).orElseThrow(() -> new DataNotFoundException("Teacher not found with id: " + id));
        userRepository.delete(user);
        log.warn("Teacher deleted: {}", objectMapper.writeValueAsString(user));
        return CommonResponse.builder()
                .success(true)
                .message("Teacher deleted successfully!")
                .build();
    }

    public CommonResponse updateTeacher(Long id, TeacherRequest teacherRequest) throws JsonProcessingException {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Teacher not found with id: " + id));
        if (!user.getUsername().equals(teacherRequest.getUsername())) {
            if (userRepository.existsByUsername(teacherRequest.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exists: ".concat(teacherRequest.getUsername()));
            }
        }
        user.setUsername(teacherRequest.getUsername());
        user.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        user.setFullName(teacherRequest.getFullName());
        user.setDegree(teacherRequest.getDegree());
        user.setPhoneNumber(teacherRequest.getPhoneNumber());
        user.setGender(teacherRequest.getGender());
        user.setEmail(teacherRequest.getEmail());
        user.setDateBegin(teacherRequest.getDateBegin());
        user.setActive(teacherRequest.getStatus());
        user.setExperience(teacherRequest.getExperience());
        user.setStudentCount(teacherRequest.getStudentCount());
        user.setCourseType(objectMapper.writeValueAsString(teacherRequest.getCourseType()));
        User updateUser = userRepository.save(user);
        log.warn("Teacher updated: {}", objectMapper.writeValueAsString(updateUser));
        return CommonResponse.builder()
                .success(true)
                .message("Teacher updated successfully!")
                .build();
    }

    public CommonResponse getTeacherAccount() {
        User user = userRepository.findByUsername(appService.getUsername()).orElseThrow(() -> new DataNotFoundException("User not found!"));
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapTeacherAccount(user))
                .build();

    }

    public CommonResponse updateTeacherAccount(TeacherAccountRequest teacherAccountRequest) throws JsonProcessingException {
        User user = userRepository.findByUsername(appService.getUsername()).orElseThrow(() -> new DataNotFoundException("User not found!"));
        user.setFullName(teacherAccountRequest.getFullName());
        user.setEmail(teacherAccountRequest.getEmail());
        user.setPhoneNumber(teacherAccountRequest.getPhoneNumber());
        user.setAddress(teacherAccountRequest.getAddress());
        User updatedAccount = userRepository.save(user);
        log.warn("Teacher himself/herself updated account: {}", objectMapper.writeValueAsString(updatedAccount));
        return CommonResponse.builder()
                .success(true)
                .message("Updated!")
                .build();
    }


}
