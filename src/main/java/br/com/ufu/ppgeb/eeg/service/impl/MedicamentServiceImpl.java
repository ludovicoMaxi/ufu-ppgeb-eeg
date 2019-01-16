package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.List;

import br.com.ufu.ppgeb.eeg.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.service.MedicamentService;


@Service
public class MedicamentServiceImpl implements MedicamentService {

    @Autowired
    private MedicamentRepository medicamentRepository;


    @Override
    public List< Medicament > findAll() {

        return medicamentRepository.findAll();
    }

}
