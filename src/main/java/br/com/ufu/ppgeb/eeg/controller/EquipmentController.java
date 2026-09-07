package br.com.ufu.ppgeb.eeg.controller;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.EquipmentResponse;
import br.com.ufu.ppgeb.eeg.mapper.EquipmentMapper;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for equipment operations.
 */
@RestController
@RequestMapping(ApiPaths.EQUIPMENT)
@AllArgsConstructor
public class EquipmentController {

  private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);

  private final EquipmentService equipmentService;

  /**
   * Lists all equipment.
   *
   * @param pageable the pagination information
   * @return the page of equipment
   */
  @GetMapping
  public Page<EquipmentResponse> list(Pageable pageable) {

    logger.info("Consultando equipamentos");
    return equipmentService.findAll(pageable).map(EquipmentMapper::toResponse);
  }
}
