package br.com.ufu.ppgeb.eeg.service.impl;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Profession;
import br.com.ufu.ppgeb.eeg.repository.ProfessionRepository;
import br.com.ufu.ppgeb.eeg.service.ProfessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ProfessionService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProfessionServiceImpl implements ProfessionService {

  private final ProfessionRepository professionRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Profession> findAll() {

    return professionRepository.findAllByOrderByNameAsc();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void registerIfAbsent(String name) {

    String normalized = StringUtils.upperCase(StringUtils.trimToNull(name));
    if (normalized == null) {
      return;
    }
    if (professionRepository.findByName(normalized).isEmpty()) {
      professionRepository.save(Profession.builder().name(normalized).build());
      log.info("Profissão cadastrada no catálogo: {}", normalized);
    }
  }
}
