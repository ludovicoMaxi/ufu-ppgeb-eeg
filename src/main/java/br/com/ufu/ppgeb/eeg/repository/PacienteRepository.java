package br.com.ufu.ppgeb.eeg.repository;


import br.com.ufu.ppgeb.eeg.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by joaol on 08/09/17.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Long > {
}
