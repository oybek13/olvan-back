package brb.team.olvanback.mapper;

import brb.team.olvanback.dto.OrganizationResponse;
import brb.team.olvanback.dto.PupilResponse;
import brb.team.olvanback.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Mapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
}
