package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.UnitResponse;
import br.com.ufu.ppgeb.eeg.mapper.UnitMapper;
import br.com.ufu.ppgeb.eeg.service.UnitService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
   * @return the list of units
   */
  @GetMapping
  public List<UnitResponse> list() {

    logger.info("Consultando unidades");
    return Optional.ofNullable(unitService.findAll())
        .orElse(List.of())
        .stream()
        .map(UnitMapper::toResponse)
        .toList();
  }
}
