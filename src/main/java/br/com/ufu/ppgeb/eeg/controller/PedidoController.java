package br.com.ufu.ppgeb.eeg.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ufu.ppgeb.eeg.model.Pedido;
import br.com.ufu.ppgeb.eeg.repository.PedidoRepository;


/**
 * Created by joaol on 08/09/17.
 */
@Controller
@RequestMapping( "/pedidos" )
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;


    @GetMapping
    @ResponseBody
    public List< Pedido > listar() {

        return pedidoRepository.findAll();

    }


    @PostMapping
    public String salvar( Pedido pedido ) {

        this.pedidoRepository.save( pedido );
        return "redirect:/pedidos";
    }


    @DeleteMapping( "/{id}" )
    public String delete( @PathVariable( value = "id" ) Long id ) {

        this.pedidoRepository.delete( id );
        return "redirect:/pedidos";
    }
}
