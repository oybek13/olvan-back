package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.PagePupilsResponse;
import brb.team.olvanback.dto.StudentRequest;
import brb.team.olvanback.entity.*;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.*;
import brb.team.olvanback.service.extra.AppService;
import brb.team.olvanback.specs.StudentSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final OrganizationRepository organizationRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final AppService appService;
    private final Mapper mapper;
    private final StudentOrganizationLessonRepository studentOrganizationLessonRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public CommonResponse createStudent(StudentRequest studentRequest) throws JsonProcessingException {
        if (userRepository.existsByUsername(studentRequest.getUsername()))
            throw new UsernameAlreadyExistException("Username " + studentRequest.getUsername() + " already exists");
        Long orgId = appService.getOrgId();
        Organization organization = organizationRepository.findById(orgId).orElse(null);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        User userStudent = userRepository.save(User.builder()
                .username(studentRequest.getUsername())
                .password(passwordEncoder.encode(studentRequest.getPassword()))
                .role(UserRole.ROLE_STUDENT)
                .isActive(studentRequest.getStatus())
                .build());
        log.warn("UserStudent created: {}", objectMapper.writeValueAsString(userStudent));
        Student savedStudent = studentRepository.save(Student.builder()
                .fullName(studentRequest.getFullName())
                .parentsFullName(studentRequest.getParentsFullName())
                .studentPhoneNumber(studentRequest.getPupilPhoneNumber())
                .parentsPhoneNumber(studentRequest.getParentsPhoneNumber())
                .enrollType(studentRequest.getEnrollType())
                .dateBegin(studentRequest.getDateBegin())
                .user(userStudent)
                .organizations(organizations)
                .build());
        log.warn("Student created: {}", objectMapper.writeValueAsString(savedStudent));
        return CommonResponse.builder()
                .success(true)
                .message("Student created successfully!")
                .build();
    }

    public CommonResponse getOneStudent(Long id) throws JsonProcessingException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Student not found with id: " + id));
        User user = userRepository.findById(student.getUser().getId()).orElseThrow(() -> new DataNotFoundException("Student user not found with id: " + student.getUser().getId()));
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapStudent(user, student))
                .build();
    }

    public CommonResponse getAllStudents(int page,
                                         int size,
                                         Boolean status,
                                         String fullName,
                                         List<String> courseTypes) throws JsonProcessingException {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        if (UserRole.ROLE_SUPER_ADMIN.name().equals(appService.getRole())) {
            Specification<Student> spec = Specification.where(StudentSpecification.isUserActive(status))
                    .and(StudentSpecification.hasUserFullName(fullName))
                    .and(StudentSpecification.hasCourseTypes(courseTypes));

            Page<Student> studentPage = studentRepository.findAll(spec, pageable);

            return CommonResponse.builder()
                    .success(true)
                    .message("Success!")
                    .data(PagePupilsResponse.builder()
                            .page(page)
                            .size(size)
                            .totalElements(studentPage.getTotalElements())
                            .contents(mapper.mapStudents(studentPage.getContent()))
                            .build())
                    .build();
        }
        Specification<Student> spec = Specification.where(StudentSpecification.isUserActive(status))
                .and(StudentSpecification.hasUserFullName(fullName))
                .and(StudentSpecification.hasCourseTypes(courseTypes))
                .and(StudentSpecification.hasOrgId(appService.getOrgId()));

        Page<Student> studentPage = studentRepository.findAll(spec, pageable);

        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(PagePupilsResponse.builder()
                        .page(page)
                        .size(size)
                        .totalElements(studentPage.getTotalElements())
                        .contents(mapper.mapStudents(studentPage.getContent()))
                        .build())
                .build();
    }


    public CommonResponse updateStudent(StudentRequest studentRequest, Long id) throws JsonProcessingException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Student not found with id: " + id));
        User user = userRepository.findById(student.getUser().getId()).orElse(null);
        if (!user.getUsername().equals(studentRequest.getUsername())) {
            if (userRepository.existsByUsername(studentRequest.getUsername())) {
                throw new UsernameNotFoundException("Username " + studentRequest.getUsername() + " already exists");
            }
        }
        user.setUsername(studentRequest.getUsername());
        user.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        user.setActive(studentRequest.getStatus());
        student.setFullName(studentRequest.getFullName());
        student.setParentsFullName(studentRequest.getParentsFullName());
        student.setStudentPhoneNumber(studentRequest.getPupilPhoneNumber());
        student.setParentsPhoneNumber(studentRequest.getParentsPhoneNumber());
        student.setEnrollType(studentRequest.getEnrollType());
        student.setDateBegin(studentRequest.getDateBegin());
        User editedUserStudent = userRepository.save(user);
        log.warn("UserStudent updated: {}", objectMapper.writeValueAsString(editedUserStudent));
        Student editedStudent = studentRepository.save(student);
        log.warn("Student updated: {}", objectMapper.writeValueAsString(editedStudent));
        return CommonResponse.builder()
                .success(true)
                .message("Student updated successfully with id: " + id)
                .build();
    }

    @Transactional
    public CommonResponse deleteStudent(Long id) throws JsonProcessingException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
        log.warn("Student deleted: {}", objectMapper.writeValueAsString(student));
        User user = userRepository.findById(student.getUser().getId()).orElseThrow(() -> new DataNotFoundException("UserStudent not found with id: " + student.getUser().getId()));
        userRepository.delete(user);
        log.warn("UserStudent deleted: {}", objectMapper.writeValueAsString(user));
        return CommonResponse.builder()
                .success(true)
                .message("Student deleted successfully with id: " + id)
                .build();
    }

    public CommonResponse getCourseTypesByStudentId(Long studentId) {
        Long orgId = appService.getOrgId();
        List<StudentOrganizationLesson> solList = studentOrganizationLessonRepository.findByStudentIdAndOrganizationId(studentId, orgId);
        List<Lesson> lessons = solList
                .stream()
                .map(studentOrganizationLesson -> lessonRepository.findById(studentOrganizationLesson.getLesson().getId()).orElse(null))
                .toList();
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(lessons)
                .build();
    }
}
