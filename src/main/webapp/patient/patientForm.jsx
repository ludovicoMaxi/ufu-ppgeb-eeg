import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'

import { initRegisterPatient } from './patientActions'
import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputSelect from '../common/form/labelAndInputSelect'
import DateTimeInput from '../common/form/dateTimeInput'
import Row from '../common/layout/row'
import If from '../common/operator/if'
import Tabs from '../common/tab/tabs';
import TabsHeader from '../common/tab/tabsHeader'
import TabsContent from '../common/tab/tabsContent'
import TabHeader from '../common/tab/tabHeader'
import TabContent from '../common/tab/tabContent'
import ContactList from './contactList'
import SystemInfo from '../common/form/systemInfo'
import { documentNumber as documentNumberMask } from '../common/form/formatValues'

class PatientForm extends Component {

    render() {
        const { documentNumberReadOnly, handleSubmit, readOnly, pristine, reset, submitting, contactList } = this.props
        const upper = value => value && value.toUpperCase();

        return (
            <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                <div className='box'>
                    <div className='box-body'>
                        <fieldset>
                            <Field name='name' component={LabelAndInput} readOnly={readOnly}
                                label='Nome' cols='12 10' placeholder='Informe o nome' normalize={upper} />
                            <Field name='documentNumber' component={LabelAndInput} readOnly={documentNumberReadOnly}
                                label='CPF' cols='12 2' placeholder='Informe o CPF' {...documentNumberMask} />

                            <Field name='birthDate' component={DateTimeInput} readOnly={readOnly} mode='date'
                                label='Data de Nascimento' cols='12 3' placeholder='Data Nascimento' formatDate='DD/MM/YYYY' />
                            <Field name='nacionality' component={LabelAndInput} readOnly={readOnly}
                                label='Nacionalidade' cols='12 3' placeholder='Informe a Nacionalidade' />
                            <Field name='sex' component={LabelAndInputSelect}
                                label='Sexo' cols='12 2' placeholder='Informe o Sexo'
                                optionList={[{ 'value': 'M', 'label': 'Masculino' },
                                { 'value': 'F', 'label': 'Feminino' }]}>
                            </Field>

                            <Field name='civilStatus' component={LabelAndInput} readOnly={readOnly}
                                label='Estado Civil' cols='12 2' placeholder='Estado Civil' />
                            <Field name='job' component={LabelAndInput} readOnly={readOnly}
                                label='Profissão' cols='12 2' placeholder='Informe a Profissão' />

                            <If test={this.props.showSystemInfo}>
                                <SystemInfo formName="patientForm" />
                            </If>
                            <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                        </fieldset>
                    </div>
                    <div className='box-footer'>
                        <button type='submit'
                            className={`btn btn-${this.props.submitClass}`}
                            disabled={submitting}>
                            {this.props.submitLabel}
                        </button>
                        <button type='button' className='btn btn-default'
                            onClick={this.props.initRegisterPatient}
                            disabled={pristine || submitting}>
                            {'Cancelar'}
                        </button>
                    </div>
                    {/* <Tabs>
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
                                                <Field name='setor' component={LabelAndInput} readOnly={readOnly}
                                                    label='Setor' cols='6 6' placeholder='Informe o setor' />
                                                <Field name='convenio' component={LabelAndInput} readOnly={readOnly}
                                                    label='Convenio' cols='6 6' placeholder='Informe o convenio' />
                                                <Field name='medicoSoliciante' component={LabelAndInput} readOnly={readOnly}
                                                    label='Medico Solicitante' cols='12 6' placeholder='Informe o Médico' />
                                                <Field name='usuario' component={LabelAndInput} readOnly={readOnly}
                                                    label='Usuario' cols='6 6' placeholder='Informe o usuário' />
                                                <Field name='clinicaOrigem' component={LabelAndInput} readOnly={readOnly}
                                                    label='Clinica Origem' cols='6 6' placeholder='Informe a Clinica Origem' />
                                                <Field name='origemCidade' component={LabelAndInput} readOnly={readOnly}
                                                    label='origem Cidade' cols='9 4' placeholder='Informe a cidade de origem' />
                                                <Field name='dataPedido' component={DateTimeInput} readOnly={readOnly} mode='date'
                                                    label='Data Pedido' cols='12 3' placeholder='Data Pedido' formatDate='DD/MM/YYYY' />
                                                <Field name='dataRealizacao' component={DateTimeInput} readOnly={readOnly} mode='date'
                                                    label='Data Realização' cols='12 3' placeholder='Data Realização' formatDate='DD/MM/YYYY' />

                                                <If test={this.props.showSystemInfo}>
                                                    <Row>
                                                        <Field name='createdAt' component={DateTimeInput} readOnly={true}
                                                            label='Criado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='createdBy' component={LabelAndInput} readOnly={true}
                                                            label='Criado por' cols='6 3' />
                                                        <Field name='updatedAt' component={DateTimeInput} readOnly={true}
                                                            label='Atualizado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='updatedBy' component={LabelAndInput} readOnly={true}
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
                                                <Field name='laudo' component={LabelAndInput} readOnly={readOnly}
                                                    label='Laudo' cols='12 12' placeholder='Informe o Laudo' />
                                                <Field name='conclusao' component={LabelAndInput} readOnly={readOnly}
                                                    label='Conclusão' cols='12 12' placeholder='Informe a conclusao' />
                                                <Field name='leito' component={LabelAndInput} readOnly={readOnly}
                                                    label='Leito' cols='6 3' placeholder='Informe o Leito' />
                                                <Field name='dadosClinicos' component={LabelAndInput} readOnly={readOnly}
                                                    label='Dados Clinicos' cols='12 12' placeholder='Informe os dados Clinicos' />
                                                <Field name='medicacoesUsadas' component={LabelAndInput} readOnly={readOnly}
                                                    label='Medicacoes Usadas' cols='6 6' placeholder='Informe a medicacoes Usadas' />
                                                <Field name='aparelhagem' component={LabelAndInput} readOnly={readOnly}
                                                    label='Aparelhagem' cols='9 4' placeholder='Informe a aparelhagem' />
                                                <Field name='dataRealizacao' component={DateTimeInput} readOnly={readOnly} mode='date'
                                                    label='Data Realização' cols='12 3' placeholder='Data Realização' formatDate='DD/MM/YYYY' />
                                                <If test={this.props.showSystemInfo}>
                                                    <Row>
                                                        <Field name='createdAt' component={DateTimeInput} readOnly={true}
                                                            label='Criado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='createdBy' component={LabelAndInput} readOnly={true}
                                                            label='Criado por' cols='6 3' />
                                                        <Field name='updatedAt' component={DateTimeInput} readOnly={true}
                                                            label='Atualizado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                                        <Field name='updatedBy' component={LabelAndInput} readOnly={true}
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
                    </Tabs> */}
                </div>
            </form >
        )
    }
}

PatientForm = reduxForm({ form: 'patientForm', destroyOnUnmount: false })(PatientForm)
const selector = formValueSelector('patientForm')
const mapStateToProps = state => ({
    dataNascimento: selector(state, 'dataNascimento'),
    criadoEm: selector(state, 'criadoEm'),
    atualizadoEm: selector(state, 'atualizadoEm')
})

const mapDispatchToProps = dispatch => bindActionCreators({ initRegisterPatient }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(PatientForm)