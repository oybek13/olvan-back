package brb.team.olvanback.service;

import brb.team.olvanback.dto.*;
import brb.team.olvanback.entity.Teacher;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.TeacherRepository;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.AppService;
import brb.team.olvanback.specs.TeacherSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final AppService appService;
    private final TeacherRepository teacherRepository;
    private final Mapper mapper;

    public CommonResponse createTeacher(TeacherRequest teacherRequest) throws JsonProcessingException {
        if (userRepository.existsByUsername(teacherRequest.getUsername())) {
            throw new UsernameAlreadyExistException("Username " + teacherRequest.getUsername() + " already exist");
        }
        User savedUserTeacher = userRepository.save(User.builder()
                .username(teacherRequest.getUsername())
                .password(passwordEncoder.encode(teacherRequest.getPassword()))
                .role(UserRole.ROLE_TEACHER)
                .isActive(teacherRequest.getStatus())
                .build());
        log.warn("UserTeacher created: {}", objectMapper.writeValueAsString(savedUserTeacher));
        Teacher savedTeacher = teacherRepository.save(Teacher.builder()
                .fullName(teacherRequest.getFullName())
                .degree(teacherRequest.getDegree())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .gender(teacherRequest.getGender())
                .email(teacherRequest.getEmail())
                .dateBegin(teacherRequest.getDateBegin())
                .experience(teacherRequest.getExperience())
                .studentCount(teacherRequest.getStudentCount())
                .courseType(objectMapper.writeValueAsString(teacherRequest.getCourseType()))
                .user(savedUserTeacher)
                .orgId(appService.getOrgId())
                .build());
        log.warn("Teacher created: {}", objectMapper.writeValueAsString(savedTeacher));
        return CommonResponse.builder()
                .success(true)
                .message("Teacher created successfully!")
                .build();
    }

    public CommonResponse getOneTeacher(Long id) throws JsonProcessingException {
        TeacherInfoDto teacherDto = findById(id);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapTeacher(teacherDto.getUser(), teacherDto.getTeacher()))
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

        if (UserRole.ROLE_SUPER_ADMIN.name().equals(appService.getRole())) {
            Specification<Teacher> spec = Specification.where(TeacherSpecification.hasFullName(fullName))
                    .and(TeacherSpecification.hasPhoneNumber(phoneNumber))
                    .and(TeacherSpecification.hasDateBegin(dateBegin))
                    .and(TeacherSpecification.isActive(status)) // ← Bu faqat `User`dan olinyapti
                    .and(TeacherSpecification.hasStudentCount(studentCount));

            Page<Teacher> teacherPage = teacherRepository.findAll(spec, pageable);

            return CommonResponse.builder()
                    .success(true)
                    .message("Success!")
                    .data(PageTeachersResponse.builder()
                            .page(page)
                            .size(size)
                            .totalElements(teacherPage.getTotalElements())
                            .contents(mapper.mapTeachers(teacherPage.getContent()))
                            .build())
                    .build();
        }
        Specification<Teacher> spec = Specification.where(TeacherSpecification.hasFullName(fullName))
                .and(TeacherSpecification.hasPhoneNumber(phoneNumber))
                .and(TeacherSpecification.hasDateBegin(dateBegin))
                .and(TeacherSpecification.isActive(status)) // ← Bu faqat `User`dan olinyapti
                .and(TeacherSpecification.hasStudentCount(studentCount))
                .and(TeacherSpecification.hasOrgId(appService.getOrgId()));

        Page<Teacher> teacherPage = teacherRepository.findAll(spec, pageable);

        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(PageTeachersResponse.builder()
                        .page(page)
                        .size(size)
                        .totalElements(teacherPage.getTotalElements())
                        .contents(mapper.mapTeachers(teacherPage.getContent()))
                        .build())
                .build();
    }

    @Transactional
    public CommonResponse deleteTeacher(Long id) throws JsonProcessingException {
        TeacherInfoDto teacherDto = findById(id);
        teacherRepository.delete(teacherDto.getTeacher());
        log.warn("Teacher deleted: {}", objectMapper.writeValueAsString(teacherDto.getTeacher()));
        userRepository.delete(teacherDto.getUser());
        log.warn("UserTeacher deleted: {}", objectMapper.writeValueAsString(teacherDto.getTeacher()));
        return CommonResponse.builder()
                .success(true)
                .message("Teacher deleted successfully!")
                .build();
    }

    public CommonResponse updateTeacher(Long id, TeacherRequest teacherRequest) throws JsonProcessingException {
        TeacherInfoDto teacherDto = findById(id);
        User user = teacherDto.getUser();
        Teacher teacher = teacherDto.getTeacher();
        if (!user.getUsername().equals(teacherRequest.getUsername())) {
            if (userRepository.existsByUsername(teacherRequest.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exists: ".concat(teacherRequest.getUsername()));
            }
        }
        user.setUsername(teacherRequest.getUsername());
        user.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        user.setActive(teacherRequest.getStatus());
        teacher.setFullName(teacherRequest.getFullName());
        teacher.setDegree(teacherRequest.getDegree());
        teacher.setPhoneNumber(teacherRequest.getPhoneNumber());
        teacher.setGender(teacherRequest.getGender());
        teacher.setEmail(teacherRequest.getEmail());
        teacher.setDateBegin(teacherRequest.getDateBegin());
        teacher.setExperience(teacherRequest.getExperience());
        teacher.setStudentCount(teacherRequest.getStudentCount());
        teacher.setCourseType(objectMapper.writeValueAsString(teacherRequest.getCourseType()));
        User editedUserTeacher = userRepository.save(user);
        log.warn("UserTeacher updated: {}", objectMapper.writeValueAsString(editedUserTeacher));
        Teacher editedTeacher = teacherRepository.save(teacher);
        log.warn("Teacher updated: {}", objectMapper.writeValueAsString(editedTeacher));
        return CommonResponse.builder()
                .success(true)
                .message("Teacher updated successfully!")
                .build();
    }

    public CommonResponse getTeacherAccount() {
        TeacherInfoDto teacherDto = findByUsername();
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapTeacherAccount(teacherDto.getTeacher()))
                .build();

    }

    public CommonResponse updateTeacherAccount(TeacherAccountRequest teacherAccountRequest) throws JsonProcessingException {
        TeacherInfoDto teacherDto = findByUsername();
        Teacher teacher = teacherDto.getTeacher();
        teacher.setFullName(teacherAccountRequest.getFullName());
        teacher.setEmail(teacherAccountRequest.getEmail());
        teacher.setPhoneNumber(teacherAccountRequest.getPhoneNumber());
        teacher.setAddress(teacherAccountRequest.getAddress());
        Teacher editedTeacher = teacherRepository.save(teacher);
        log.warn("Teacher himself/herself updated account: {}", objectMapper.writeValueAsString(editedTeacher));
        return CommonResponse.builder()
                .success(true)
                .message("Updated!")
                .build();
    }

    private TeacherInfoDto findById(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(
                () -> new DataNotFoundException("Teacher not found with teacherId: " + teacherId));
        User user = userRepository.findById(teacher.getUser().getId()).orElseThrow(
                () -> new DataNotFoundException("UserTeacher not found with id: " + teacher.getUser().getId()));
        return TeacherInfoDto.builder()
                .teacher(teacher)
                .user(user)
                .build();
    }

    private TeacherInfoDto findByUsername() {
        User user = userRepository.findByUsername(appService.getUsername()).orElseThrow(() -> new DataNotFoundException("UserTeacher not found!"));
        Teacher teacher = teacherRepository.findByUserId(user.getId()).orElseThrow(() -> new DataNotFoundException("Teacher not found with userId: " + user.getId()));
        return TeacherInfoDto.builder()
                .user(user)
                .teacher(teacher)
                .build();
    }

}
