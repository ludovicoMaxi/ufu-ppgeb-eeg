package br.com.ufu.ppgeb.eeg.controller;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.MedicamentResponse;
import br.com.ufu.ppgeb.eeg.mapper.MedicamentMapper;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for medicament operations.
 */
@RestController
@RequestMapping(ApiPaths.MEDICAMENT)
@AllArgsConstructor
public class MedicamentController {

  private static final Logger logger = LoggerFactory.getLogger(MedicamentController.class);

  private final MedicamentService medicamentService;

  /**
   * Lists all medicaments.
   *
   * @param pageable the pagination information
   * @return the page of medicaments
   */
  @GetMapping
  public Page<MedicamentResponse> list(Pageable pageable) {

    logger.info("Consultando medicamentos");
    return medicamentService.findAll(pageable).map(MedicamentMapper::toResponse);
  }
}
