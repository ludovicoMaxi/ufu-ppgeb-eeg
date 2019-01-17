package br.com.ufu.ppgeb.eeg.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;


/**
 * Created by joaol on 10/01/19.
 */
public interface ExamEquipmentRepository extends JpaRepository< ExamEquipment, Long > {

    List< ExamEquipment > findByExam( Exam exam );

}
