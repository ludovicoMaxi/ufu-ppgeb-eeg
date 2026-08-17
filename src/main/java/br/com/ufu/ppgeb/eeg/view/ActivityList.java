package br.com.ufu.ppgeb.eeg.view;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

import br.com.ufu.ppgeb.eeg.model.Activity;


@Getter
@Setter
public class ActivityList {

    private Long examId;

    private List< Activity > activities;


    @Override
    public String toString() {

        return "ActivityList{" + "examId=" + examId + ", activities=" + activities + '}';
    }
}
