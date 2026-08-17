package br.com.ufu.ppgeb.eeg.view;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

import br.com.ufu.ppgeb.eeg.model.Epoch;


@Getter
@Setter
public class EpochList {

    private Long examId;

    private List< Epoch > epochs;


    @Override
    public String toString() {

        return "EpochList{" + "examId=" + examId + ", epochs=" + epochs + '}';
    }
}
