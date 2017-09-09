package br.com.ufu.ppgeb.eeg.controller;


import br.com.ufu.ppgeb.eeg.model.Paciente;
import br.com.ufu.ppgeb.eeg.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by joaol on 08/09/17.
 */
@Controller
@RequestMapping( "/pacientes" )
public class EegController {

    @Autowired
    private PacienteRepository pacienteRepository;


    @GetMapping
    public ModelAndView listar() {

        ModelAndView modelAndView = new ModelAndView( "ListaPacientes" );
        modelAndView.addObject( "pacientes", pacienteRepository.findAll() );
        modelAndView.addObject( "paciente", new Paciente() );
        return modelAndView;

    }


    @PostMapping
    public String salvar( Paciente paciente ) {

        this.pacienteRepository.save( paciente );
        return "redirect:/pacientes";
    }
}
