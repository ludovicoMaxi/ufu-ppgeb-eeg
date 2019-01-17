package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.service.EquipmentService;


/**
 * Created by joaol on 10/01/19.
 */
@Controller
@RequestMapping( "/api/equipment" )
public class EquipmentController {

    private static final Logger logger = LogManager.getLogger( EquipmentController.class );

    @Autowired
    private EquipmentService equipmentService;


    @GetMapping
    @ResponseBody
    public List< Equipment > list() {

        return equipmentService.findAll();

    }

}
