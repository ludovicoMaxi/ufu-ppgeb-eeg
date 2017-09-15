package br.com.ufu.ppgeb.eeg.controller;


import br.com.ufu.ppgeb.eeg.model.Paciente;
import br.com.ufu.ppgeb.eeg.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by joaol on 08/09/17.
 */
@Controller
@RequestMapping( "/pacientes" )
public class EegController {

    @Autowired
    private PacienteRepository pacienteRepository;


    @GetMapping
    @ResponseBody
    public List< Paciente > listar() {

        return pacienteRepository.findAll();

    }


    @PostMapping
    public String salvar( Paciente paciente ) {

        this.pacienteRepository.save( paciente );
        return "redirect:/pacientes";
    }

    @DeleteMapping("/{id}")
    public String delete( @PathVariable(value="id") Long id  ) {

        this.pacienteRepository.delete( id );
        return "redirect:/";
    }
}
