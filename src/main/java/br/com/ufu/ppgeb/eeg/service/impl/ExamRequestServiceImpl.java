package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;

import java.util.List;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.repository.ExamRequestRepository;
import br.com.ufu.ppgeb.eeg.service.ExamRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of ExamRequestService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ExamRequestServiceImpl implements ExamRequestService {

  private final ExamRequestRepository examRequestRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ExamRequest save(ExamRequest examRequest) {

    Assert.notNull(examRequest, "examRequest cannot be null.");

    validateExamRequest(examRequest);

    ExamRequest saved = examRequestRepository.save(examRequest);
    log.info("Solicitação de exame criada com id={}", saved.getId());
    return saved;
  }

  private void validateExamRequest(ExamRequest examRequest) {

    Assert.notNull(examRequest, "ExamRequest cannot be null.");
    Assert.notNull(examRequest.getMedicalRecord(), "medicalRecord cannot be empty.");
    Assert.notNull(examRequest.getMedicalRequest(), "medicalRequest cannot be empty.");
    Assert.hasText(examRequest.getSector(), "sector cannot be empty.");
    Assert.hasText(examRequest.getDoctorRequestant(), "doctorRequestant cannot be empty.");
    Assert.hasText(examRequest.getUser(), "user cannot be empty.");
    Assert.notNull(examRequest.getRequestDate(), "requestDate cannot be empty.");
    Assert.notNull(examRequest.getPatient(), "patient cannot be null.");
    Assert.notNull(examRequest.getPatient().getId(), "patient id cannot be null.");
  }

  @Override
  @Transactional(readOnly = true)
  public List<ExamRequest> findAll() {

    return examRequestRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public ExamRequest findById(Long id) {

    Assert.notNull(id, "id cannot be null.");
    return examRequestRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ExamRequest", id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ExamRequest> findByFilter(Long medicalRecord, Long medicalRequest,
      Long patientId, String doctorRequestant) {

    if (StringUtils.isBlank(doctorRequestant)
        && isNull(medicalRequest)
        && isNull(patientId)
        && isNull(medicalRecord)) {
      throw new IllegalArgumentException("Informe pelo menos um campo para consultar!");
    }

    return examRequestRepository.findByFilter(medicalRecord, medicalRequest, patientId,
        doctorRequestant);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, "id cannot be null.");
    if (!examRequestRepository.existsById(id)) {
      throw new ResourceNotFoundException("ExamRequest", id);
    }
    examRequestRepository.deleteById(id);
    log.info("Solicitação de exame removida com id={}", id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ExamRequest update(ExamRequest examRequest) {

    Assert.notNull(examRequest, "examRequest cannot be null.");

    validateExamRequest(examRequest);
    Assert.notNull(examRequest.getId(), "examRequest ID cannot be null.");

    Long examRequestId = examRequest.getId();
    ExamRequest oldExamRequest = examRequestRepository.findById(examRequestId)
        .orElseThrow(() -> new ResourceNotFoundException("ExamRequest", examRequestId));

    if (!oldExamRequest.equals(examRequest)) {

      if (!examRequest.getPatient().getId()
          .equals(oldExamRequest.getPatient().getId())) {
        throw new IllegalArgumentException("Patient ID is different. New=" + examRequest.getPatient().getId()
            + ", Old=" + oldExamRequest.getPatient().getId());
      }

      oldExamRequest.setMedicalRecord(examRequest.getMedicalRecord());
      oldExamRequest.setMedicalRequest(examRequest.getMedicalRequest());
      oldExamRequest.setAchievementDate(examRequest.getAchievementDate());
      oldExamRequest.setAgreement(examRequest.getAgreement());
      oldExamRequest.setCityOrigin(examRequest.getCityOrigin());
      oldExamRequest.setClinicOrigin(examRequest.getClinicOrigin());
      oldExamRequest.setDoctorRequestant(examRequest.getDoctorRequestant());
      oldExamRequest.setRequestDate(examRequest.getRequestDate());
      oldExamRequest.setSector(examRequest.getSector());
      oldExamRequest.setUser(examRequest.getUser());

      examRequest = examRequestRepository.save(oldExamRequest);
      log.info("Solicitação de exame atualizada com id={}", examRequestId);
    }

    return examRequest;
  }
}
