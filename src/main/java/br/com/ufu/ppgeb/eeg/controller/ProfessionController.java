package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ProfessionResponse;
import br.com.ufu.ppgeb.eeg.mapper.ProfessionMapper;
import br.com.ufu.ppgeb.eeg.service.ProfessionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for profession operations.
 */
@RestController
@RequestMapping(ApiPaths.PROFESSION)
@AllArgsConstructor
public class ProfessionController {

  private static final Logger logger = LoggerFactory.getLogger(ProfessionController.class);

  private final ProfessionService professionService;

  /**
   * Lists all professions ordered by name.
   *
   * @return the professions
   */
  @GetMapping
  public List<ProfessionResponse> list() {

    logger.info("Consultando profissões");
    return professionService.findAll().stream()
        .map(ProfessionMapper::toResponse)
        .toList();
  }
}
