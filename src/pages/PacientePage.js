import React from 'react';
import $ from 'jquery';
import toastr from 'toastr';

var Paciente = React.createClass({
  getInitialState: function() {
    return {display: true };
  },
  handleDelete() {
    var self = this;
    $.ajax({
      url: "http://localhost:8080/pacientes/" + this.props.paciente.id ,
      type: 'DELETE',
      success: function(result) {
        self.setState({display: false});
      },
      error: function(xhr, ajaxOptions, thrownError) {
        toastr.error(xhr.responseJSON.message);
      }
    });
  },
  render: function() {
    return (
      <tr>
      <td>{this.props.paciente.nome}</td>
      <td>{this.props.paciente.sexo}</td>
      <td>{this.props.paciente.dataNascimento}</td>
      <td>
      <button className="btn btn-info" onClick={this.handleDelete}>Delete</button>
      </td>
      </tr>
    );
  }
});


var PacienteTable = React.createClass({
  render: function() {
    var rows = [];
    this.props.pacientes.forEach(function(paciente) {
      rows.push(<Paciente paciente={paciente} />);
    });
    return (
      <div>
      <PacienteForm />
      <table className="table table-striped">
      <thead>
      <tr>
      <th>Nome</th><th>Sexo</th><th>Data de Nascimento</th>
      </tr>
      </thead>
      <tbody>{rows}</tbody>
      </table>
      </div>
    );
  }
});

var PacienteForm = React.createClass({
  getInitialState: function () {
    return {nome:'', sexo:'', dataNascimento:''};
  },
  handleChangeNome(event) {
    this.setState({nome: event.target.value});
  },
  handleChangeSexo(event) {
    this.setState({sexo: event.target.value});
  },
  handleChangeDataNasc(event) {
    this.setState({dataNascimento: event.target.value});
  },
  handleSubmit(event) {
    $.ajax({
      url: "http://localhost:8080/pacientes/",
      type: 'POST',
      data: this.state,
      success: function(result) {
        alert('Adicionado com sucesso');
      },
      error: function(xhr, ajaxOptions, thrownError) {
        toastr.error(xhr.responseJSON.message);
      }
    });
    event.preventDefault();
  },
  render: function() {
    return (
      <form className="form-inline" method="POST" onSubmit={this.handleSubmit} style={{margin: '20px 0'}}>
        <div class="form-group">
          <input type="text" className="form-control" placeholder="Nome"
            value={this.state.nome} onChange={this.handleChangeNome}/>
          <input type="text" className="form-control" placeholder="Sexo"
            value={this.state.sexo} onChange={this.handleChangeSexo}/>
          <input type="text" className="form-control" placeholder="Data Nascimento"
            value={this.state.dataNascimento} onChange={this.handleChangeDataNasc} />
          <button type="submit" className="btn btn-primary">Adicionar</button>
        </div>
      </form>
    );
  }
});


var PacientePage = React.createClass({

  loadPacientesFromServer: function () {
    var self = this;
    $.ajax({
      url: "http://localhost:8080/pacientes",
      dataType: "json"
    }).then(function (data) {
      self.setState({pacientes: data});
    });
  },

  getInitialState: function () {
    return {pacientes: []};
  },

  componentDidMount: function () {
    this.loadPacientesFromServer();
  },

  render() {
    return (
      <PacienteTable pacientes={this.state.pacientes}/>
    );
  }
});

export default PacientePage;
