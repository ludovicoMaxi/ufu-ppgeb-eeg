import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'

import { init } from './pacienteActions'
import LabelAndInput from '../common/form/labelAndInput'
import DateTimeInput from '../common/form/dateTimeInput'
import Row from '../common/layout/row'
import If from '../common/operator/if'
import Tabs from '../common/tab/tabs';
import TabsHeader from '../common/tab/tabsHeader'
import TabsContent from '../common/tab/tabsContent'
import TabHeader from '../common/tab/tabHeader'
import TabContent from '../common/tab/tabContent'
import ContactList from './contactList'

class PacientForm extends Component {

    render() {
        const { handleSubmit, readOnly, pristine, reset, submitting, contactList } = this.props
        return (
            <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                <div className='box'>
                    <div className='box-body'>
                        <fieldset>
                            <Field name='name' component={LabelAndInput} readOnly={readOnly}
                                label='Nome' cols='12 7' placeholder='Informe o nome' />
                            <Field name='sexo' component={LabelAndInput} readOnly={readOnly}
                                label='Sexo' cols='4 2' placeholder='sexo' />
                            <Field name='dataNascimento' component={DateTimeInput} readOnly={readOnly} mode='date'
                                label='Data de Nascimento' cols='8 3' placeholder='Data Nascimento' formatDate='DD/MM/YYYY' />
                            <If test={this.props.showSystemInfo}>
                                <Row>
                                    <Field name='criadoEm' component={DateTimeInput} readOnly={true}
                                        label='Criado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                    <Field name='customer.createdBy' component={LabelAndInput} readOnly={true}
                                        label='criadoPor' cols='6 3' />
                                    <Field name='atualizadoEm' component={DateTimeInput} readOnly={true}
                                        label='Atualizado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                    <Field name='customer.updatedBy' component={LabelAndInput} readOnly={true}
                                        label='atualizadoPor' cols='6 3' />
                                </Row>
                            </If>
                            {this.props.error && <strong>{this.props.error}</strong>}
                        </fieldset>
                    </div>
                    <div className='box-footer'>
                        <button type='submit'
                            className={`btn btn-${this.props.submitClass}`}
                            disabled={submitting}>
                            {this.props.submitLabel}
                        </button>
                        <button type='button' className='btn btn-default'
                            onClick={this.props.init}
                            disabled={pristine || submitting}>
                            {'Cancelar'}
                        </button>
                    </div>
                    <Tabs>
                        <TabsHeader>
                            <TabHeader label='Pedido' icon='address-card' target='tabPedido' />
                            <TabHeader label='Exame' icon='map-marker' target='tabExame' />
                        </TabsHeader>
                        <TabsContent>
                            <TabContent id='tabPedido'>
                                <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                                    <div className='box'>
                                        <div className='box-body'>
                                            <fieldset>
                                                <Field name='customer.setor' component={LabelAndInput} readOnly={readOnly}
                                                    label='Setor' cols='6 6' placeholder='Informe o setor' />
                                                <Field name='customer.convenio' component={LabelAndInput} readOnly={readOnly}
                                                    label='Convenio' cols='6 6' placeholder='Informe o convenio' />
                                                <Field name='customer.medicoSoliciante' component={LabelAndInput} readOnly={readOnly}
                                                    label='Medico Solicitante' cols='12 6' placeholder='Informe o Médico' />
                                                <Field name='customer.usuario' component={LabelAndInput} readOnly={readOnly}
                                                    label='Usuario' cols='6 6' placeholder='Informe o usuário' />
                                                <Field name='customer.clinicaOrigem' component={LabelAndInput} readOnly={readOnly}
                                                    label='Clinica Origem' cols='6 6' placeholder='Informe a Clinica Origem' />
                                                <Field name='customer.origemCidade' component={LabelAndInput} readOnly={readOnly}
                                                    label='origem Cidade' cols='9 4' placeholder='Informe a cidade de origem' />
                                                <Field name='customer.dataPedido' component={DateTimeInput} readOnly={readOnly} mode='date'
                                                    label='Data Pedido' cols='12 3' placeholder='Data Pedido' formatDate='DD/MM/YYYY' />
                                                <Field name='customer.dataRealizacao' component={DateTimeInput} readOnly={readOnly} mode='date'
                                                    label='Data Realização' cols='12 3' placeholder='Data Realização' formatDate='DD/MM/YYYY' />

                                                <If test={this.props.showSystemInfo}>
                                                    <Row>
                                                        <Field name='customer.createdAt' component={DateTimeInput} readOnly={true}
                                                            label='Criado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='customer.createdBy' component={LabelAndInput} readOnly={true}
                                                            label='Criado por' cols='6 3' />
                                                        <Field name='customer.updatedAt' component={DateTimeInput} readOnly={true}
                                                            label='Atualizado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='customer.updatedBy' component={LabelAndInput} readOnly={true}
                                                            label='Atualizado por' cols='6 3' />
                                                    </Row>
                                                </If>
                                                <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                                            </fieldset>
                                        </div>
                                        {this.props.error && <strong>{this.props.error}</strong>}
                                        <div className='box-footer'>
                                            <button type='submit'
                                                className={`btn btn-${this.props.submitClass}`}
                                                disabled={submitting}>
                                                {this.props.submitLabel}
                                            </button>
                                            <button type='button' className='btn btn-default'
                                                onClick={this.props.init}
                                                disabled={pristine || submitting}>
                                                {'Cancelar'}
                                            </button>
                                        </div>
                                    </div>
                                </form >
                            </TabContent>
                            <TabContent id='tabExame'>
                                <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                                    <div className='box'>
                                        <div className='box-body'>
                                            <fieldset>
                                                <Field name='customer.laudo' component={LabelAndInput} readOnly={readOnly}
                                                    label='Laudo' cols='12 12' placeholder='Informe o Laudo' />
                                                <Field name='customer.conclusao' component={LabelAndInput} readOnly={readOnly}
                                                    label='Conclusão' cols='12 12' placeholder='Informe a conclusao' />
                                                <Field name='customer.leito' component={LabelAndInput} readOnly={readOnly}
                                                    label='Leito' cols='6 3' placeholder='Informe o Leito' />
                                                <Field name='customer.dadosClinicos' component={LabelAndInput} readOnly={readOnly}
                                                    label='Dados Clinicos' cols='12 12' placeholder='Informe os dados Clinicos' />
                                                <Field name='customer.medicacoesUsadas' component={LabelAndInput} readOnly={readOnly}
                                                    label='Medicacoes Usadas' cols='6 6' placeholder='Informe a medicacoes Usadas' />
                                                <Field name='customer.aparelhagem' component={LabelAndInput} readOnly={readOnly}
                                                    label='Aparelhagem' cols='9 4' placeholder='Informe a aparelhagem' />
                                                <Field name='customer.dataRealizacao' component={DateTimeInput} readOnly={readOnly} mode='date'
                                                    label='Data Realização' cols='12 3' placeholder='Data Realização' formatDate='DD/MM/YYYY' />
                                                <If test={this.props.showSystemInfo}>
                                                    <Row>
                                                        <Field name='customer.createdAt' component={DateTimeInput} readOnly={true}
                                                            label='Criado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='customer.createdBy' component={LabelAndInput} readOnly={true}
                                                            label='Criado por' cols='6 3' />
                                                        <Field name='customer.updatedAt' component={DateTimeInput} readOnly={true}
                                                            label='Atualizado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='customer.updatedBy' component={LabelAndInput} readOnly={true}
                                                            label='Atualizado por' cols='6 3' />
                                                    </Row>
                                                </If>
                                                <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                                            </fieldset>
                                        </div>
                                        {this.props.error && <strong>{this.props.error}</strong>}
                                        <div className='box-footer'>
                                            <button type='submit'
                                                className={`btn btn-${this.props.submitClass}`}
                                                disabled={submitting}>
                                                {this.props.submitLabel}
                                            </button>
                                            <button type='button' className='btn btn-default'
                                                onClick={this.props.init}
                                                disabled={pristine || submitting}>
                                                {'Cancelar'}
                                            </button>
                                        </div>
                                    </div>
                                </form >
                            </TabContent>
                        </TabsContent>
                    </Tabs>
                </div>
            </form >
        )
    }
}

PacientForm = reduxForm({ form: 'pacienteForm', destroyOnUnmount: false })(PacientForm)
const selector = formValueSelector('pacienteForm')
const mapStateToProps = state => ({
    dataNascimento: selector(state, 'dataNascimento'),
    criadoEm: selector(state, 'criadoEm'),
    atualizadoEm: selector(state, 'atualizadoEm')
})

const mapDispatchToProps = dispatch => bindActionCreators({ init }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(PacientForm)