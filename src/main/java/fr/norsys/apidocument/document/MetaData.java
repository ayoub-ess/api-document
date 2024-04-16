package fr.norsys.apidocument.document;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metadata")
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
    private Document document;
}
