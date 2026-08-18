package br.com.ufu.ppgeb.eeg.service.impl;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Unit;
import br.com.ufu.ppgeb.eeg.repository.UnitRepository;
import br.com.ufu.ppgeb.eeg.service.UnitService;
import lombok.AllArgsConstructor;
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
  public List<Unit> findAll() {

    return unitRepository.findAll();
  }
}
