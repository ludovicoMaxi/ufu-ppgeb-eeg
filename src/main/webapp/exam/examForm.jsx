import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputTextarea from '../common/form/labelAndInputTextarea'
import DateTimeInput from '../common/form/dateTimeInput'
import If from '../common/operator/if'
import Tabs from '../common/tab/tabs';
import TabsHeader from '../common/tab/tabsHeader'
import TabsContent from '../common/tab/tabsContent'
import TabHeader from '../common/tab/tabHeader'
import TabContent from '../common/tab/tabContent'
import SystemInfo from '../common/form/systemInfo'
import { upper } from '../common/form/formatValues'
import ExamMedicamentListForm from '../examMedicament/examMedicamentListForm'
import ExamEquipmentListForm from '../examEquipment/examEquipmentListForm'
import EpochListForm from '../epoch/epochListForm'

class ExamForm extends Component {

    renderExamRequestRows() {
        const examRequest = this.props.examRequest || {}
        return (
            <tr key={examRequest.id}>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.id}</Link></td>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.createdAt}</Link></td>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.createdBy}</Link></td>
            </tr>
        )
    }

    render() {
        const {
            handleSubmit,
            readOnly,
            pristine,
            submitting,
            submitClass,
            submitLabel,
            showSystemInfo,
            reset,
            examId,
            examMedicaments,
            examEquipments,
            examRequest,
            patientId } = this.props;

        return (
            <div>
                <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                    <div className='box'>
                        <div className='box-body'>
                            <fieldset>
                                <legend>Exame</legend>
                                <Field name='achievementDate' component={DateTimeInput} readOnly={readOnly} mode='date'
                                    label='Data de Realização' cols='12 3' placeholder='Data Realização' formatDate='DD/MM/YYYY HH:mm:ss' />
                                <Field name='bed' component={LabelAndInput} readOnly={readOnly} normalize={upper}
                                    label='Leito' cols='12 6' placeholder='Informe o Leito' />
                                <Field name='clinicalData' component={LabelAndInputTextarea} readOnly={readOnly} normalize={upper}
                                    label='Dados Clinicos' cols='12 12' placeholder='Informe os dados Clinicos' />
                                <Field name='medicalReport' component={LabelAndInputTextarea} readOnly={readOnly} normalize={upper}
                                    label='Laudo' cols='12 12' placeholder='Informe o Laudo' />
                                <Field name='conclusion' component={LabelAndInputTextarea} readOnly={readOnly} normalize={upper}
                                    label='Conclusão' cols='12 12' placeholder='Informe a Conclusão' />
                                <If test={showSystemInfo}>
                                    <SystemInfo formName="examForm" />
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
                <If test={!!examId}>
                    <Tabs>
                        <TabsHeader>
                            <TabHeader label='Medicamentos' icon='capsules' target='tabMedicine' />
                            <TabHeader label='Equipamentos' icon='cogs' target='tabEquipments' />
                            <TabHeader label='Épocas' icon='stopwatch' target='tabEpochs' />
                            <If test={examRequest != undefined} >
                                <TabHeader label='Requerimentos' icon='box' target='tabRequests' />
                            </If>
                        </TabsHeader>
                        <TabsContent>
                            <TabContent id='tabMedicine'>
                                <ExamMedicamentListForm examId={examId} examMedicamentList={examMedicaments} />
                            </TabContent>
                            <TabContent id='tabEquipments'>
                                <ExamEquipmentListForm examId={examId} examEquipmentList={examEquipments} />
                            </TabContent>
                            <TabContent id='tabEpochs'>
                                <EpochListForm examId={examId} />
                            </TabContent>
                            <If test={examRequest != undefined} >
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
                                    </div >
                                </TabContent>
                            </If>
                        </TabsContent>
                    </Tabs>
                </If>
            </div>
        )
    }
}

ExamForm = reduxForm({ form: 'examForm', destroyOnUnmount: false })(ExamForm)
const selector = formValueSelector('examForm')
const mapStateToProps = state => ({
    createdAt: selector(state, 'createdAt'),
    updateAt: selector(state, 'updateAt'),
    examMedicaments: selector(state, 'examMedicaments'),
    examEquipments: selector(state, 'examEquipments'),
    examRequest: selector(state, 'examRequest'),
    patientId: selector(state, 'patient.id')
})

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ExamForm)