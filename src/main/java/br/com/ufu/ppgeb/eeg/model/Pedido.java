package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
@Table( name = "PEDIDO" )
public class Pedido {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    private Long prontuario;

    private Long pedido;

    private String setor;

    private String convenio;

    private String medico_soliciante;

    private String usuario;

    private String clinicaOrigem;

    private String origemCidade;

    private Long pacienteId;

    private Date dataPedido;

    private Date dataRealizacao;


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public Long getProntuario() {

        return prontuario;
    }


    public void setProntuario( Long prontuario ) {

        this.prontuario = prontuario;
    }


    public Long getPedido() {

        return pedido;
    }


    public void setPedido( Long pedido ) {

        this.pedido = pedido;
    }


    public String getSetor() {

        return setor;
    }


    public void setSetor( String setor ) {

        this.setor = setor;
    }


    public String getConvenio() {

        return convenio;
    }


    public void setConvenio( String convenio ) {

        this.convenio = convenio;
    }


    public String getMedico_soliciante() {

        return medico_soliciante;
    }


    public void setMedico_soliciante( String medico_soliciante ) {

        this.medico_soliciante = medico_soliciante;
    }


    public String getUsuario() {

        return usuario;
    }


    public void setUsuario( String usuario ) {

        this.usuario = usuario;
    }


    public String getClinicaOrigem() {

        return clinicaOrigem;
    }


    public void setClinicaOrigem( String clinicaOrigem ) {

        this.clinicaOrigem = clinicaOrigem;
    }


    public String getOrigemCidade() {

        return origemCidade;
    }


    public void setOrigemCidade( String origemCidade ) {

        this.origemCidade = origemCidade;
    }


    public Long getPacienteId() {

        return pacienteId;
    }


    public void setPacienteId( Long pacienteId ) {

        this.pacienteId = pacienteId;
    }


    public Date getDataPedido() {

        return dataPedido;
    }


    public void setDataPedido( Date dataPedido ) {

        this.dataPedido = dataPedido;
    }


    public Date getDataRealizacao() {

        return dataRealizacao;
    }


    public void setDataRealizacao( Date dataRealizacao ) {

        this.dataRealizacao = dataRealizacao;
    }


    @Override
    public String toString() {

        return "Pedido{" + "id=" + id + ", prontuario=" + prontuario + ", pedido=" + pedido + ", setor='" + setor + '\'' + ", convenio='" + convenio + '\''
                + ", medico_soliciante='" + medico_soliciante + '\'' + ", usuario='" + usuario + '\'' + ", clinicaOrigem='" + clinicaOrigem + '\''
                + ", origemCidade='" + origemCidade + '\'' + ", pacienteId=" + pacienteId + ", dataPedido=" + dataPedido + ", dataRealizacao=" + dataRealizacao
                + '}';
    }
}
