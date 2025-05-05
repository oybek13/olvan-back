package brb.team.olvanback.entity;

import brb.team.olvanback.enums.EnrollType;
import brb.team.olvanback.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "olvan_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "parentsFullName")
    private String parentsFullName;

    @Column(name = "pupilPhoneNumber")
    private String pupilPhoneNumber;

    @Column(name = "parentsPhoneNumber")
    private String parentsPhoneNumber;

    @Enumerated(EnumType.STRING)
    private EnrollType enrollType;

    @Column(name = "dateBegin")
    private String dateBegin;

    @Column(name = "inn", length = 9)
    private String inn;

    @Column(name = "courseType", columnDefinition = "TEXT")
    private String courseType;

    @Column(name = "degree")
    private String degree;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "experience")
    private BigDecimal experience;

    @Column(name = "studentCount")
    private Integer studentCount;

    @Column(name = "attendance")
    private Integer attendance;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private boolean isActive;
}
