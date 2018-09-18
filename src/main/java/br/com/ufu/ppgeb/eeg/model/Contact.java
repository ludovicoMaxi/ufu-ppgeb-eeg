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
 * Created by joaol on 12/09/18.
 */
@Entity
@Table( name = "CONTACT" )
public class Contact {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @Column( name = "NAME", length = 256, nullable = false )
    private String name;

    @Column( name = "FLAG_ACTIVE", nullable = false )
    private Boolean active;

    @Column( name = "OBJECT_ID", nullable = false )
    private Long objectId;

    @Column( name = "OBJECT_TYPE", nullable = false )
    private Long objectType;

    @Column( name = "PHONE", length = 20 )
    private String phone;

    @Column( name = "CELLPHONE", length = 20 )
    private String cellphone;

    @Column( name = "WHATSAPP", length = 20 )
    private String whatsapp;

    @Column( name = "FACEBOOK", length = 200 )
    private String facebook;

    @Column( name = "INSTAGRAM", length = 200 )
    private String instagram;

    @Column( name = "EMAIL", length = 100 )
    private String email;

    @Column( name = "MAIN" )
    private Boolean main;

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
        if ( !( o instanceof Contact ) )
            return false;
        Contact contact = (Contact) o;
        return Objects.equals( id, contact.id ) && Objects.equals( name, contact.name ) && Objects.equals( active, contact.active )
            && Objects.equals( objectId, contact.objectId ) && Objects.equals( objectType, contact.objectType ) && Objects.equals( phone, contact.phone )
            && Objects.equals( cellphone, contact.cellphone ) && Objects.equals( whatsapp, contact.whatsapp ) && Objects.equals( facebook, contact.facebook )
            && Objects.equals( instagram, contact.instagram ) && Objects.equals( email, contact.email ) && Objects.equals( main, contact.main )
            && Objects.equals( createdAt, contact.createdAt ) && Objects.equals( createdBy, contact.createdBy )
            && Objects.equals( updatedAt, contact.updatedAt ) && Objects.equals( updatedBy, contact.updatedBy );
    }


    @Override
    public int hashCode() {

        return Objects.hash(
            id,
            name,
            active,
            objectId,
            objectType,
            phone,
            cellphone,
            whatsapp,
            facebook,
            instagram,
            email,
            main,
            createdAt,
            createdBy,
            updatedAt,
            updatedBy );
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


    public Boolean getActive() {

        return active;
    }


    public void setActive( Boolean active ) {

        this.active = active;
    }


    public Long getObjectId() {

        return objectId;
    }


    public void setObjectId( Long objectId ) {

        this.objectId = objectId;
    }


    public Long getObjectType() {

        return objectType;
    }


    public void setObjectType( Long objectType ) {

        this.objectType = objectType;
    }


    public String getPhone() {

        return phone;
    }


    public void setPhone( String phone ) {

        this.phone = phone;
    }


    public String getCellphone() {

        return cellphone;
    }


    public void setCellphone( String cellphone ) {

        this.cellphone = cellphone;
    }


    public String getWhatsapp() {

        return whatsapp;
    }


    public void setWhatsapp( String whatsapp ) {

        this.whatsapp = whatsapp;
    }


    public String getFacebook() {

        return facebook;
    }


    public void setFacebook( String facebook ) {

        this.facebook = facebook;
    }


    public String getInstagram() {

        return instagram;
    }


    public void setInstagram( String instagram ) {

        this.instagram = instagram;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail( String email ) {

        this.email = email;
    }


    public Boolean getMain() {

        return main;
    }


    public void setMain( Boolean main ) {

        this.main = main;
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

        return "Contact{" + "id=" + id + ", name='" + name + '\'' + ", active=" + active + ", objectId=" + objectId + ", objectType=" + objectType + ", phone='"
            + phone + '\'' + ", cellphone='" + cellphone + '\'' + ", whatsapp='" + whatsapp + '\'' + ", facebook='" + facebook + '\'' + ", instagram='"
            + instagram + '\'' + ", email='" + email + '\'' + ", main=" + main + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\''
            + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
