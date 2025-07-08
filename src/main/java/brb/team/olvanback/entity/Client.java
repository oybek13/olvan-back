package brb.team.olvanback.entity;

import brb.team.olvanback.enums.EnrollType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "olvan_clients")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Client extends BaseEntity {

    @Column(name = "full_name", columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Enumerated(EnumType.STRING)
    private EnrollType enrollType;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;
}
