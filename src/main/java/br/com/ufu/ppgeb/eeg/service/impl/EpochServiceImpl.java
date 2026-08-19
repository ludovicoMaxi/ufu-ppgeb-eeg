package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.repository.EpochRepository;
import br.com.ufu.ppgeb.eeg.service.EpochService;
import br.com.ufu.ppgeb.eeg.view.EpochList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of EpochService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class EpochServiceImpl implements EpochService {

  private final EpochRepository epochRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Epoch save(Epoch epoch) {

    Assert.notNull(epoch, "epoch cannot be null.");

    validateEpoch(epoch);

    Epoch saved = epochRepository.save(epoch);
    log.info("Época criada com id={}", saved.getId());
    return saved;
  }

  private void validateEpoch(Epoch epoch) {

    Assert.notNull(epoch, "Epoch cannot be null.");
    Assert.notNull(epoch.getStartTime(), "start time cannot be null.");
    Assert.notNull(epoch.getDuration(), "duration cannot be null.");
    Assert.hasText(epoch.getDescription(), "description cannot be empty.");
  }

  @Override
  @Transactional(readOnly = true)
  public List<Epoch> findAll() {

    return epochRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Epoch findById(Long id) {

    Assert.notNull(id, "id cannot be null.");
    return epochRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Epoch", id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Epoch> findByFilter(Long examId) {

    Assert.notNull(examId, "examId cannot be null.");

    return epochRepository.findByExamId(examId);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, "id cannot be null.");
    if (!epochRepository.existsById(id)) {
      throw new ResourceNotFoundException("Epoch", id);
    }
    epochRepository.deleteById(id);
    log.info("Época removida com id={}", id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<Epoch> updateList(EpochList epochList) {

    Assert.notNull(epochList, "EpochList cannot be null.");
    Assert.notNull(epochList.getExamId(), "ExamId cannot be null.");

    Map<Long, Epoch> oldEpochsById = new HashMap<>();
    for (Epoch oldEpoch : epochRepository.findByExamId(epochList.getExamId())) {
      oldEpochsById.put(oldEpoch.getId(), oldEpoch);
    }

    List<Epoch> currentEpochs = epochList.getEpochs();
    List<Epoch> savedEpochs = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(currentEpochs)) {

      for (Epoch epoch : currentEpochs) {

        if (nonNull(epoch.getExamId())
            && !epoch.getExamId()
                .equals(epochList.getExamId())) {
          throw new IllegalArgumentException(
              epoch + " is not same examId in update=" + epochList.getExamId());
        }

        epoch.setExamId(epochList.getExamId());

        if (nonNull(epoch.getId())) {

          Epoch oldEpoch = oldEpochsById.remove(epoch.getId());
          if (isNull(oldEpoch)) {
            throw new IllegalArgumentException("Epoch with id=" + epoch.getId()
                + " not exist by examID=" + epochList.getExamId());
          }

          if (!epoch.equals(oldEpoch)) {
            validateEpoch(epoch);
            epoch = epochRepository.save(epoch);
          }
        } else {
          validateEpoch(epoch);
          epoch = epochRepository.save(epoch);
        }

        savedEpochs.add(epoch);
      }
    }

    epochRepository.deleteAll(oldEpochsById.values());

    log.info("Épocas do exame atualizadas; examId={}, quantidade={}",
        epochList.getExamId(), savedEpochs.size());
    return savedEpochs;
  }
}
