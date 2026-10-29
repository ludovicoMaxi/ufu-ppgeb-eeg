package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for ExamRequest entities.
 */
public interface ExamRequestRepository
    extends JpaRepository<ExamRequest, Long>,
    JpaSpecificationExecutor<ExamRequest> {

}
