package br.com.ufu.ppgeb.eeg.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ufu.ppgeb.eeg.model.Pedido;


/**
 * Created by joaol on 08/09/17.
 */
public interface PedidoRepository extends JpaRepository< Pedido, Long > {
}
