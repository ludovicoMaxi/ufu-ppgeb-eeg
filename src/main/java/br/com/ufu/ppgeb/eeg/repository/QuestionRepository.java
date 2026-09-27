package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for Question entities.
 */
public interface QuestionRepository
    extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
}