package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ExamRequest entities.
 */
public interface ExamRequestRepository
    extends JpaRepository<ExamRequest, Long>,
    ExamRequestRepositoryCustom {

}
