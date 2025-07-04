package brb.team.olvanback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "olvan_contracts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract extends BaseEntity {

    @Column(length = 500, nullable = false)
    String fileName;

    @Column(length = 500, nullable = false)
    String filePath;

    @Column(nullable = false)
    Long fileSize;

    @Column(name = "org_id")
    Long orgId;
}
