package br.com.ufu.ppgeb.eeg.repository;


import br.com.ufu.ppgeb.eeg.model.Car;

import java.util.List;


/**
 * Created by joaol on 15/04/18.
 */
public interface CarRepositoryCustom {

    List<Car> findByFilter(String brand, String model, String color, Integer maxYear, Integer minYear, Float maxPrice, Float minPrice, Boolean isNew);


}
