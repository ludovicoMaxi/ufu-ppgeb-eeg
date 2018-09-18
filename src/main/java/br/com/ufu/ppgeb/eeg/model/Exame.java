package br.com.ufu.ppgeb.eeg.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
@Table(name = "EXAME")
public class Exame {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "PEDIDO_ID", nullable = false)
    private Long pedidoId;

    @Column(name = "PACIENTE_ID", nullable = false)
    private Long pacienteId;

    @Column(name = "DATA_REALIZACAO", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date dataRealizacao;

    @Column(name = "LAUDO", length = 256, nullable = false)
    private String laudo;

    @Column(name = "CONCLUSAO", length = 256, nullable = false)
    private String conclusao;

    @Column(name = "LEITO", length = 256)
    private String leito;

    @Column(name = "DADOS_CLINICOS", length = 256, nullable = false)
    private String dadosClinicos;

    @Column(name = "MEDICACOES_USADAS", length = 256)
    private String medicacoesUsadas;

    @Column(name = "APARELHAGEM", length = 256, nullable = false)
    private String aparelhagem;


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public Long getPedidoId() {

        return pedidoId;
    }


    public void setPedidoId(Long pedidoId) {

        this.pedidoId = pedidoId;
    }


    public Long getPacienteId() {

        return pacienteId;
    }


    public void setPacienteId(Long pacienteId) {

        this.pacienteId = pacienteId;
    }


    public Date getDataRealizacao() {

        return dataRealizacao;
    }


    public void setDataRealizacao(Date dataRealizacao) {

        this.dataRealizacao = dataRealizacao;
    }


    public String getLaudo() {

        return laudo;
    }


    public void setLaudo(String laudo) {

        this.laudo = laudo;
    }


    public String getConclusao() {

        return conclusao;
    }


    public void setConclusao(String conclusao) {

        this.conclusao = conclusao;
    }


    public String getLeito() {

        return leito;
    }


    public void setLeito(String leito) {

        this.leito = leito;
    }


    public String getDadosClinicos() {

        return dadosClinicos;
    }


    public void setDadosClinicos(String dadosClinicos) {

        this.dadosClinicos = dadosClinicos;
    }


    public String getMedicacoesUsadas() {

        return medicacoesUsadas;
    }


    public void setMedicacoesUsadas(String medicacoesUsadas) {

        this.medicacoesUsadas = medicacoesUsadas;
    }


    public String getAparelhagem() {

        return aparelhagem;
    }


    public void setAparelhagem(String aparelhagem) {

        this.aparelhagem = aparelhagem;
    }


    @Override
    public String toString() {

        return "Exame{" + "id=" + id + ", pedidoId=" + pedidoId + ", pacienteId=" + pacienteId + ", dataRealizacao=" + dataRealizacao + ", laudo='" + laudo
                + '\'' + ", conclusao='" + conclusao + '\'' + ", leito='" + leito + '\'' + ", dadosClinicos='" + dadosClinicos + '\'' + ", medicacoesUsadas='"
                + medicacoesUsadas + '\'' + ", aparelhagem='" + aparelhagem + '\'' + '}';
    }
}
