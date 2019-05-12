package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ufu.ppgeb.eeg.utils.CompareDate;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
@Table( name = "EXAM_REQUEST" )
public class ExamRequest {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator( name = "EXAM_REQUEST_SQ", sequenceName = "EXAM_REQUEST_SQ", allocationSize = 1 )
    @GeneratedValue( generator = "EXAM_REQUEST_SQ", strategy = GenerationType.SEQUENCE )
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

    @Column( name = "USER_REQUEST" )
    private String user;

    @Column( name = "CLINIC_ORIGIN" )
    private String clinicOrigin;

    @Column( name = "CITY_ORIGIN" )
    private String cityOrigin;

    @ManyToOne
    @JoinColumn( name = "PATIENT_ID", nullable = false )
    private Patient patient;

    @Column( name = "REQUEST_DATE" )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
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
        if ( !( o instanceof ExamRequest ) )
            return false;
        ExamRequest examRequest = (ExamRequest) o;
        return Objects.equals( getMedicalRecord(), examRequest.getMedicalRecord() ) && //
            Objects.equals( getMedicalRequest(), examRequest.getMedicalRequest() ) && //
            Objects.equals( getSector(), examRequest.getSector() ) && //
            Objects.equals( getAgreement(), examRequest.getAgreement() ) && //
            Objects.equals( getDoctorRequestant(), examRequest.getDoctorRequestant() ) && //
            Objects.equals( getUser(), examRequest.getUser() ) && //
            Objects.equals( getClinicOrigin(), examRequest.getClinicOrigin() ) && //
            Objects.equals( getCityOrigin(), examRequest.getCityOrigin() ) && //
            Objects.equals( getPatient(), examRequest.getPatient() ) && //
            CompareDate.compareDates( getRequestDate(), examRequest.getRequestDate() ) && //
            CompareDate.compareDates( getAchievementDate(), examRequest.getAchievementDate() );
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
            getPatient(),
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

        return cityOrigin;
    }


    public void setCityOrigin( String cityOrigin ) {

        this.cityOrigin = cityOrigin;
    }


    public Patient getPatient() {

        return patient;
    }


    public void setPatient( Patient patient ) {

        this.patient = patient;
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

        return "ExamRequest{" + "id=" + id + ", medicalRecord=" + medicalRecord + ", medicalRequest=" + medicalRequest + ", sector='" + sector + '\''
            + ", agreement='" + agreement + '\'' + ", doctorRequestant='" + doctorRequestant + '\'' + ", user='" + user + '\'' + ", clinicOrigin='"
            + clinicOrigin + '\'' + ", cityOrigin='" + cityOrigin + '\'' + ", patient=" + patient + ", requestDate=" + requestDate + ", achievementDate="
            + achievementDate + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy
            + '\'' + '}';
    }
}
