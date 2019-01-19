import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import { initRegisterPatient } from './patientActions'
import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputSelect from '../common/form/labelAndInputSelect'
import DateTimeInput from '../common/form/dateTimeInput'
import If from '../common/operator/if'
import Tabs from '../common/tab/tabs';
import TabsHeader from '../common/tab/tabsHeader'
import TabsContent from '../common/tab/tabsContent'
import TabHeader from '../common/tab/tabHeader'
import TabContent from '../common/tab/tabContent'
import SystemInfo from '../common/form/systemInfo'
import { upper, documentNumber as documentNumberMask } from '../common/form/formatValues'

class PatientForm extends Component {

    renderExamRows() {
        const list = this.props.examList || []
        return list.map(exam => (
            <tr key={exam.id}>
                <td><Link to={`/exam/${exam.id}`}>{exam.id}</Link></td>
                <td><Link to={`/exam/${exam.id}`}>{exam.achievementDate}</Link></td>
                <td><Link to={`/exam/${exam.id}`}>{exam.createdAt}</Link></td>
                <td><Link to={`/exam/${exam.id}`}>{exam.createdBy}</Link></td>
            </tr>
        ))
    }

    renderExamRequestRows() {
        const list = this.props.examRequestList || []
        return list.map(examRequest => (
            <tr key={examRequest.id}>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.id}</Link></td>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.createdAt}</Link></td>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.createdBy}</Link></td>
            </tr>
        ))
    }


    render() {
        const { handleSubmit,
            readOnly,
            pristine,
            reset,
            submitting,
            submitLabel,
            submitClass,
            showSystemInfo,
            initRegisterPatient,
            patientId,
            showTabs } = this.props

        return (
            <div>
                <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                    <div className='box'>
                        <div className='box-body'>
                            <fieldset>
                                <legend>Paciente</legend>
                                <Field name='name' component={LabelAndInput} readOnly={readOnly}
                                    label='Nome' cols='12 10' placeholder='Informe o nome' normalize={upper} />
                                <Field name='documentNumber' component={LabelAndInput} readOnly={readOnly}
                                    label='CPF' cols='12 2' placeholder='Informe o CPF' {...documentNumberMask} />

                                <Field name='birthDate' component={DateTimeInput} readOnly={readOnly} mode='date'
                                    label='Data de Nascimento' cols='12 3' placeholder='Data Nascimento' formatDate='DD/MM/YYYY' />
                                <Field name='nacionality' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                    label='Nacionalidade' cols='12 3' placeholder='Informe a Nacionalidade' />
                                <Field name='sex' component={LabelAndInputSelect}
                                    label='Sexo' cols='12 2'
                                    readOnly={readOnly}
                                    placeholder='Sexo'
                                    options={[{ 'value': 'M', 'label': 'MASCULINO' },
                                    { 'value': 'F', 'label': 'FEMININO' }]}>
                                </Field>

                                <Field name='civilStatus' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                    label='Estado Civil' cols='12 2' placeholder='Estado Civil' />
                                <Field name='job' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                    label='Profissão' cols='12 2' placeholder='Informe a Profissão' />

                                <If test={showSystemInfo}>
                                    <SystemInfo formName="patientForm" />
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
                                    onClick={initRegisterPatient}
                                    disabled={pristine || submitting}>
                                    {'Cancelar'}
                                </button>
                            </div>
                        </If>
                    </div>
                </form >

                <If test={showTabs === true}>
                    <div className='box'>
                        <div className='box-body'>
                            <Tabs>
                                <TabsHeader>
                                    <TabHeader label='Exames' icon='notes-medical' target='tabExams' />
                                    <TabHeader label='Requerimentos' icon='box' target='tabRequests' />
                                </TabsHeader>
                                <TabsContent>
                                    <TabContent id='tabExams'>
                                        <div>
                                            <table className='table'>
                                                <thead>
                                                    <tr>
                                                        <th>Exame Id</th>
                                                        <th>Data Realização</th>
                                                        <th>Criado Em</th>
                                                        <th>Criado Por</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {this.renderExamRows()}
                                                </tbody>
                                            </table>
                                            <div className='box-footer'>
                                                <Link to={`/patient/${patientId}/exam/add`}>
                                                    <button type='submit'
                                                        className={`btn btn-success`}>
                                                        <i className="fa fa-plus"></i> Adicionar
                                                </button>
                                                </Link>
                                            </div>
                                        </div >
                                    </TabContent>
                                    <TabContent id='tabRequests'>
                                        <div>
                                            <table className='table'>
                                                <thead>
                                                    <tr>
                                                        <th>Requerimento Id</th>
                                                        <th>Criado Em</th>
                                                        <th>Criado Por</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {this.renderExamRequestRows()}
                                                </tbody>
                                            </table>
                                            <div className='box-footer'>
                                                <Link to={`/patient/${patientId}/exam-request/add`}>
                                                    <button type='submit'
                                                        className={`btn btn-success`}>
                                                        <i className="fa fa-plus"></i> Adicionar
                                                </button>
                                                </Link>
                                            </div>
                                        </div >
                                    </TabContent>
                                </TabsContent>
                            </Tabs>
                        </div>
                    </div>
                </If>
            </div>
        )
    }
}

PatientForm = reduxForm({ form: 'patientForm', destroyOnUnmount: false })(PatientForm)
const selector = formValueSelector('patientForm')
const mapStateToProps = state => ({
    birthDate: selector(state, 'birthDate'),
    createdAt: selector(state, 'createdAt'),
    updateAt: selector(state, 'updateAt'),
    patientId: selector(state, 'id'),
    examRequestList: state.patient.examRequestList,
    examList: state.patient.examList
})

const mapDispatchToProps = dispatch => bindActionCreators({ initRegisterPatient }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(PatientForm)