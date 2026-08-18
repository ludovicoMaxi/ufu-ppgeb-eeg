package br.com.ufu.ppgeb.eeg.service.impl;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.repository.EquipmentRepository;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of EquipmentService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class EquipmentServiceImpl
    implements EquipmentService {

  private final EquipmentRepository equipmentRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Equipment> findAll() {

    return equipmentRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Equipment> findByName(String name) {

    return equipmentRepository.findByName(name);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Equipment save(Equipment equipment) {

    Assert.notNull(
        equipment, "equipment cannot be null.");
    Assert.hasText(
        equipment.getName(),
        "equipment name cannot be empty.");

    equipment.setName(equipment.getName().toUpperCase());

    if (equipmentRepository.existsByName(
        equipment.getName())) {
      throw new IllegalArgumentException(
          "Equipamento já cadastrado: "
              + equipment.getName());
    }

    Equipment saved = equipmentRepository.save(equipment);
    log.info("Equipamento criado com id={}", saved.getId());
    return saved;
  }
}
