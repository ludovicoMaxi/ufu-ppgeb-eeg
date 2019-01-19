package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.service.EpochService;
import br.com.ufu.ppgeb.eeg.view.EpochList;


/**
 * Created by joaol on 10/01/18.
 */
@Controller
@RequestMapping( "/api/epoch" )
public class EpochController {

    private static final Logger logger = LogManager.getLogger( EpochController.class );

    @Autowired
    private EpochService epochService;


    @GetMapping
    @ResponseBody
    public List< Epoch > list( @RequestParam( value = "examId", required = true ) Long examId

    ) {

        logger.info( "Parametros{ examId=" + examId + " }" );
        return epochService.findByFilter( examId );

    }


    @GetMapping( "/{id}" )
    public @ResponseBody Epoch findById( @PathVariable( value = "id" ) Long id ) {

        return this.epochService.findById( id );
    }


    @PostMapping
    public @ResponseBody Epoch save( @RequestBody Epoch epoch ) {

        logger.info( "Saving " + epoch );
        return this.epochService.save( epoch );
    }


    @PutMapping
    public @ResponseBody EpochList updateList( @RequestBody EpochList epochList )
        throws Exception {

        logger.info( "Updating " + epochList );
        List< Epoch > epoches = this.epochService.updateList( epochList.getEpochList() );
        EpochList epochesReturn = new EpochList();
        epochesReturn.setEpochList( epoches );
        return epochesReturn;
    }
}
