package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.model.Exam;


/**
 * Created by joaol on 10/01/19.
 */
public interface ActivityRepository extends JpaRepository< Activity, Long > {

    List< Activity > findByExam( Exam exam );

}
