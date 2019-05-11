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
@Table( name = "ACTIVITY" )
public class Activity {

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    @ManyToOne
    @JoinColumn( name = "EXAM_ID", nullable = false )
    private Exam exam;

    @Column( name = "START_TIME", nullable = false )
    private Long startTime;

    @Column( name = "DURATION", nullable = false )
    private Long duration;

    @Column( name = "DESCRIPTION", length = 1024, nullable = false )
    private String description;

    @Column( name = "CREATED_AT", nullable = false )
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss" )
    private Date createdAt;

    @Column( name = "CREATED_BY", length = 20, nullable = false )
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
        if ( !( o instanceof Activity ) )
            return false;
        Activity epoch = (Activity) o;
        return Objects.equals( getId(), epoch.getId() ) && //
            Objects.equals( getExam(), epoch.getExam() ) && //
            Objects.equals( getStartTime(), epoch.getStartTime() ) && //
            Objects.equals( getDuration(), epoch.getDuration() ) && //
            Objects.equals( getDescription(), epoch.getDescription() ) && //
            Objects.equals( getCreatedAt(), epoch.getCreatedAt() ) && //
            Objects.equals( getCreatedBy(), epoch.getCreatedBy() ) && //
            Objects.equals( getUpdatedAt(), epoch.getUpdatedAt() ) && //
            Objects.equals( getUpdatedBy(), epoch.getUpdatedBy() );
    }


    @Override
    public int hashCode() {

        return Objects
            .hash( getId(), getExam(), getStartTime(), getDuration(), getDescription(), getCreatedAt(), getCreatedBy(), getUpdatedAt(), getUpdatedBy() );
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


    public Long getStartTime() {

        return startTime;
    }


    public void setStartTime( Long startTime ) {

        this.startTime = startTime;
    }


    public Long getDuration() {

        return duration;
    }


    public void setDuration( Long duration ) {

        this.duration = duration;
    }


    public String getDescription() {

        return description;
    }


    public void setDescription( String description ) {

        this.description = description;
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

        return "Activity{" + "id=" + id + ", exam=" + exam + ", startTime=" + startTime + ", duration=" + duration + ", description='" + description + '\''
            + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
