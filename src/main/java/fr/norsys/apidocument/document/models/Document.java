package fr.norsys.apidocument.document.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Document {
    @Id
    @SequenceGenerator(
            name = "document_id_seq",
            sequenceName = "document_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "document_id_seq"
    )
    private Long id;
    @Column(unique = true)
    private UUID documentUUID;
    private String name;
    private String type;
    @Column(name = "creation_date",nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "document")
    @Cascade(CascadeType.ALL)
    private List<MetaData> metadata;

}
