package brb.team.olvanback.service;

import brb.team.olvanback.dto.*;
import brb.team.olvanback.entity.*;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.*;
import brb.team.olvanback.service.extra.AppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final AppService appService;
    private final JdbcTemplate jdbcTemplate;
    private final StudentRepository studentRepository;
    private final Mapper mapper;
    private final LessonRepository lessonRepository;
    private final OrganizationRepository organizationRepository;
    private final StudentOrganizationLessonRepository studentOrganizationLessonRepository;

    @Transactional
    public CommonResponse registerClient(RegisterClientDto request) throws JsonProcessingException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException("Username already exists: " + request.getUsername());
        }
        User userClient = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_CLIENT)
                .isActive(true)
                .build());
        log.info("UserClient created successfully: {}", objectMapper.writeValueAsString(userClient));
        Client client = clientRepository.save(Client.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .bio(request.getBio())
                .user(userClient)
                .build());
        log.info("Client created successfully: {}", objectMapper.writeValueAsString(client));
        return CommonResponse.builder()
                .success(true)
                .message("Client created successfully!")
                .build();
    }

    public CommonResponse getClientInfo() {
        String role = appService.getRole();
        String username = appService.getUsername();
        if (role.equals(UserRole.ROLE_CLIENT.name())) {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("User not found with username: " + username));
            Client client = clientRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Client not found with username: " + username));
            return CommonResponse.builder()
                    .success(true)
                    .message("Success!")
                    .data(RegisterClientDto.builder()
                            .username(user.getUsername())
                            .fullName(client.getFullName())
                            .phoneNumber(client.getPhoneNumber())
                            .address(client.getAddress())
                            .enrollType(client.getEnrollType())
                            .bio(client.getBio())
                            .build())
                    .build();
        }
        return CommonResponse.builder()
                .success(false)
                .message("Oops, user not Client")
                .build();
    }

    @Transactional
    public CommonResponse editClientProfile(Long id, RegisterClientDto request) throws JsonProcessingException {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Client not found with id: " + id));
        User user = userRepository.findById(client.getUser().getId()).orElseThrow(
                () -> new DataNotFoundException("UserClient not found with id: " + client.getUser().getId()));
        if (!request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exists: " + request.getUsername());
            }
        }
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User editedUserClient = userRepository.save(user);
        log.info("UserClient updated: {}", objectMapper.writeValueAsString(editedUserClient));
        client.setFullName(request.getFullName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setAddress(request.getAddress());
        client.setEnrollType(request.getEnrollType());
        client.setBio(request.getBio());
        Client editedClient = clientRepository.save(client);
        log.info("Client updated: {}", objectMapper.writeValueAsString(editedClient));
        return CommonResponse.builder()
                .success(true)
                .message("Edited successfully!")
                .build();
    }

    @Transactional
    public CommonResponse createKid(KidRequest kidRequest) throws JsonProcessingException {
        if (userRepository.existsByUsername(kidRequest.getUsername())) {
            throw new UsernameAlreadyExistException("Username already exists: " + kidRequest.getUsername());
        }
        User userStudent = userRepository.save(User.builder()
                .username(kidRequest.getUsername())
                .password(passwordEncoder.encode(kidRequest.getPassword()))
                .role(UserRole.ROLE_STUDENT)
                .isActive(true)
                .build());
        log.info("UserStudent created: {}", objectMapper.writeValueAsString(userStudent));
        Client parent = getInfoClient();
        Student student = studentRepository.save(Student.builder()
                .fullName(kidRequest.getFullName())
                .parentsFullName(parent.getFullName())
                .studentPhoneNumber(kidRequest.getStudentPhoneNumber())
                .parentsPhoneNumber(parent.getPhoneNumber())
                .enrollType(null)
                .dateBegin(LocalDate.now())
                .user(userStudent)
                .client(parent)
                .birthDate(kidRequest.getBirthDate())
                .build());
        log.info("Student created: {}", objectMapper.writeValueAsString(student));
        return CommonResponse.builder()
                .success(true)
                .message("Kid created successfully!")
                .build();
    }

    public CommonResponse getAllKids() throws JsonProcessingException {
        List<Student> kids = studentRepository.findByClient(getInfoClient());
        return CommonResponse.builder()
                .success(true)
                .message("Success")
                .data(mapper.mapStudents(kids))
                .build();
    }

    @Transactional
    public CommonResponse editKid(Long id, KidRequest kidRequest) throws JsonProcessingException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Student/Kid not found with id: " + id));
        User user = userRepository.findById(student.getUser().getId()).orElse(null);
        if (!user.getUsername().equals(kidRequest.getUsername())) {
            if (userRepository.existsByUsername(kidRequest.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exists: " + kidRequest.getUsername());
            }
        }
        user.setUsername(kidRequest.getUsername());
        user.setPassword(passwordEncoder.encode(kidRequest.getPassword()));
        User editedUserStudent = userRepository.save(user);
        log.info("UserStudent/Kid edited: {}", objectMapper.writeValueAsString(editedUserStudent));
        student.setFullName(kidRequest.getFullName());
        student.setStudentPhoneNumber(kidRequest.getStudentPhoneNumber());
        student.setBirthDate(kidRequest.getBirthDate());
        Student editedStudent = studentRepository.save(student);
        log.info("Student/Kid edited: {}", objectMapper.writeValueAsString(editedStudent));
        return CommonResponse.builder()
                .success(true)
                .message("Student/Kid edited successfully!")
                .build();
    }

    public CommonResponse getOneKid(Long id) throws JsonProcessingException {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Student/Kid not found with id: " + id));
        User user = userRepository.findById(student.getUser().getId()).orElseThrow(
                () -> new DataNotFoundException("UserStudent not found with id: " + student.getUser().getId()));
        return CommonResponse.builder()
                .success(true)
                .message("Success")
                .data(Mapper.mapStudent(user, student))
                .build();
    }

    @Transactional
    public CommonResponse deleteKid(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Student/Kid not found with id: " + id));
        studentRepository.delete(student);
        log.info("Student/Kid deleted successfully with id: {}", id);
        User user = userRepository.findById(student.getUser().getId()).orElseThrow(
                () -> new DataNotFoundException("UserStudent not found with id: " + student.getUser().getId()));
        userRepository.delete(user);
        log.info("UserStudent deleted successfully with id: {}", user.getId());
        return CommonResponse.builder()
                .success(true)
                .message("Student/Kid deleted successfully!")
                .build();
    }

    private Client getInfoClient() {
        String username = appService.getUsername();
        User user = userRepository.findByUsername(username).orElse(null);
        return clientRepository.findByUser(user).orElse(null);
    }

    public CommonResponse getLessonByOrgId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lesson> lessonPage = lessonRepository.findByOrgId(id, pageable);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Map.of(
                        "contents", lessonPage.getContent(),
                        "totalElements", lessonPage.getTotalElements(),
                        "page", page,
                        "size", size
                ))
                .build();
    }

    public CommonResponse linkKidWithOrganization(Long studentId, Long organizationId, Long lessonId) throws JsonProcessingException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new DataNotFoundException("Student not found with id: " + studentId));

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + organizationId));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new DataNotFoundException("Lesson not found with id: " + lessonId));

        if (!student.getOrganizations().contains(organization)) {
            student.getOrganizations().add(organization);
        }
        studentRepository.save(student);

        if (!studentOrganizationLessonRepository.existsByStudentAndOrganizationAndLesson(student, organization, lesson)) {
            studentOrganizationLessonRepository.save(StudentOrganizationLesson.builder()
                            .student(student)
                            .organization(organization)
                            .lesson(lesson)
                    .build());
        }

        return CommonResponse.builder()
                .success(true)
                .message("Lesson linked successfully!")
                .build();
    }

    public CommonResponse getCurrentLessonsByStudent(Long studentId) {
        // 1. Studentga tegishli barcha organization-lesson larni tortamiz
        List<StudentOrganizationLesson> solList = studentOrganizationLessonRepository.findByStudentId(studentId);

        // 2. OrganizationId -> lesson list bo‘yicha guruhlab olamiz
        //{
        //  2L: [sol1]
        //  3L: [sol2, sol3]
        //}
        Map<Long, List<StudentOrganizationLesson>> groupedByOrg = solList
                .stream()
                .collect(Collectors.groupingBy(sol -> sol.getOrganization().getId()));

        // 3. Natijaviy DTO list
        List<CurrentOrgsAndLessonsDto> result = new ArrayList<>();

        for (Map.Entry<Long, List<StudentOrganizationLesson>> entry : groupedByOrg.entrySet()) {
            List<LessonDto> lessonDtos = entry
                    .getValue()
                    .stream()
                    .map(sol -> {
                        Lesson l = sol.getLesson();
                        return LessonDto.builder()
                                .id(l.getId())
                                .name(l.getName())
                                .description(l.getDescription())
                                .ageCategory(l.getAgeCategory())
                                .studentCount(l.getPupilCount())
                                .price(l.getPrice())
                                .teacherFullName(l.getTeacherFullName())
                                .days(l.getDays())
                                .timeBegin(l.getTimeBegin())
                                .timeEnd(l.getTimeEnd())
                                .build();
                    }).toList();

            // faqat birinchi organization ni olish
            Organization org = entry.getValue().get(0).getOrganization();
            OrganizationDto orgDto = OrganizationDto.builder()
                    .id(org.getId())
                    .fullName(org.getFullName())
                    .phoneNumber(org.getPhoneNumber())
                    .dateBegin(org.getDateBegin())
                    .address(org.getAddress())
                    .build();

            result.add(CurrentOrgsAndLessonsDto.builder()
                    .organization(orgDto)
                    .lesson(lessonDtos)
                    .build());
        }

        CurrentOrgsLessonsDto responseDto = CurrentOrgsLessonsDto.builder()
                .data(result)
                .build();

        return CommonResponse.builder()
                .success(true)
                .message("Success")
                .data(responseDto)
                .build();
    }


    public CommonResponse getSubscriptions() {
        String sql = "select * from olvan_subscriptions";
        List<SubscriptionDto> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SubscriptionDto.class));
        return CommonResponse.builder()
                .success(true)
                .message("Success")
                .data(result)
                .build();
    }


}
