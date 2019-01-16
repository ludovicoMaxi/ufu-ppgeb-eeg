package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ufu.ppgeb.eeg.model.Unit;
import br.com.ufu.ppgeb.eeg.repository.UnitRepository;
import br.com.ufu.ppgeb.eeg.service.UnitService;


@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;


    @Override
    public List< Unit > findAll() {

        return unitRepository.findAll();
    }

}
