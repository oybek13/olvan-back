package brb.team.olvanback.dto;

import brb.team.olvanback.entity.Contract;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationResponse {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String directorPhoneNumber;
    private String directorFullName;
    private String dateBegin;
    private String address;
    private Boolean status;
    private String inn;
    private Contract contract;
}
