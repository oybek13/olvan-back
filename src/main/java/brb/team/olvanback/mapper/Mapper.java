package brb.team.olvanback.mapper;

import brb.team.olvanback.dto.OrganizationResponse;
import brb.team.olvanback.dto.OrganizationsResponse;
import brb.team.olvanback.dto.PupilResponse;
import brb.team.olvanback.dto.TeacherResponse;
import brb.team.olvanback.entity.*;
import brb.team.olvanback.repository.StudentRepository;
import brb.team.olvanback.repository.TeacherRepository;
import brb.team.olvanback.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Mapper {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public Mapper(UserRepository userRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public List<OrganizationsResponse> mapOrgs(List<Organization> organizations) {
        List<OrganizationsResponse> list = new ArrayList<>();

        // 1. OrgId -> StudentCount map olish
        Map<Long, Integer> studentCountMap = studentRepository.countStudentsByOrgIds(
                organizations
                        .stream()
                        .map(Organization::getId)
                        .toList()
        );

        for (Organization organization : organizations) {
            User user = userRepository.findById(organization.getUser().getId()).orElse(null);
            Integer studentCount = studentCountMap.getOrDefault(organization.getId(), 0);
            Integer teacherCount = teacherRepository.countByOrgId(organization.getId());

            list.add(OrganizationsResponse.builder()
                    .id(organization.getId())
                    .fullName(organization.getFullName())
                    .isActive(user.isActive())
                    .dateBegin(organization.getDateBegin())
                    .address(organization.getAddress())
                    .studentCount(studentCount)
                    .teacherCount(teacherCount)
                    .build());
        }
        return list;
    }

    public static OrganizationResponse mapOrg(User user, Organization organization, Contract contract) {
        return OrganizationResponse.builder()
                .id(organization.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .status(user.isActive())
                .fullName(organization.getFullName())
                .phoneNumber(organization.getPhoneNumber())
                .directorPhoneNumber(organization.getDirectorPhoneNumber())
                .directorFullName(organization.getDirectorFullName())
                .dateBegin(organization.getDateBegin())
                .address(organization.getAddress())
                .inn(organization.getInnOrPinfl())
                .contract(contract)
                .build();
    }

    public static OrganizationResponse mapOrgAccount(User user, Organization organization) {
        return OrganizationResponse.builder()
                .id(organization.getId())
                .fullName(organization.getFullName())
                .directorFullName(organization.getDirectorFullName())
                .phoneNumber(organization.getPhoneNumber())
                .username(user.getUsername())
                .password(user.getPassword())
                .address(organization.getAddress())
                .inn(organization.getInnOrPinfl())
                .build();
    }

    public static PupilResponse mapStudent(User user, Student student) throws JsonProcessingException {
        return PupilResponse.builder()
                .id(student.getId())
                .username(user.getUsername())
                .status(user.isActive())
                .fullName(student.getFullName())
                .parentsFullName(student.getParentsFullName())
                .pupilPhoneNumber(student.getStudentPhoneNumber())
                .parentsPhoneNumber(student.getParentsPhoneNumber())
                .enrollType(student.getEnrollType())
                .dateBegin(student.getDateBegin())
                .birthDate(student.getBirthDate())
                .build();
    }

    public List<PupilResponse> mapStudents(List<Student> content) throws JsonProcessingException {
        List<PupilResponse> list = new ArrayList<>();
        for (Student student : content) {
            User user = userRepository.findById(student.getUser().getId()).orElse(null);
            list.add(mapStudent(user, student));
        }
        return list;
    }

    public static TeacherResponse mapTeacher(User user, Teacher teacher) throws JsonProcessingException {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .username(user.getUsername())
                .status(user.isActive())
                .fullName(teacher.getFullName())
                .degree(teacher.getDegree())
                .phoneNumber(teacher.getPhoneNumber())
                .gender(teacher.getGender())
                .email(teacher.getEmail())
                .dateBegin(teacher.getDateBegin())
                .experience(teacher.getExperience())
                .courseType(Arrays.asList(objectMapper.readValue(teacher.getCourseType(), String[].class)))
                .studentCount(teacher.getStudentCount())
                .build();
    }

    public static TeacherResponse mapTeacherAccount(Teacher teacher) {
        return TeacherResponse.builder()
                .fullName(teacher.getFullName())
                .email(teacher.getEmail())
                .phoneNumber(teacher.getPhoneNumber())
                .build();
    }


    public List<TeacherResponse> mapTeachers(List<Teacher> content) throws JsonProcessingException {
        List<TeacherResponse> list = new ArrayList<>();
        for (Teacher teacher : content) {
            User user = userRepository.findById(teacher.getUser().getId()).orElse(null);
            list.add(mapTeacher(user, teacher));
        }
        return list;
    }

}
