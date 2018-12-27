package br.com.ufu.ppgeb.eeg.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Exam;


/**
 * Created by joaol on 24/12/18.
 */
public interface ExamRepository extends JpaRepository< Exam, Long >, ExamRepositoryCustom {

}
