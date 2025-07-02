package brb.team.olvanback.mapper;

import brb.team.olvanback.dto.OrganizationResponse;
import brb.team.olvanback.dto.OrganizationsResponse;
import brb.team.olvanback.dto.PupilResponse;
import brb.team.olvanback.dto.TeacherResponse;
import brb.team.olvanback.entity.Contract;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class Mapper {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

    public Mapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<OrganizationsResponse> mapOrgs(List<User> users) {
        List<OrganizationsResponse> list = new ArrayList<>();
        for (User user : users) {
            Integer pupilCount = userRepository.countByOrgIdAndRole(user.getId(), UserRole.ROLE_PUPIL);
            Integer teacherCount = userRepository.countByOrgIdAndRole(user.getId(), UserRole.ROLE_TEACHER);
            list.add(OrganizationsResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .isActive(user.isActive())
                    .dateBegin(user.getDateBegin())
                    .address(user.getAddress())
                    .studentCount(pupilCount)
                    .teacherCount(teacherCount)
                    .build());
        }
        return list;
    }

    public static OrganizationResponse mapOrg(User user, Contract contract) {
        return OrganizationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .directorPhoneNumber(user.getParentsPhoneNumber())
                .directorFullName(user.getParentsFullName())
                .dateBegin(user.getDateBegin())
                .address(user.getAddress())
                .inn(user.getInn())
                .status(user.isActive())
                .contract(contract)
                .build();
    }

    public static OrganizationResponse mapOrgAccount(User user) {
        return OrganizationResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .directorFullName(user.getParentsFullName())
                .phoneNumber(user.getPhoneNumber())
                .username(user.getUsername())
                .password(user.getPassword())
                .address(user.getAddress())
                .inn(user.getInn())
                .build();
    }

    public static PupilResponse mapPupil(User user) throws JsonProcessingException {
        return PupilResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .parentsFullName(user.getParentsFullName())
                .pupilPhoneNumber(user.getPhoneNumber())
                .parentsPhoneNumber(user.getParentsPhoneNumber())
                .enrollType(user.getEnrollType())
                .dateBegin(user.getDateBegin())
                .courseType(Arrays.asList(objectMapper.readValue(user.getCourseType(), String[].class)))
                .attendance(user.getAttendance())
                .status(user.isActive())
                .teacherName(user.getTeacherName())
                .build();
    }

    public static List<PupilResponse> mapPupils(List<User> content) throws JsonProcessingException {
        List<PupilResponse> list = new ArrayList<>();
        for (User user : content) {
            list.add(mapPupil(user));
        }
        return list;
    }

    public static TeacherResponse mapTeacher(User user) throws JsonProcessingException {
        return TeacherResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .degree(user.getDegree())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .email(user.getEmail())
                .dateBegin(user.getDateBegin())
                .status(user.isActive())
                .experience(user.getExperience())
                .courseType(Arrays.asList(objectMapper.readValue(user.getCourseType(), String[].class)))
                .studentCount(user.getStudentCount())
                .build();
    }

    public static TeacherResponse mapTeacherAccount(User user) {
        return TeacherResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }


    public static List<TeacherResponse> mapTeachers(List<User> content) throws JsonProcessingException {
        List<TeacherResponse> list = new ArrayList<>();
        for (User user : content) {
            list.add(mapTeacher(user));
        }
        return list;
    }

}
