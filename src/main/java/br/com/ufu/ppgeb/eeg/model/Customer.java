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


/**
 * Created by joaol on 05/09/18.
 */
@Entity
@Table( name = "CUSTOMER" )
public class Customer {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @Column( name = "NAME", length = 256, nullable = false )
    private String name;

    @Column( name = "DOCUMENT_NUMBER", length = 20, nullable = false )
    private String documentNumber;

    @Column( name = "SECONDURAY_DOCUMENT_NUMBER", length = 20 )
    private String secondurayDocumentNumber;

    @Column( name = "SECONDURAY_DOCUMENT_TYPE", length = 20 )
    private String secondurayDocumentType;

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

    @Column( name = "BIRTHDATE", nullable = false )
    @JsonFormat( pattern = "dd/MM/yyyy" )
    private Date birthDate;

    @Column( name = "NACIONALITY", length = 20 )
    private String nacionality;

    @Column( name = "CIVIL_STATUS", length = 20 )
    private String civilStatus;

    @Column( name = "JOB", length = 20 )
    private String job;


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof Customer ) )
            return false;
        Customer customer = (Customer) o;
        return Objects.equals( getId(), customer.getId() ) && Objects.equals( getName(), customer.getName() )
            && Objects.equals( getDocumentNumber(), customer.getDocumentNumber() )
            && Objects.equals( getSecondurayDocumentNumber(), customer.getSecondurayDocumentNumber() )
            && Objects.equals( getSecondurayDocumentType(), customer.getSecondurayDocumentType() ) && Objects.equals( getCreatedAt(), customer.getCreatedAt() )
            && Objects.equals( getCreatedBy(), customer.getCreatedBy() ) && Objects.equals( getUpdatedAt(), customer.getUpdatedAt() )
            && Objects.equals( getUpdatedBy(), customer.getUpdatedBy() ) && Objects.equals( getBirthDate(), customer.getBirthDate() )
            && Objects.equals( getNacionality(), customer.getNacionality() ) && Objects.equals( getCivilStatus(), customer.getCivilStatus() )
            && Objects.equals( getJob(), customer.getJob() );
    }


    @Override
    public int hashCode() {

        return Objects.hash(
            getId(),
            getName(),
            getDocumentNumber(),
            getSecondurayDocumentNumber(),
            getSecondurayDocumentType(),
            getCreatedAt(),
            getCreatedBy(),
            getUpdatedAt(),
            getUpdatedBy(),
            getBirthDate(),
            getNacionality(),
            getCivilStatus(),
            getJob() );
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


    public String getSecondurayDocumentNumber() {

        return secondurayDocumentNumber;
    }


    public void setSecondurayDocumentNumber( String secondurayDocumentNumber ) {

        this.secondurayDocumentNumber = secondurayDocumentNumber;
    }


    public String getSecondurayDocumentType() {

        return secondurayDocumentType;
    }


    public void setSecondurayDocumentType( String secondurayDocumentType ) {

        this.secondurayDocumentType = secondurayDocumentType;
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


    @Override
    public String toString() {

        return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", documentNumber='" + documentNumber + '\'' + ", secondurayDocumentNumber='"
            + secondurayDocumentNumber + '\'' + ", secondurayDocumentType='" + secondurayDocumentType + '\'' + ", createdAt=" + createdAt + ", createdBy='"
            + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + ", birthDate=" + birthDate + ", nacionality='" + nacionality
            + '\'' + ", civilStatus='" + civilStatus + '\'' + ", job='" + job + '\'' + '}';
    }
}
