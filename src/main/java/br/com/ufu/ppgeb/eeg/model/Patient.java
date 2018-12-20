package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ufu.ppgeb.eeg.utils.CompareDate;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
@Table( name = "patient" )
public class Patient {

    private static final long serialVersionUID = 1L;

    @Id
    @Column( name = "ID" )
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @Column( name = "NAME" )
    String name;

    @Column( name = "DOCUMENT_NUMBER", length = 20, nullable = false )
    private String documentNumber;

    @Column( name = "SEX" )
    char sex;

    @Column( name = "BIRTHDATE", nullable = false )
    @JsonFormat( pattern = "dd/MM/yyyy" )
    private Date birthDate;

    @Column( name = "NACIONALITY", length = 20 )
    private String nacionality;

    @Column( name = "CIVIL_STATUS", length = 20 )
    private String civilStatus;

    @Column( name = "JOB", length = 20 )
    private String job;

    @Column( name = "CREATED_AT", nullable = false )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date createdAt;

    @Column( name = "CREATED_BY", nullable = false )
    private String createdBy;

    @Column( name = "UPDATED_AT" )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date updatedAt;

    @Column( name = "UPDATED_BY", length = 20 )
    private String updatedBy;


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof Patient ) )
            return false;
        Patient patient = (Patient) o;
        return getSex() == patient.getSex() //
            && Objects.equals( getName(), patient.getName() ) //
            && Objects.equals( getDocumentNumber(), patient.getDocumentNumber() ) //
            && CompareDate.compareDates( getBirthDate(), patient.getBirthDate() ) //
            && Objects.equals( getNacionality(), patient.getNacionality() ) //
            && Objects.equals( getCivilStatus(), patient.getCivilStatus() ) //
            && Objects.equals( getJob(), patient.getJob() ) //
            && CompareDate.compareDates( getCreatedAt(), patient.getCreatedAt() ) //
            && Objects.equals( getCreatedBy(), patient.getCreatedBy() ) //
            && CompareDate.compareDates( getUpdatedAt(), patient.getUpdatedAt() ) //
            && Objects.equals( getUpdatedBy(), patient.getUpdatedBy() );
    }


    @Override
    public int hashCode() {

        return Objects.hash(
            getName(),
            getDocumentNumber(),
            getSex(),
            getBirthDate(),
            getNacionality(),
            getCivilStatus(),
            getJob(),
            getCreatedAt(),
            getCreatedBy(),
            getUpdatedAt(),
            getUpdatedBy() );
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


    public String getDocumentNumber() {

        return documentNumber;
    }


    public void setDocumentNumber( String documentNumber ) {

        this.documentNumber = documentNumber;
    }


    public char getSex() {

        return sex;
    }


    public void setSex( char sexo ) {

        this.sex = sexo;
    }


    public Date getBirthDate() {

        return birthDate;
    }


    public void setBirthDate( Date birthDate ) {

        this.birthDate = birthDate;
    }


    public String getNacionality() {

        return nacionality;
    }


    public void setNacionality( String nacionality ) {

        this.nacionality = nacionality;
    }


    public String getCivilStatus() {

        return civilStatus;
    }


    public void setCivilStatus( String civilStatus ) {

        this.civilStatus = civilStatus;
    }


    public String getJob() {

        return job;
    }


    public void setJob( String job ) {

        this.job = job;
    }


    public Date getCreatedAt() {

        return createdAt;
    }


    public void setCreatedAt( Date createdAt ) {

        this.createdAt = createdAt;
    }


    public String getCreatedBy() {

        return createdBy;
    }


    public void setCreatedBy( String createdBy ) {

        this.createdBy = createdBy;
    }


    public Date getUpdatedAt() {

        return updatedAt;
    }


    public void setUpdatedAt( Date updatedAt ) {

        this.updatedAt = updatedAt;
    }


    public String getUpdatedBy() {

        return updatedBy;
    }


    public void setUpdatedBy( String updatedBy ) {

        this.updatedBy = updatedBy;
    }


    @Override
    public String toString() {

        return "Patient{" + "name='" + name + '\'' + ", documentNumber='" + documentNumber + '\'' + ", sex=" + sex + ", birthDate=" + birthDate
            + ", nacionality='" + nacionality + '\'' + ", civilStatus='" + civilStatus + '\'' + ", job='" + job + '\'' + ", createdAt=" + createdAt
            + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
