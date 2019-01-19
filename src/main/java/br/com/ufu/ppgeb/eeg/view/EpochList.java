package br.com.ufu.ppgeb.eeg.view;


import br.com.ufu.ppgeb.eeg.model.Epoch;

import java.util.List;


public class EpochList {

    List< Epoch > epochList;


    public List< Epoch > getEpochList() {

        return epochList;
    }


    public void setEpochList( List< Epoch > epochList ) {

        this.epochList = epochList;
    }


    @Override
    public String toString() {

        return "EpochList{" + "epochList=" + epochList + '}';
    }
}
