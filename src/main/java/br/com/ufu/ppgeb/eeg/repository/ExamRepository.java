package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Exam entities.
 */
public interface ExamRepository
    extends JpaRepository<Exam, Long>, ExamRepositoryCustom {

}
