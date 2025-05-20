package brb.team.olvanback.mapper;

import brb.team.olvanback.dto.OrganizationResponse;
import brb.team.olvanback.dto.PupilResponse;
import brb.team.olvanback.dto.TeacherResponse;
import brb.team.olvanback.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Mapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<OrganizationResponse> mapOrgs(List<User> users) {
        List<OrganizationResponse> list = new ArrayList<>();
        for (User user : users) {
            list.add(OrganizationResponse.builder()
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .inn(user.getInn())
                    .role(user.getRole())
                    .isActive(user.isActive())
                    .build());
        }
        return list;
    }

    public static OrganizationResponse mapOrg(User user) {
        return OrganizationResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .inn(user.getInn())
                .role(user.getRole())
                .isActive(user.isActive())
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
                .build();
    }

    public static List<PupilResponse> mapPupils(List<User> content) throws JsonProcessingException {
        List<PupilResponse> list = new ArrayList<>();
        for (User user : content) {
            PupilResponse pupil = PupilResponse.builder()
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
                    .build();
            list.add(pupil);
        }
        return list;
    }

    public static TeacherResponse mapTeacher(User user) throws JsonProcessingException {
        return TeacherResponse.builder()
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

    public static List<TeacherResponse> mapTeachers(List<User> content) throws JsonProcessingException {
        List<TeacherResponse> list = new ArrayList<>();
        for (User user : content) {
            TeacherResponse teacher = TeacherResponse.builder()
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
            list.add(teacher);
        }
        return list;
    }
}
