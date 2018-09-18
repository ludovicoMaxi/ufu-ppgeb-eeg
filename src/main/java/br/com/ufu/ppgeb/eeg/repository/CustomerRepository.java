package br.com.ufu.ppgeb.eeg.repository;


import br.com.ufu.ppgeb.eeg.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by joaol on 05/09/18.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
