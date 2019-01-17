package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Equipment;


/**
 * Created by joaol on 10/01/19.
 */
public interface EquipmentRepository extends JpaRepository< Equipment, Long > {

    List< Equipment > findByName( String name );

}
