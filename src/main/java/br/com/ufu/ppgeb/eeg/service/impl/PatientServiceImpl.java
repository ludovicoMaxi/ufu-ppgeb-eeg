package br.com.ufu.ppgeb.eeg.service.impl;

import java.util.List;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.repository.PatientRepository;
import br.com.ufu.ppgeb.eeg.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of PatientService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

  private static final String MSG_PATIENT_NULL = "patient cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "Patient";

  private final PatientRepository patientRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Patient save(Patient patient) {

    Assert.notNull(patient, MSG_PATIENT_NULL);

    validatePatient(patient);

    if (patientRepository.existsByDocumentNumber(patient.getDocumentNumber())) {
      throw new IllegalArgumentException("CPF já foi cadastrado, por favor informe outro.");
    }

    Patient saved = patientRepository.save(patient);
    log.info("Paciente criado com id={}", saved.getId());
    return saved;
  }

  private void validatePatient(Patient patient) {

    Assert.notNull(patient, "Patient cannot be null.");
    Assert.hasText(patient.getName(), "name cannot be empty.");
    Assert.hasText(patient.getDocumentNumber(), "documentNumber cannot be empty.");
    Assert.notNull(patient.getBirthDate(), "birthDate cannot be empty.");
    Assert.notNull(patient.getNationality(), "nationality cannot be null.");

    if (patient.getSex() != 'F'
        && patient.getSex() != 'M') {
      throw new IllegalArgumentException("sex Invalid");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Patient> findAll() {

    return patientRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Patient findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return patientRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Patient> findByFilter(String name, String documentNumber) {

    if (StringUtils.isBlank(name)
        && StringUtils.isBlank(documentNumber)) {
      throw new IllegalArgumentException("Informe pelo menos um campo para consultar!");
    }

    return patientRepository.findByFilter(name, documentNumber);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    if (!patientRepository.existsById(id)) {
      throw new ResourceNotFoundException(RESOURCE_NAME, id);
    }
    patientRepository.deleteById(id);
    log.info("Paciente removido com id={}", id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Patient update(Patient patient) {

    Assert.notNull(patient, MSG_PATIENT_NULL);

    validatePatient(patient);
    Assert.notNull(patient.getId(), "patient ID cannot be null.");

    Long patientId = patient.getId();
    Patient oldPatient = patientRepository.findById(patientId)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, patientId));

    if (!oldPatient.equals(patient)) {

      if (!oldPatient.getDocumentNumber()
          .equals(patient.getDocumentNumber())) {
        throw new IllegalArgumentException("CPF/CNPJ está divergente.");
      }

      oldPatient.setName(patient.getName());
      oldPatient.setSex(patient.getSex());
      oldPatient.setBirthDate(patient.getBirthDate());
      oldPatient.setNationality(patient.getNationality());
      oldPatient.setCivilStatus(patient.getCivilStatus());
      oldPatient.setJob(patient.getJob());

      patient = patientRepository.save(oldPatient);
      log.info("Paciente atualizado com id={}", patientId);
    }

    return patient;
  }
}
