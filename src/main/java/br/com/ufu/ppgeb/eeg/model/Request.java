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
@Table( name = "REQUEST" )
public class Request {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @Column( name = "MEDICAL_RECORD" )
    private Long medicalRecord;

    @Column( name = "MEDICAL_REQUEST" )
    private Long medicalRequest;

    @Column( name = "SECTOR" )
    private String sector;

    @Column( name = "AGREEMENT" )
    private String agreement;

    @Column( name = "DOCTOR_REQUESTANT" )
    private String doctorRequestant;

    @Column( name = "USER" )
    private String user;

    @Column( name = "CLINIC_ORIGIN" )
    private String clinicOrigin;

    @Column( name = "CITY_ORIGIN" )
    private String CityOrigin;

    @Column( name = "PATIENT_ID" )
    private Long patientId;

    @Column( name = "REQUEST_DATE" )
    private Date requestDate;

    @Column( name = "ACHIEVEMENT_DATE" )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date achievementDate;

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
        if ( !( o instanceof Request ) )
            return false;
        Request request = (Request) o;
        return Objects.equals( getMedicalRecord(), request.getMedicalRecord() ) && //
            Objects.equals( getMedicalRequest(), request.getMedicalRequest() ) && //
            Objects.equals( getSector(), request.getSector() ) && //
            Objects.equals( getAgreement(), request.getAgreement() ) && //
            Objects.equals( getDoctorRequestant(), request.getDoctorRequestant() ) && //
            Objects.equals( getUser(), request.getUser() ) && //
            Objects.equals( getClinicOrigin(), request.getClinicOrigin() ) && //
            Objects.equals( getCityOrigin(), request.getCityOrigin() ) && //
            Objects.equals( getPatientId(), request.getPatientId() ) && //
            Objects.equals( getRequestDate(), request.getRequestDate() ) && //
            Objects.equals( getAchievementDate(), request.getAchievementDate() ) && //
            CompareDate.compareDates( getCreatedAt(), request.getCreatedAt() ) && //
            Objects.equals( getCreatedBy(), request.getCreatedBy() ) && //
            CompareDate.compareDates( getUpdatedAt(), request.getUpdatedAt() ) && //
            Objects.equals( getUpdatedBy(), request.getUpdatedBy() );
    }


    @Override
    public int hashCode() {

        return Objects.hash(
            getMedicalRecord(),
            getMedicalRequest(),
            getSector(),
            getAgreement(),
            getDoctorRequestant(),
            getUser(),
            getClinicOrigin(),
            getCityOrigin(),
            getPatientId(),
            getRequestDate(),
            getAchievementDate(),
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


    public Long getMedicalRecord() {

        return medicalRecord;
    }


    public void setMedicalRecord( Long medicalRecord ) {

        this.medicalRecord = medicalRecord;
    }


    public Long getMedicalRequest() {

        return medicalRequest;
    }


    public void setMedicalRequest( Long medicalRequest ) {

        this.medicalRequest = medicalRequest;
    }


    public String getSector() {

        return sector;
    }


    public void setSector( String sector ) {

        this.sector = sector;
    }


    public String getAgreement() {

        return agreement;
    }


    public void setAgreement( String agreement ) {

        this.agreement = agreement;
    }


    public String getDoctorRequestant() {

        return doctorRequestant;
    }


    public void setDoctorRequestant( String doctorRequestant ) {

        this.doctorRequestant = doctorRequestant;
    }


    public String getUser() {

        return user;
    }


    public void setUser( String user ) {

        this.user = user;
    }


    public String getClinicOrigin() {

        return clinicOrigin;
    }


    public void setClinicOrigin( String clinicOrigin ) {

        this.clinicOrigin = clinicOrigin;
    }


    public String getCityOrigin() {

        return CityOrigin;
    }


    public void setCityOrigin( String cityOrigin ) {

        CityOrigin = cityOrigin;
    }


    public Long getPatientId() {

        return patientId;
    }


    public void setPatientId( Long patientId ) {

        this.patientId = patientId;
    }


    public Date getRequestDate() {

        return requestDate;
    }


    public void setRequestDate( Date requestDate ) {

        this.requestDate = requestDate;
    }


    public Date getAchievementDate() {

        return achievementDate;
    }


    public void setAchievementDate( Date achievementDate ) {

        this.achievementDate = achievementDate;
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

        return "Request{" + "id=" + id + ", medicalRecord=" + medicalRecord + ", medicalRequest=" + medicalRequest + ", sector='" + sector + '\''
            + ", agreement='" + agreement + '\'' + ", doctorRequestant='" + doctorRequestant + '\'' + ", user='" + user + '\'' + ", clinicOrigin='"
            + clinicOrigin + '\'' + ", CityOrigin='" + CityOrigin + '\'' + ", patientId=" + patientId + ", requestDate=" + requestDate + ", achievementDate="
            + achievementDate + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy
            + '\'' + '}';
    }
}
