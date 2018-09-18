package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Contact;


/**
 * Created by joaol on 17/09/18.
 */
public interface ContactRepositoryCustom {

    List< Contact > findByFilter( Long objectType, Long objectId );

}
