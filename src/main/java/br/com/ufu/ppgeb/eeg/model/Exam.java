package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.ufu.ppgeb.eeg.utils.CompareDate;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
@Table( name = "EXAM" )
public class Exam {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator( name = "EXAM_SQ", sequenceName = "EXAM_SQ", allocationSize = 1 )
    @GeneratedValue( generator = "EXAM_SQ", strategy = GenerationType.SEQUENCE )
    private Long id;

    @OneToOne
    @JoinColumn( name = "EXAM_REQUEST_ID" )
    private ExamRequest examRequest;

    @ManyToOne
    @JoinColumn( name = "PATIENT_ID", nullable = false )
    private Patient patient;

    @Column( name = "ACHIEVEMENT_DATE", nullable = false )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date achievementDate;

    @Column( name = "MEDICAL_REPORT", length = 256 )
    private String medicalReport;

    @Column( name = "CONCLUSION", length = 256 )
    private String conclusion;

    @Column( name = "BED", length = 256 )
    private String bed;

    @Column( name = "HEIGHT" )
    private Long height;

    @Column( name = "WEIGHT" )
    private Double weight;

    @Column( name = "CLINICAL_DATA", length = 256 )
    private String clinicalData;

    @OneToMany( mappedBy = "exam" )
    @JsonManagedReference
    private List< ExamMedicament > examMedicaments;

    @OneToMany( mappedBy = "exam" )
    @JsonManagedReference
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


    public Exam() {

    }


    public Exam( Long id ) {

        this.id = id;
    }


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( !( o instanceof Exam ) )
            return false;
        Exam exam = (Exam) o;
        return Objects.equals( getId(), exam.getId() ) && //
            Objects.equals( getExamRequest(), exam.getExamRequest() ) && //
            Objects.equals( getPatient(), exam.getPatient() ) && //
            CompareDate.compareDates( getAchievementDate(), exam.getAchievementDate() ) && //
            Objects.equals( getMedicalReport(), exam.getMedicalReport() ) && //
            Objects.equals( getConclusion(), exam.getConclusion() ) && //
            Objects.equals( getBed(), exam.getBed() ) && //
            Objects.equals( getHeight(), exam.getHeight() ) && //
            Objects.equals( getWeight(), exam.getWeight() ) && //
            Objects.equals( getClinicalData(), exam.getClinicalData() ) && //
            Objects.equals( getCreatedBy(), exam.getCreatedBy() ) && //
            Objects.equals( getUpdatedBy(), exam.getUpdatedBy() );
    }


    @Override
    public int hashCode() {

        return Objects.hash(
            getId(),
            getExamRequest(),
            getPatient(),
            getAchievementDate(),
            getMedicalReport(),
            getConclusion(),
            getBed(),
            getHeight(),
            getWeight(),
            getClinicalData(),
            getExamMedicaments(),
            getExamEquipments(),
            getCreatedBy(),
            getUpdatedBy() );
    }


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public ExamRequest getExamRequest() {

        return examRequest;
    }


    public void setExamRequest( ExamRequest examRequest ) {

        this.examRequest = examRequest;
    }


    public Patient getPatient() {

        return patient;
    }


    public void setPatient( Patient patient ) {

        this.patient = patient;
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


    public Long getHeight() {

        return height;
    }


    public void setHeight( Long height ) {

        this.height = height;
    }


    public Double getWeight() {

        return weight;
    }


    public void setWeight( Double weight ) {

        this.weight = weight;
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

        return "Exam{" + "id=" + id + ", examRequest=" + examRequest + ", patient=" + patient + ", achievementDate=" + achievementDate + ", medicalReport='"
            + medicalReport + '\'' + ", conclusion='" + conclusion + '\'' + ", bed='" + bed + '\'' + ", height=" + height + ", weight=" + weight
            + ", clinicalData='" + clinicalData + '\'' + ", examMedicaments=" + examMedicaments + ", examEquipments=" + examEquipments + ", createdAt="
            + createdAt + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
