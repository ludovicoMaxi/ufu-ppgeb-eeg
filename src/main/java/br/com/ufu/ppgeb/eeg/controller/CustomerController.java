package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import br.com.ufu.ppgeb.eeg.view.CustomerView;
import br.com.ufu.ppgeb.eeg.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.Customer;


/**
 * Created by joaol on 05/09/18.
 */
@Controller
@RequestMapping( "/api/customer" )
public class CustomerController {

    private static final Logger logger = LogManager.getLogger( CustomerController.class );

    @Autowired
    private CustomerService customerService;


    @GetMapping
    @ResponseBody
    public List< Customer > list(
        @RequestParam( value = "brand", required = false ) String brand,
        @RequestParam( value = "model", required = false ) String model,
        @RequestParam( value = "color", required = false ) String color,
        @RequestParam( value = "maxYear", required = false ) Integer maxYear,
        @RequestParam( value = "minYear", required = false ) Integer minYear,
        @RequestParam( value = "maxPrice", required = false ) Float maxPrice,
        @RequestParam( value = "minPrice", required = false ) Float minPrice,
        @RequestParam( value = "isNew", required = false ) Boolean isNew

    ) {

        logger.info(
            "Parametros{ brand=" + brand + ", model=" + model + ", color=" + color + ", maxYear=" + maxYear + ", minYear=" + minYear + ", maxPrice=" + maxPrice
                + ", minPrice=" + minPrice + ", isNew=" + isNew + "}" );
        return customerService.findByFilter( brand, model, color, maxYear, minYear, maxPrice, minPrice, isNew );

    }


    @GetMapping( "/{id}" )
    public @ResponseBody Customer findById( @PathVariable( value = "id" ) Long id ) {

        return this.customerService.findById( id );
    }


    @PostMapping
    public @ResponseBody Customer save( @RequestBody CustomerView customerview ) {

        logger.info( "Saving " + customerview );
        return this.customerService.save( customerview );
    }


    @DeleteMapping( "/{id}" )
    public @ResponseBody String delete( @PathVariable( value = "id" ) Long id ) {

        this.customerService.delete( id );
        return "redirect:/";
    }


    @PutMapping
    public @ResponseBody Customer update( @RequestBody CustomerView customerView )
        throws Exception {

        logger.info( "Updating " + customerView );
        return this.customerService.update( customerView );
    }
}
