package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.EquipmentResponse;
import br.com.ufu.ppgeb.eeg.mapper.EquipmentMapper;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
   * @return the list of equipment
   */
  @GetMapping
  public List<EquipmentResponse> list() {

    logger.info("Consultando equipamentos");
    return Optional.ofNullable(equipmentService.findAll())
        .orElse(List.of())
        .stream()
        .map(EquipmentMapper::toResponse)
        .toList();
  }
}
