import React from 'react';
import $ from 'jquery';
import toastr from 'toastr';

var PacienteForm = React.createClass({
  getInitialState: function () {
    return {id:'',
    prontuario:'',
    pedido:'',
    setor:'',
    convenio:'',
    medico_soliciante:'',
    usuario:'',
    clinicaOrigem:'',
    origemCidade:'',
    pacienteId:'',
    dataPedido:'',
    dataRealizacao: ''};
  },
  handleChangeProntuario(event) {
    this.setState({prontuario: event.target.value});
  },
  handleChangePedido(event) {
    this.setState({pedido: event.target.value});
  },
  handleChangeConvenio(event) {
    this.setState({convenio: event.target.value});
  },
  handleChangeMedico_soliciante(event) {
    this.setState({medico_soliciante: event.target.value});
  },
  handleChangeUsuario(event) {
    this.setState({usuario: event.target.value});
  },
  handleChangeClinicaOrigem(event) {
    this.setState({clinicaOrigem: event.target.value});
  },
  handleChangeOrigemCidade(event) {
    this.setState({origemCidade: event.target.value});
  },
  handleChangeDataPedido(event) {
    this.setState({dataPedido: event.target.value});
  },
  handleChangeDataRealizacao(event) {
    this.setState({dataRealizacao: event.target.value});
  },
  handleChangePacienteId(event) {
    this.setState({pacienteId: event.target.value});
  },
  handleSubmit(event) {
    $.ajax({
      url: "http://localhost:8080/pedidos/",
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
          <fieldset>
            <legend>Dados do Pedido</legend>
            <table cellspacing="10">
              <tr>
                <td>
                  <label for="prontuario">Prontuario:</label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Prontuário" value={this.state.prontuario} onChange={this.handleChangeProntuario}/>
                </td>
                <td>
                  <label for="pedido">Pedido:</label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Pedido" value={this.state.pedido} onChange={this.handleChangePedido}/>
                </td>
                <td>
                  <label for="dataPedido">Data Pedido:</label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Data Pedido" value={this.state.dataPedido} onChange={this.handleChangeDataPedido}/>
                </td>
                <td>
                  <label for="dataRealizacao">Data Final para realização:</label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Data Final para realização" value={this.state.dataRealizacao} onChange={this.handleChangeDataRealizacao}/>
                </td>
              </tr>
              <tr>
                <td>
                  <label for="usuario">Usuário: </label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Usuario" value={this.state.usuario} onChange={this.handleChangeUsuario}/>
                </td>
                <td>
                  <label for="Convenio">Convenio: </label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Convenio" value={this.state.convenio} onChange={this.handleChangeConvenio}/>
                </td>
                <td>
                  <label for="Convenio">Clinica de Origem: </label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="clinica Origem" value={this.state.clinicaOrigem} onChange={this.handleChangeClinicaOrigem}/>
                </td>
              </tr>
              <tr>
                <td>
                  <label for="usuario">Medico Solicitante: </label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Medico Solicitante" value={this.state.medico_soliciante} onChange={this.handleChangeMedico_soliciante}/>
                </td>
                <td>
                  <label for="Convenio">Cidade de Origem: </label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="Cidade de Origem" value={this.state.origemCidade} onChange={this.handleChangeOrigemCidade}/>
                </td>
                <td>
                  <label for="pacienteID">Paciente ID: </label>
                </td>
                <td align="left">
                  <input type="text" className="form-control" placeholder="ID paciente" value={this.state.pacienteId} onChange={this.handleChangePacienteId}/>
                </td>
              </tr>
            </table>
            <button type="submit" className="btn btn-primary">Adicionar</button>
          </fieldset>
        </div>
      </form>
    );
  }
});


var PedidoRegister = React.createClass({
  render: function() {
    return (
      <PacienteForm />
    );
  }
});

export default PedidoRegister;
