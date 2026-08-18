package br.com.ufu.ppgeb.eeg.service.impl;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.repository.MedicamentRepository;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of MedicamentService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class MedicamentServiceImpl
    implements MedicamentService {

  private final MedicamentRepository medicamentRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Medicament> findAll() {

    return medicamentRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Medicament> findByName(String name) {

    return medicamentRepository.findByName(name);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Medicament save(Medicament medicament) {

    Assert.notNull(medicament, "medicament cannot be null.");
    Assert.hasText(medicament.getName(),
        "medicament name cannot be empty.");

    medicament.setName(medicament.getName().toUpperCase());

    if (medicamentRepository.existsByName(medicament.getName())) {
      throw new IllegalArgumentException("Medicamento já cadastrado: "
              + medicament.getName());
    }

    Medicament saved = medicamentRepository.save(medicament);
    log.info("Medicamento criado com id={}", saved.getId());
    return saved;
  }
}
