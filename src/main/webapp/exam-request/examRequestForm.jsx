import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'

import { initRegisterPatient } from './examRequestActions'
import LabelAndInput from '../common/form/labelAndInput'
import DateTimeInput from '../common/form/dateTimeInput'
import If from '../common/operator/if'
import Tabs from '../common/tab/tabs';
import TabsHeader from '../common/tab/tabsHeader'
import TabsContent from '../common/tab/tabsContent'
import TabHeader from '../common/tab/tabHeader'
import TabContent from '../common/tab/tabContent'
import SystemInfo from '../common/form/systemInfo'
import { onlyNumbers, upper } from '../common/form/formatValues'

class ExamRequestForm extends Component {

    render() {
        const { handleSubmit, readOnly, pristine, submitting, submitClass, submitLabel, showSystemInfo, reset } = this.props

        return (
            <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                <div className='box'>
                    <div className='box-body'>
                        <fieldset>
                            <legend>Requerimento</legend>
                            <Field name='medicalRecord' component={LabelAndInput} readOnly={readOnly}
                                label='Prontuário ID' cols='6 3' placeholder='Prontuário ID' normalize={onlyNumbers} />
                            <Field name='medicalRequest' component={LabelAndInput} readOnly={readOnly}
                                label='Requisição ID' cols='6 3' placeholder='Requisição ID' normalize={onlyNumbers} />
                            <Field name='requestDate' component={DateTimeInput} readOnly={readOnly} mode='date'
                                label='Data do Pedido' cols='12 3' placeholder='Data Pedido' formatDate='DD/MM/YYYY HH:mm:ss' />
                            <Field name='achievementDate' component={DateTimeInput} readOnly={readOnly} mode='date'
                                label='Data de Realização' cols='12 3' placeholder='Data Realização' formatDate='DD/MM/YYYY HH:mm:ss' />
                            <Field name='sector' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                label='Setor' cols='12 6' placeholder='Informe o setor' />
                            <Field name='agreement' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                label='Convênio' cols='12 6' placeholder='Informe o convenio' />
                            <Field name='clinicOrigin' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                label='Clinica de Origem' cols='12 6' placeholder='Informe a Clinica Origem' />
                            <Field name='CityOrigin' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                label='Cidade de Origem' cols='12 6' placeholder='Informe a cidade de origem' />
                            <Field name='doctorRequestant' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                label='Medico Solicitante' cols='12 8' placeholder='Informe o Médico' />
                            <Field name='user' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                label='Usuario' cols='12 4' placeholder='Informe o usuário' />

                            <If test={showSystemInfo}>
                                <SystemInfo formName="examRequestForm" />
                            </If>
                            <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                        </fieldset>
                    </div>
                    <If test={!readOnly}>
                        <div className='box-footer'>
                            <button type='submit'
                                className={`btn btn-${submitClass}`}
                                disabled={submitting}>
                                {submitLabel}
                            </button>
                            <button type='button' className='btn btn-default'
                                onClick={reset}
                                disabled={pristine || submitting}>
                                {'Cancelar'}
                            </button>
                        </div>
                    </If>
                </div>
            </form >
        )
    }
}

ExamRequestForm = reduxForm({ form: 'examRequestForm', destroyOnUnmount: false })(ExamRequestForm)
const selector = formValueSelector('examRequestForm')
const mapStateToProps = state => ({
    createdAt: selector(state, 'createdAt'),
    updateAt: selector(state, 'updateAt')
})

const mapDispatchToProps = dispatch => bindActionCreators({ initRegisterPatient }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ExamRequestForm)