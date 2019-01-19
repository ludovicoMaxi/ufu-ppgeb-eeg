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

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.service.ExamService;


/**
 * Created by joaol on 16/09/18.
 */
@Controller
@RequestMapping( "/api/exam" )
public class ExamController {

    private static final Logger logger = LogManager.getLogger( ExamController.class );

    @Autowired
    private ExamService examService;


    @GetMapping
    @ResponseBody
    public List< Exam > list(
        @RequestParam( value = "id", required = false ) Long id,
        @RequestParam( value = "bed", required = false ) String bed,
        @RequestParam( value = "patientId", required = false ) Long patientId,
        @RequestParam( value = "examRequestId", required = false ) Long examRequestId

    ) {

        logger.info( "Parametros{ id=" + id + ", bed=" + bed + ", patientId=" + patientId + ", examRequestId=" + examRequestId + " }" );
        return examService.findByFilter( id, bed, patientId, examRequestId );

    }


    @GetMapping( "/{id}" )
    public @ResponseBody Exam findById( @PathVariable( value = "id" ) Long id ) {

        return this.examService.findById( id );
    }


    @PostMapping
    public @ResponseBody Exam save( @RequestBody Exam exam ) {

        logger.info( "Saving " + exam );
        return this.examService.save( exam );
    }


    @PutMapping
    public @ResponseBody Exam update( @RequestBody Exam exam )
        throws Exception {

        logger.info( "Updating " + exam );
        return this.examService.update( exam );
    }


    @PutMapping( "/medicament" )
    public @ResponseBody Exam updateExamMedicament( @RequestBody Exam examMedicamentList )
        throws Exception {

        logger.info( "Updating exam-medicament" + examMedicamentList );
        return this.examService.updateExamMedicament( examMedicamentList );
    }


    @PutMapping( "/equipment" )
    public @ResponseBody Exam updateExamEquipment( @RequestBody Exam examEquipmentList )
        throws Exception {

        logger.info( "Updating exam-equipment" + examEquipmentList );
        return this.examService.updateExamEquipment( examEquipmentList );
    }
}
