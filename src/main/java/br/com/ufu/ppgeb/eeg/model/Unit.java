package br.com.ufu.ppgeb.eeg.model;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by joaol on 08/12/18.
 */
@Getter
@Setter
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


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof Unit unit ) )
            return false;
        return Objects.equals( getName(), unit.getName() ) && Objects.equals( getDescription(), unit.getDescription() );
    }


    @Override
    public int hashCode() {

        return Objects.hash( getName(), getDescription() );
    }


    @Override
    public String toString() {

        return "Unit{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}
