package cz.cvut.kbss.ear.homeLibrary.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class AbstractIdentifiableObject implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    public Integer getId() {
        return id;
    }
}
