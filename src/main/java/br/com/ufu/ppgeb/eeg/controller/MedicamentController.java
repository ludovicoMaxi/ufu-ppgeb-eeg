package br.com.ufu.ppgeb.eeg.controller;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;
import java.util.List;
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
@RequestMapping("/api/medicament")
@AllArgsConstructor
public class MedicamentController {

  private static final Logger logger =
      LoggerFactory.getLogger(MedicamentController.class);

  private final MedicamentService medicamentService;

  /**
   * Lists all medicaments.
   *
   * @return the list of medicaments
   */
  @GetMapping
  public List<Medicament> list() {

    logger.info("Consultando medicamentos");
    return medicamentService.findAll();
  }
}
