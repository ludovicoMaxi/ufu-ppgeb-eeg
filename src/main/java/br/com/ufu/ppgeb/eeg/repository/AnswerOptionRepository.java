package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for AnswerOption entities.
 */
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
}