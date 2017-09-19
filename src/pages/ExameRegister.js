import React from 'react';
import $ from 'jquery';
import toastr from 'toastr';

var ExameForm = React.createClass({
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
  handleChangePedidoId(event) {
    this.setState({pedidoId: event.target.value});
  },
  handleChangePacienteId(event) {
    this.setState({pacienteId: event.target.value});
  },
  handleChangeDataRealizacao(event) {
    this.setState({dataRealizacao: event.target.value});
  },
  handleChangeLaudo(event) {
    this.setState({laudo: event.target.value});
  },
  handleChangeConclusao(event) {
    this.setState({conclusao: event.target.value});
  },
  handleChangeLeito(event) {
    this.setState({leito: event.target.value});
  },
  handleChangeDadosClinicos(event) {
    this.setState({dadosClinicos: event.target.value});
  },
  handleChangeMedicacoesUsadas(event) {
    this.setState({medicacoesUsadas: event.target.value});
  },
  handleChangeAparelhagem(event) {
    this.setState({aparelhagem: event.target.value});
  },
  handleSubmit(event) {
    $.ajax({
      url: "http://localhost:8080/exames/",
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
            <legend>Dados do Exame</legend>
            <table cellspacing="10">
               <tr>
                  <td>
                     <label for="pedidoId">Id do pedido:</label>
                  </td>
                  <td align="left">
                     <input type="text" className="form-control" placeholder="Id do Pedido" value={this.state.pedidoId} onChange={this.handleChangePedidoId}/>
                  </td>
                  <td>
                     <label for="pacienteID">Paciente ID: </label>
                  </td>
                  <td align="left">
                     <input type="text" className="form-control" placeholder="ID paciente" value={this.state.pacienteId} onChange={this.handleChangePacienteId}/>
                  </td>
                  <td>
                     <label for="dataRealizacao">Data Realização:</label>
                  </td>
                  <td align="left">
                     <input type="text" className="form-control" placeholder="Data de Realização" value={this.state.dataRealizacao} onChange={this.handleChangeDataRealizacao}/>
                  </td>
                  <td>
                     <label for="leito">Leito:</label>
                  </td>
                  <td align="left">
                     <input type="text" className="form-control" placeholder="Leito" value={this.state.leito} onChange={this.handleChangeLeito}/>
                  </td>
               </tr>
               <tr>
                  <td>
                     <label for="dadosClinicos">Dados Clinicos: </label>
                  </td>
                  <td align="left">
                     <input type="text" className="form-control" placeholder="Dados Clinicos" value={this.state.dadosClinicos} onChange={this.handleChangeDadosClinicos}/>
                  </td>
                  <td>
                     <label for="medicacoesUsadas">Medicações Usadas: </label>
                  </td>
                  <td align="left">
                     <input type="text" className="form-control" placeholder="Medicações Usadas" value={this.state.medicacoesUsadas} onChange={this.handleChangeMedicacoesUsadas}/>
                  </td>
                  <td>
                     <label for="aparelhagem">Aparelhagem: </label>
                  </td>
                  <td align="left">
                     <input type="text" className="form-control" placeholder="Aparelhagem" value={this.state.aparelhagem} onChange={this.handleChangeAparelhagem}/>
                  </td>
               </tr>
               <tr>
                  <td>
                     <label for="laudo">laudo:</label>
                  </td>
                  <td align="left">
                     <textarea type="text" className="form-control" placeholder="Laudo" value={this.state.laudo} onChange={this.handleChangeLaudo}/>
                  </td>
               </tr>
               <tr>
                  <td>
                     <label for="conclusao">conclusão:</label>
                  </td>
                  <td align="left">
                     <textarea className="form-control" placeholder="Conclusão" value={this.state.conclusao} onChange={this.handleChangeConclusao}/>
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


var ExameRegister = React.createClass({
  render: function() {
    return (
      <ExameForm />
    );
  }
});

export default ExameRegister;
