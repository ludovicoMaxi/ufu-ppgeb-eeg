package br.com.ufu.ppgeb.eeg.model;


import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by joaol on 12/09/18.
 */
@Entity
@Table( name = "OBJECT_TYPE" )
public class ObjectType {

    private static final long serialVersionUID = 1L;

    public static final ObjectType PATIENT = new ObjectType( 1L );

    @Id
    private Long id;

    @Column( name = "NAME", length = 256, nullable = false )
    private String name;

    @Column( name = "DESCRIPTION" )
    private Long description;


    public ObjectType( Long id ) {

        this.id = id;
    }


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof ObjectType ) )
            return false;
        ObjectType that = (ObjectType) o;
        return Objects.equals( id, that.id ) && Objects.equals( name, that.name ) && Objects.equals( description, that.description );
    }


    @Override
    public int hashCode() {

        return Objects.hash( id, name, description );
    }


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public String getName() {

        return name;
    }


    public void setName( String name ) {

        this.name = name;
    }


    public Long getDescription() {

        return description;
    }


    public void setDescription( Long description ) {

        this.description = description;
    }


    @Override
    public String toString() {

        return "ObjectType{" + "id=" + id + ", name='" + name + '\'' + ", description=" + description + '}';
    }
}
