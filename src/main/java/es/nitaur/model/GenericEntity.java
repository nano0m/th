package es.nitaur.model;

import javax.persistence.*;
import java.io.*;

@MappedSuperclass
public class GenericEntity implements Serializable {
    private static final long serialVersionUID = 3771817657702120569L;

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || !this.getClass().equals(o.getClass())) return false;

        GenericEntity that = (GenericEntity) o;

        return null != this.getId() ? this.getId().equals(that.getId()) : null == that.getId();
    }

    @Override
    public int hashCode() {
        return null != this.getId() ? this.getId().hashCode() : 0;
    }
}