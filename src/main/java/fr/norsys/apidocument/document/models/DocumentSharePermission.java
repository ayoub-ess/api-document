package fr.norsys.apidocument.document.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.norsys.apidocument.auth.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "document_share_permission",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "document_id", "permission_id"})
)
@Getter
@Setter
public class DocumentSharePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "document_id")
    @JsonIgnore
    private Document document;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

}
