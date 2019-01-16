package br.com.ufu.ppgeb.eeg.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Unit;


/**
 * Created by joaol on 10/01/19.
 */
public interface UnitRepository extends JpaRepository< Unit, Long > {

}
