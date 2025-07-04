package brb.team.olvanback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "olvan_teachers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @Column(name = "full_name", columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "degree")
    private String degree;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "date_begin")
    private LocalDate dateBegin;

    @Column(name = "experience")
    private BigDecimal experience;

    @Column(name = "student_count")
    private Integer studentCount;

    @Column(name = "course_type", columnDefinition = "TEXT")
    private String courseType;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
}
