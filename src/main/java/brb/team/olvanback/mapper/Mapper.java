package brb.team.olvanback.mapper;

import brb.team.olvanback.dto.OrganizationResponse;
import brb.team.olvanback.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static List<OrganizationResponse> map(List<User> users) {
        List<OrganizationResponse> list = new ArrayList<>();
        for (User user : users) {
            list.add(OrganizationResponse.builder()
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .password(user.getPassword())
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
                .password(user.getPassword())
                .fullName(user.getFullName())
                .inn(user.getInn())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }
}
