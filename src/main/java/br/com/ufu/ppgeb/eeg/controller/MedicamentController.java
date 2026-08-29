package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.MedicamentResponse;
import br.com.ufu.ppgeb.eeg.mapper.MedicamentMapper;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
   * @return the list of medicaments
   */
  @GetMapping
  public List<MedicamentResponse> list() {

    logger.info("Consultando medicamentos");
    return Optional.ofNullable(medicamentService.findAll())
        .orElse(List.of())
        .stream()
        .map(MedicamentMapper::toResponse)
        .toList();
  }
}
