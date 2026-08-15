package br.com.ufu.ppgeb.eeg.model;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


/**
 * Created by joaol on 08/12/18.
 */
@Entity
@Table( name = "UNIT" )
public class Unit {

    @Id
    @SequenceGenerator( name = "UNIT_SQ", sequenceName = "UNIT_SQ", allocationSize = 1, initialValue = 100 )
    @GeneratedValue( generator = "UNIT_SQ", strategy = GenerationType.SEQUENCE )
    private Long id;

    @Column( name = "NAME", nullable = false )
    private String name;

    @Column( name = "DESCRIPTION" )
    private String description;


    public Long getId() {

        return id;
    }


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof Unit ) )
            return false;
        Unit unit = (Unit) o;
        return Objects.equals( getName(), unit.getName() ) && Objects.equals( getDescription(), unit.getDescription() );
    }


    @Override
    public int hashCode() {

        return Objects.hash( getName(), getDescription() );
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


    public String getDescription() {

        return description;
    }


    public void setDescription( String description ) {

        this.description = description;
    }


    @Override
    public String toString() {

        return "Unit{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}
