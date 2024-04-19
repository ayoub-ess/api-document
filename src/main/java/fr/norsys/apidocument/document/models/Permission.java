package fr.norsys.apidocument.document.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission")
@Getter
@Setter
public class Permission {
    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "description")
    private PermissionType type;
}
