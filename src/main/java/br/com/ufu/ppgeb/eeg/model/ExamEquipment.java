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
@Table( name = "EXAM_EQUIPMENT" )
public class ExamEquipment {

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @ManyToOne
    @JoinColumn( name = "EXAM_ID", nullable = false )
    private Exam exam;

    @ManyToOne
    @JoinColumn( name = "EQUIPMENT_ID", nullable = false )
    private Equipment equipment;

    @Column( name = "AMOUNT", nullable = false )
    private Long amount;

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
        if ( !( o instanceof ExamEquipment ) )
            return false;
        ExamEquipment that = (ExamEquipment) o;
        return Objects.equals( getExam(), that.getExam() ) && Objects.equals( getEquipment(), that.getEquipment() )
            && Objects.equals( getAmount(), that.getAmount() );
    }


    @Override
    public int hashCode() {

        return Objects.hash( getExam(), getEquipment(), getAmount() );
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


    public Equipment getEquipment() {

        return equipment;
    }


    public void setEquipment( Equipment equipment ) {

        this.equipment = equipment;
    }


    public Long getAmount() {

        return amount;
    }


    public void setAmount( Long amount ) {

        this.amount = amount;
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

        return "ExamEquipment{" + "id=" + id + ", exam=" + exam + ", equipment=" + equipment + ", amount=" + amount + ", createdAt=" + createdAt
            + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}