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

import br.com.ufu.ppgeb.eeg.model.Activity;
import br.com.ufu.ppgeb.eeg.service.ActivityService;
import br.com.ufu.ppgeb.eeg.view.ActivityList;


/**
 * Created by joaol on 10/01/18.
 */
@Controller
@RequestMapping( "/api/activity" )
public class ActivityController {

    private static final Logger logger = LogManager.getLogger( ActivityController.class );

    @Autowired
    private ActivityService activityService;


    @GetMapping
    @ResponseBody
    public List< Activity > list( @RequestParam( value = "examId", required = true ) Long examId

    ) {

        logger.info( "Parametros{ examId=" + examId + " }" );
        return activityService.findByFilter( examId );

    }


    @GetMapping( "/{id}" )
    public @ResponseBody Activity findById( @PathVariable( value = "id" ) Long id ) {

        return this.activityService.findById( id );
    }


    @PostMapping
    public @ResponseBody Activity save( @RequestBody Activity activity ) {

        logger.info( "Saving " + activity );
        return this.activityService.save( activity );
    }


    @PutMapping
    public @ResponseBody ActivityList updateList( @RequestBody ActivityList activityList )
        throws Exception {

        logger.info( "Updating " + activityList );
        List< Activity > activities = this.activityService.updateList( activityList );
        activityList.setActivities( activities );
        return activityList;
    }
}
