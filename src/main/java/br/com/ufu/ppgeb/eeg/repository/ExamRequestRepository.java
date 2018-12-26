package br.com.ufu.ppgeb.eeg.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;


/**
 * Created by joaol on 24/12/18.
 */
public interface ExamRequestRepository extends JpaRepository< ExamRequest, Long >, ExamRequestRepositoryCustom {

}
