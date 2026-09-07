package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

  /**
   * Finds activities by exam id paginated.
   *
   * @param examId the exam id
   * @param pageable the pagination information
   * @return the page of activities
   */
  Page<Activity> findByExamId(Long examId, Pageable pageable);

}
