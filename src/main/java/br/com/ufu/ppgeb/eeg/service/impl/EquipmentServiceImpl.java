package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.repository.EquipmentRepository;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;


@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;


    @Override
    public List< Equipment > findAll() {

        return equipmentRepository.findAll();
    }


    @Override
    public List< Equipment > findByName( String name ) {

        return equipmentRepository.findByName( name );
    }


    @Override
    public Equipment save( Equipment equipment ) {

        equipment.setName( equipment.getName().toUpperCase() );
        List< Equipment > equipmentList = findByName( equipment.getName() );

        if ( equipmentList != null && equipmentList.size() > 0 ) {
            throw new IllegalArgumentException( "Equipamento já cadastrado: " + equipment.getName() );
        } else {
            Equipment equipmentSave = new Equipment();
            equipmentSave.setCreatedAt( new Date() );
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if ( auth != null ) {
                equipmentSave.setCreatedBy( auth.getName() );
            }
            equipmentSave.setName( equipment.getName() );
            equipmentSave.setDescription( equipment.getDescription() );
            equipment = equipmentRepository.save( equipmentSave );
        }
        return equipment;
    }

}
