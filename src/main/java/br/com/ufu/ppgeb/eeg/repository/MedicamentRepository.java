package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Medicament;


/**
 * Created by joaol on 10/01/19.
 */
public interface MedicamentRepository extends JpaRepository< Medicament, Long > {

    List< Medicament > findByName( String name );

}
