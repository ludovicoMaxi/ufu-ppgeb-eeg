package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
@Table( name = "EXAM" )
public class Exam {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @Column( name = "REQUEST_ID" )
    private Long requestId;

    @Column( name = "PATIENT_ID", nullable = false )
    private Long patientId;

    @Column( name = "ACHIEVEMENT_DATE" )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date achievementDate;

    @Column( name = "MEDICAL_REPORT", length = 256, nullable = false )
    private String medicalReport;

    @Column( name = "CONCLUSION", length = 256, nullable = false )
    private String conclusion;

    @Column( name = "BED", length = 256 )
    private String bed;

    @Column( name = "CLINICAL_DATA", length = 256, nullable = false )
    private String clinicalData;

    @OneToMany( mappedBy = "exam" )
    private List< ExamMedicament > examMedicaments;

    @OneToMany( mappedBy = "exam" )
    private List< ExamEquipment > examEquipments;

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
        if ( !( o instanceof Exam ) )
            return false;
        Exam exam = (Exam) o;
        return Objects.equals( getRequestId(), exam.getRequestId() ) && Objects.equals( getPatientId(), exam.getPatientId() )
            && Objects.equals( getAchievementDate(), exam.getAchievementDate() ) && Objects.equals( getMedicalReport(), exam.getMedicalReport() )
            && Objects.equals( getConclusion(), exam.getConclusion() ) && Objects.equals( getBed(), exam.getBed() )
            && Objects.equals( getClinicalData(), exam.getClinicalData() ) && Objects.equals( getExamMedicaments(), exam.getExamMedicaments() )
            && Objects.equals( getExamEquipments(), exam.getExamEquipments() );
    }


    @Override
    public int hashCode() {

        return Objects.hash(
            getRequestId(),
            getPatientId(),
            getAchievementDate(),
            getMedicalReport(),
            getConclusion(),
            getBed(),
            getClinicalData(),
            getExamMedicaments(),
            getExamEquipments() );
    }


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public Long getRequestId() {

        return requestId;
    }


    public void setRequestId( Long requestId ) {

        this.requestId = requestId;
    }


    public Long getPatientId() {

        return patientId;
    }


    public void setPatientId( Long patientId ) {

        this.patientId = patientId;
    }


    public Date getAchievementDate() {

        return achievementDate;
    }


    public void setAchievementDate( Date achievementDate ) {

        this.achievementDate = achievementDate;
    }


    public String getMedicalReport() {

        return medicalReport;
    }


    public void setMedicalReport( String medicalReport ) {

        this.medicalReport = medicalReport;
    }


    public String getConclusion() {

        return conclusion;
    }


    public void setConclusion( String conclusion ) {

        this.conclusion = conclusion;
    }


    public String getBed() {

        return bed;
    }


    public void setBed( String bed ) {

        this.bed = bed;
    }


    public String getClinicalData() {

        return clinicalData;
    }


    public void setClinicalData( String clinicalData ) {

        this.clinicalData = clinicalData;
    }


    public List< ExamMedicament > getExamMedicaments() {

        return examMedicaments;
    }


    public void setExamMedicaments( List< ExamMedicament > examMedicaments ) {

        this.examMedicaments = examMedicaments;
    }


    public List< ExamEquipment > getExamEquipments() {

        return examEquipments;
    }


    public void setExamEquipments( List< ExamEquipment > examEquipments ) {

        this.examEquipments = examEquipments;
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

        return "Exam{" + "id=" + id + ", requestId=" + requestId + ", patientId=" + patientId + ", achievementDate=" + achievementDate + ", medicalReport='"
            + medicalReport + '\'' + ", conclusion='" + conclusion + '\'' + ", bed='" + bed + '\'' + ", clinicalData='" + clinicalData + '\''
            + ", examMedicaments=" + examMedicaments + ", examEquipments=" + examEquipments + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\''
            + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
