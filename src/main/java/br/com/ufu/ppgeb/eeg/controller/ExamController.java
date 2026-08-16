package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.service.ExamService;


@RestController
@RequestMapping( "/api/exam" )
@AllArgsConstructor
public class ExamController {

    private static final Logger logger = LoggerFactory.getLogger( ExamController.class );

    private final ExamService examService;


    @GetMapping
    public List< Exam > list( @RequestParam( value = "id", required = false ) Long id,
                              @RequestParam( value = "bed", required = false ) String bed,
                              @RequestParam( value = "patientId", required = false ) Long patientId,
                              @RequestParam( value = "examRequestId", required = false ) Long examRequestId ) {

        logger.info( "Consultando exames; id={}, bedInformado={}, patientId={}, examRequestId={}", id, bed != null && !bed.isBlank(), patientId,
            examRequestId );
        return examService.findByFilter( id, bed, patientId, examRequestId );
    }


    @GetMapping( "/{id}" )
    public Exam findById( @PathVariable( value = "id" ) Long id ) {

        logger.info( "Consultando exame id={}", id );
        return examService.findById( id );
    }


    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Exam save( @RequestBody Exam exam ) {

        logger.info( "Recebendo criação de exame" );
        return examService.save( exam );
    }


    @PutMapping
    public Exam update( @RequestBody Exam exam ) {

        logger.info( "Recebendo atualização de exame id={}", exam.getId() );
        return examService.update( exam );
    }


    @PutMapping( "/medicament" )
    public Exam updateExamMedicament( @RequestBody Exam examMedicamentList ) {

        logger.info( "Recebendo atualização de medicamentos do exame id={}", examMedicamentList.getId() );
        return examService.updateExamMedicament( examMedicamentList );
    }


    @PutMapping( "/equipment" )
    public Exam updateExamEquipment( @RequestBody Exam examEquipmentList ) {

        logger.info( "Recebendo atualização de equipamentos do exame id={}", examEquipmentList.getId() );
        return examService.updateExamEquipment( examEquipmentList );
    }
}
