package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;


@RestController
@RequestMapping( "/api/equipment" )
@AllArgsConstructor
public class EquipmentController {

    private static final Logger logger = LoggerFactory.getLogger( EquipmentController.class );

    private final EquipmentService equipmentService;


    @GetMapping
    public List< Equipment > list() {

        logger.info( "Consultando equipamentos" );
        return equipmentService.findAll();
    }
}
