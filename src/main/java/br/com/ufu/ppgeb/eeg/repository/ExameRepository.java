package br.com.ufu.ppgeb.eeg.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Exame;


/**
 * Created by joaol on 08/09/17.
 */
public interface ExameRepository extends JpaRepository< Exame, Long > {
}
