package br.com.ufu.ppgeb.eeg.service;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Customer;
import br.com.ufu.ppgeb.eeg.view.CustomerView;


public interface CustomerService {

    Customer save( CustomerView customerView );


    Customer findById( Long id );


    List< Customer > findAll();


    List< Customer > findByFilter( String brand, String model, String color, Integer maxYear, Integer minYear, Float maxPrice, Float minPrice, Boolean isNew );


    void delete( Long id );


    Customer update( CustomerView customerView );

}
