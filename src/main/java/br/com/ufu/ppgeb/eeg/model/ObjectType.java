package br.com.ufu.ppgeb.eeg.model;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by joaol on 12/09/18.
 */
@Getter
@Setter
@Entity
@Table( name = "OBJECT_TYPE" )
public class ObjectType {

    public static final ObjectType PATIENT = new ObjectType( 1L );

    @Id
    private Long id;

    @Column( name = "NAME", length = 256, nullable = false )
    private String name;

    @Column( name = "DESCRIPTION" )
    private Long description;


    public ObjectType() {

    }


    public ObjectType( Long id ) {

        this.id = id;
    }


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof ObjectType that ) )
            return false;
        return Objects.equals( id, that.id ) && Objects.equals( name, that.name ) && Objects.equals( description, that.description );
    }


    @Override
    public int hashCode() {

        return Objects.hash( id, name, description );
    }


    @Override
    public String toString() {

        return "ObjectType{" + "id=" + id + ", name='" + name + '\'' + ", description=" + description + '}';
    }
}
