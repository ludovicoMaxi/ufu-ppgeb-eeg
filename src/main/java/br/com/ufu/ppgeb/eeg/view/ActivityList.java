package br.com.ufu.ppgeb.eeg.view;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Activity;


public class ActivityList {

    Long examId;

    List< Activity > activities;


    public Long getExamId() {

        return examId;
    }


    public void setExamId( Long examId ) {

        this.examId = examId;
    }


    public List< Activity > getActivities() {

        return activities;
    }


    public void setActivities( List< Activity > activities ) {

        this.activities = activities;
    }


    @Override
    public String toString() {

        return "ActivityList{" + "examId=" + examId + ", activities=" + activities + '}';
    }
}
