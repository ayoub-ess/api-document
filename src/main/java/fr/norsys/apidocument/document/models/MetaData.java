package fr.norsys.apidocument.document.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metadata")
@Getter
@Setter
public class MetaData {
    @Id
    @SequenceGenerator(
            name = "metadata_id_seq",
            sequenceName = "metadata_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "metadata_id_seq"
    )
    private Long id;
    String key;

    String value;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    @JsonIgnore
    private Document document;
}
