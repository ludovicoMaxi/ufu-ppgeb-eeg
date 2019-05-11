package br.com.ufu.ppgeb.eeg.view;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Epoch;


public class EpochList {

    Long examId;

    List< Epoch > epochs;


    public Long getExamId() {

        return examId;
    }


    public void setExamId( Long examId ) {

        this.examId = examId;
    }


    public List< Epoch > getEpochs() {

        return epochs;
    }


    public void setEpochs( List< Epoch > epochs ) {

        this.epochs = epochs;
    }


    @Override
    public String toString() {

        return "EpochList{" + "examId=" + examId + ", epochs=" + epochs + '}';
    }
}
