package br.com.ufu.ppgeb.eeg.repository;


import br.com.ufu.ppgeb.eeg.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by joaol on 15/04/18.
 */
public interface CarRepository extends JpaRepository<Car, Long>, CarRepositoryCustom {
}
