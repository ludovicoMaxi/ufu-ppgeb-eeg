package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table( name = "EXAM_MEDICAMENT" )
public class ExamMedicament {

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @ManyToOne
    @JoinColumn( name = "EXAM_ID", nullable = false )
    private Exam exam;

    @ManyToOne
    @JoinColumn( name = "MEDICAMENT_ID", nullable = false )
    private Medicament medicament;

    @Column( name = "AMOUNT", nullable = false )
    private Long amount;

    @ManyToOne
    @JoinColumn( name = "UNIT_ID", nullable = false )
    private Unit unit;

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
        if ( !( o instanceof ExamMedicament ) )
            return false;
        ExamMedicament that = (ExamMedicament) o;
        return Objects.equals( getExam(), that.getExam() ) && Objects.equals( getMedicament(), that.getMedicament() )
            && Objects.equals( getAmount(), that.getAmount() ) && Objects.equals( getUnit(), that.getUnit() );
    }


    @Override
    public int hashCode() {

        return Objects.hash( getExam(), getMedicament(), getAmount(), getUnit() );
    }


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public Exam getExam() {

        return exam;
    }


    public void setExam( Exam exam ) {

        this.exam = exam;
    }


    public Medicament getMedicament() {

        return medicament;
    }


    public void setMedicament( Medicament medicament ) {

        this.medicament = medicament;
    }


    public Long getAmount() {

        return amount;
    }


    public void setAmount( Long amount ) {

        this.amount = amount;
    }


    public Unit getUnit() {

        return unit;
    }


    public void setUnit( Unit unit ) {

        this.unit = unit;
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

        return "ExamMedicament{" + "id=" + id + ", exam=" + exam + ", medicament=" + medicament + ", amount=" + amount + ", unit=" + unit + ", createdAt="
            + createdAt + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
