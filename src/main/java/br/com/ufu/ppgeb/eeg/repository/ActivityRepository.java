package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Activity entities.
 */
public interface ActivityRepository
    extends JpaRepository<Activity, Long> {

  /**
   * Finds activities by exam id.
   *
   * @param examId the exam id
   * @return the list of activities
   */
  List<Activity> findByExamId(Long examId);

}
