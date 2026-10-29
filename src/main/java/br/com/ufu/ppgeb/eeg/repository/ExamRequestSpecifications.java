package br.com.ufu.ppgeb.eeg.repository;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for ExamRequest queries.
 */
public final class ExamRequestSpecifications {

  private static final String LIKE_WILDCARD = "%";
  private static final String MEDICAL_RECORD = "medicalRecord";
  private static final String MEDICAL_REQUEST = "medicalRequest";
  private static final String DOCTOR_REQUESTANT = "doctorRequestant";
  private static final String PATIENT = "patient";
  private static final String ID = "id";

  private ExamRequestSpecifications() {
  }

  /**
   * Builds a specification that filters exam requests by the given filters.
   * Blank or null filters are ignored.
   *
   * @param medicalRecord the medical record filter
   * @param medicalRequest the medical request filter
   * @param patientId the patient id filter
   * @param doctorRequestant the doctor requestant filter
   * @return the specification
   */
  public static Specification<ExamRequest> withFilters(Long medicalRecord, Long medicalRequest,
      Long patientId, String doctorRequestant) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (isNotBlank(doctorRequestant)) {
        predicates.add(cb.like(root.get(DOCTOR_REQUESTANT),
            LIKE_WILDCARD + doctorRequestant + LIKE_WILDCARD));
      }
      if (nonNull(medicalRecord)) {
        predicates.add(cb.equal(root.get(MEDICAL_RECORD), medicalRecord));
      }
      if (nonNull(medicalRequest)) {
        predicates.add(cb.equal(root.get(MEDICAL_REQUEST), medicalRequest));
      }
      if (nonNull(patientId)) {
        predicates.add(cb.equal(root.get(PATIENT).get(ID), patientId));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
