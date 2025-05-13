package brb.team.olvanback.mapper;

import brb.team.olvanback.dto.OrganizationResponse;
import brb.team.olvanback.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Mapper {

    public static List<OrganizationResponse> map(List<User> users) {
        log.info("Mapping users: {}", users);
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

    public static OrganizationResponse map(User user) {
        return OrganizationResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .inn(user.getInn())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }
}
