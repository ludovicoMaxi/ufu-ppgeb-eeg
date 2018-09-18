package br.com.ufu.ppgeb.eeg.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by joaol on 08/09/17.
 */
@Entity
@Table( name = "PACIENTE" )
public class Paciente {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    String nome;

    char sexo;

    @JsonFormat(pattern="dd/MM/yyyy")
    Date dataNascimento;


    public Long getId() {

        return id;
    }


    public void setId( Long id ) {

        this.id = id;
    }


    public String getNome() {

        return nome;
    }


    public void setNome( String nome ) {

        this.nome = nome;
    }


    public char getSexo() {

        return sexo;
    }


    public void setSexo( char sexo ) {

        this.sexo = sexo;
    }


    public Date getDataNascimento() {

        return dataNascimento;
    }


    public void setDataNascimento( Date dataNascimento ) {

        this.dataNascimento = dataNascimento;
    }


    @Override
    public String toString() {

        return "Paciente{" + "id=" + id + ", nome='" + nome + '\'' + ", sexo=" + sexo + ", dataNascimento=" + dataNascimento + '}';
    }
}
