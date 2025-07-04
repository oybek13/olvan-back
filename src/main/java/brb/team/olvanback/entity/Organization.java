package brb.team.olvanback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "olvan_organizations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Organization extends BaseEntity {

    @Column(name = "full_name", columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "director_phone_number")
    private String directorPhoneNumber;

    @Column(name = "director_full_name", length = 500)
    private String directorFullName;

    @Column(name = "date_begin")
    private LocalDate dateBegin;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "inn_or_pinfl")
    private String innOrPinfl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany(mappedBy = "organizations")
    @JsonIgnore
    private List<Student> students;
}
