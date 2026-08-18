package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Unit;
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
@RequestMapping("/api/unit")
@AllArgsConstructor
public class UnitController {

  private static final Logger logger =
      LoggerFactory.getLogger(UnitController.class);

  private final UnitService unitService;

  /**
   * Lists all units.
   *
   * @return the list of units
   */
  @GetMapping
  public List<Unit> list() {

    logger.info("Consultando unidades");
    return unitService.findAll();
  }
}
