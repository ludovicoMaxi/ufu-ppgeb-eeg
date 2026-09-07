package br.com.ufu.ppgeb.eeg.service.impl;

import br.com.ufu.ppgeb.eeg.model.Unit;
import br.com.ufu.ppgeb.eeg.repository.UnitRepository;
import br.com.ufu.ppgeb.eeg.service.UnitService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UnitService.
 */
@Service
@AllArgsConstructor
public class UnitServiceImpl implements UnitService {

  private final UnitRepository unitRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Unit> findAll(Pageable pageable) {

    return unitRepository.findAll(pageable);
  }
}
