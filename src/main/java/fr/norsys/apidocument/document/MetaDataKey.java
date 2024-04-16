package fr.norsys.apidocument.document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

public class MetaDataKey {

    private Long id;

    private String key;

    private String value;
}
