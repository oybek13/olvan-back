package brb.team.olvanback.entity;

import brb.team.olvanback.enums.EnrollType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "olvan_students")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student extends BaseEntity {

    @Column(name = "full_name", columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "parents_full_name", columnDefinition = "TEXT")
    private String parentsFullName;

    @Column(name = "student_phone_number")
    private String studentPhoneNumber;

    @Column(name = "parents_phone_number")
    private String parentsPhoneNumber;

    @Enumerated(EnumType.STRING)
    private EnrollType enrollType;

    @Column(name = "date_begin")
    private LocalDate dateBegin;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "student_organization",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private List<Organization> organizations;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
