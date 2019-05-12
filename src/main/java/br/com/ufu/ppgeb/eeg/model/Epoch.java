package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table( name = "EPOCH" )
public class Epoch {

    @Id
    @SequenceGenerator( name = "EPOCH_SQ", sequenceName = "EPOCH_SQ", allocationSize = 1 )
    @GeneratedValue( generator = "EPOCH_SQ", strategy = GenerationType.SEQUENCE )
    private Long id;

    @Column( name = "EXAM_ID", nullable = false )
    private Long examId;

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
        if ( !( o instanceof Epoch ) )
            return false;
        Epoch epoch = (Epoch) o;
        return Objects.equals( getId(), epoch.getId() ) && //
            Objects.equals( getExamId(), epoch.getExamId() ) && //
            Objects.equals( getStartTime(), epoch.getStartTime() ) && //
            Objects.equals( getDuration(), epoch.getDuration() ) && //
            Objects.equals( getDescription(), epoch.getDescription() );
    }


    @Override
    public int hashCode() {

        return Objects
            .hash( getId(), getExamId(), getStartTime(), getDuration(), getDescription(), getCreatedAt(), getCreatedBy(), getUpdatedAt(), getUpdatedBy() );
    }


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public Long getExamId() {

        return examId;
    }


    public void setExamId( Long examId ) {

        this.examId = examId;
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

        return "Epoch{" + "id=" + id + ", examId=" + examId + ", startTime=" + startTime + ", duration=" + duration + ", description=" + description
            + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\'' + ", updatedAt=" + updatedAt + ", updatedBy='" + updatedBy + '\'' + '}';
    }
}
