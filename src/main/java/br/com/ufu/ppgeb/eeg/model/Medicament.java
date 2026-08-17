package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;
import java.util.Objects;

import static java.util.Objects.isNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * Created by joaol on 08/12/18.
 */
@Getter
@Setter
@Entity
@Table( name = "MEDICAMENT" )
@EntityListeners( AuditingEntityListener.class )
public class Medicament {

    @Id
    @SequenceGenerator( name = "MEDICAMENT_SQ", sequenceName = "MEDICAMENT_SQ", allocationSize = 1 )
    @GeneratedValue( generator = "MEDICAMENT_SQ", strategy = GenerationType.SEQUENCE )
    private Long id;

    @Column( name = "NAME", nullable = false )
    private String name;

    @Column( name = "DESCRIPTION" )
    private String description;

    @CreatedDate
    @Column( name = "CREATED_AT", nullable = false, updatable = false )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date createdAt;

    @CreatedBy
    @Column( name = "CREATED_BY", nullable = false, updatable = false )
    private String createdBy;

    @LastModifiedDate
    @Column( name = "UPDATED_AT" )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date updatedAt;

    @LastModifiedBy
    @Column( name = "UPDATED_BY", length = 20 )
    private String updatedBy;


    public void setName( String name ) {

        this.name = isNull( name ) ? null : name.toUpperCase();
    }


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof Medicament that ) )
            return false;
        return Objects.equals( getName(), that.getName() ) && Objects.equals( getDescription(), that.getDescription() );
    }


    @Override
    public int hashCode() {

        return Objects.hash( getName(), getDescription() );
    }


    @Override
    public String toString() {

        return "Medicament{" + "id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\''
            + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
