package br.com.ufu.ppgeb.eeg.repository;


import br.com.ufu.ppgeb.eeg.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.ExamMedicament;

import java.util.List;


/**
 * Created by joaol on 10/01/19.
 */
public interface ExamMedicamentRepository extends JpaRepository< ExamMedicament, Long > {

    List< ExamMedicament > findByExam(Exam exam );

}
