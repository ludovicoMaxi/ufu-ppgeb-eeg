package br.com.ufu.ppgeb.eeg.model;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
public class Exame {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    private Long pedidoId;

    private Long pacienteId;

    private Date dataRealizacao;

    private String laudo;

    private String conclusao;

    private String leito;

    private String dadosClinicos;

    private String medicacoesUsadas;

    private String aparelhagem;


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public Long getPedidoId() {

        return pedidoId;
    }


    public void setPedidoId( Long pedidoId ) {

        this.pedidoId = pedidoId;
    }


    public Long getPacienteId() {

        return pacienteId;
    }


    public void setPacienteId( Long pacienteId ) {

        this.pacienteId = pacienteId;
    }


    public Date getDataRealizacao() {

        return dataRealizacao;
    }


    public void setDataRealizacao( Date dataRealizacao ) {

        this.dataRealizacao = dataRealizacao;
    }


    public String getLaudo() {

        return laudo;
    }


    public void setLaudo( String laudo ) {

        this.laudo = laudo;
    }


    public String getConclusao() {

        return conclusao;
    }


    public void setConclusao( String conclusao ) {

        this.conclusao = conclusao;
    }


    public String getLeito() {

        return leito;
    }


    public void setLeito( String leito ) {

        this.leito = leito;
    }


    public String getDadosClinicos() {

        return dadosClinicos;
    }


    public void setDadosClinicos( String dadosClinicos ) {

        this.dadosClinicos = dadosClinicos;
    }


    public String getMedicacoesUsadas() {

        return medicacoesUsadas;
    }


    public void setMedicacoesUsadas( String medicacoesUsadas ) {

        this.medicacoesUsadas = medicacoesUsadas;
    }


    public String getAparelhagem() {

        return aparelhagem;
    }


    public void setAparelhagem( String aparelhagem ) {

        this.aparelhagem = aparelhagem;
    }


    @Override
    public String toString() {

        return "Exame{" + "id=" + id + ", pedidoId=" + pedidoId + ", pacienteId=" + pacienteId + ", dataRealizacao=" + dataRealizacao + ", laudo='" + laudo
            + '\'' + ", conclusao='" + conclusao + '\'' + ", leito='" + leito + '\'' + ", dadosClinicos='" + dadosClinicos + '\'' + ", medicacoesUsadas='"
            + medicacoesUsadas + '\'' + ", aparelhagem='" + aparelhagem + '\'' + '}';
    }
}
