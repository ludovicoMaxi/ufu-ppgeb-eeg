package br.com.ufu.ppgeb.eeg.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Contact;


/**
 * Created by joaol on 13/09/18.
 */
public interface ContactRepository extends JpaRepository< Contact, Long >, ContactRepositoryCustom {
}
