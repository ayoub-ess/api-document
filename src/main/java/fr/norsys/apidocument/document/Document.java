package fr.norsys.apidocument.document;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Document {
    @Id
    @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    private Long id;
    @Column(unique = true)
    private UUID documentUUID;
    private String name;
    private String type;
    private Date creationDate;

    @OneToMany
    private List<MetaData> metadata;

}
