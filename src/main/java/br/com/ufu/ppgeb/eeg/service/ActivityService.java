package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.view.ActivityList;


public interface ActivityService {

    Activity save( Activity activity );


    Activity findById( Long id );


    List< Activity > findByFilter( Long examId );


    List< Activity > findAll();


    void delete( Long id );


    List< Activity > updateList( ActivityList activityList );

}
