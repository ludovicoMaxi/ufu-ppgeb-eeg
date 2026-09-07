package br.com.ufu.ppgeb.eeg.controller;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.UnitResponse;
import br.com.ufu.ppgeb.eeg.mapper.UnitMapper;
import br.com.ufu.ppgeb.eeg.service.UnitService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for unit operations.
 */
@RestController
@RequestMapping(ApiPaths.UNIT)
@AllArgsConstructor
public class UnitController {

  private static final Logger logger = LoggerFactory.getLogger(UnitController.class);

  private final UnitService unitService;

  /**
   * Lists all units.
   *
   * @param pageable the pagination information
   * @return the page of units
   */
  @GetMapping
  public Page<UnitResponse> list(Pageable pageable) {

    logger.info("Consultando unidades");
    return unitService.findAll(pageable).map(UnitMapper::toResponse);
  }
}
