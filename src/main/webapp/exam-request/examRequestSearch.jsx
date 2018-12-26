import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchExamRequest as onSubmit, clearSearchExamRequest } from './examRequestActions'
import LabelAndInput from '../common/form/labelAndInput'
import If from '../common/operator/if'
import { onlyNumbers, upper } from '../common/form/formatValues'

class ExamRequestSearch extends Component {

    componentWillUnmount() {
        this.props.clearSearchExamRequest();
    }

    renderRows() {
        const list = this.props.list || []
        return list.map(examRequest => (
            <tr key={examRequest.id}>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.medicalRecord}</Link></td>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.medicalRequest}</Link></td>
                <td><Link to={`/exam-request/${examRequest.id}`}>{examRequest.doctorRequestant}</Link></td>
            </tr>
        ))
    }

    render() {
        const { handleSubmit, pristine, submitting, reset, list } = this.props;

        return (
            <div>
                <Form role='form' onSubmit={handleSubmit} className='box box-solid'>
                    <div className='box'>
                        <div className='box-body'>
                            <fieldset>
                                <Field name='medicalRecord' component={LabelAndInput} readOnly={false}
                                    label='Prontuário ID' cols='6 3' placeholder='Prontuário ID' normalize={onlyNumbers} />
                                <Field name='medicalRequest' component={LabelAndInput} readOnly={false}
                                    label='Requisição ID' cols='6 3' placeholder='Requisição ID' normalize={onlyNumbers} />
                                <Field name='doctorRequestant' component={LabelAndInput} readOnly={false} normalize={upper}
                                    label='Medico Solicitante' cols='12 6' placeholder='Informe o Médico' />
                                <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                            </fieldset>
                        </div>
                        {this.props.error && <strong style={{ 'color': 'red' }}>{this.props.error}</strong>}
                        <div className='box-footer'>
                            <button type='submit'
                                className={`btn btn-primary`}
                                disabled={submitting}>
                                Buscar
                        </button>
                            <button type='button' className='btn btn-default'
                                onClick={reset}
                                disabled={pristine || submitting}>
                                {'Limpar'}
                            </button>
                        </div>
                    </div>

                    <If test={!!list && list.length > 0}>
                        <div>
                            <h3>Resultado da Busca</h3>
                            <table className='table'>
                                <thead>
                                    <tr>
                                        <th>Prontuário ID</th>
                                        <th>Requisição ID</th>
                                        <th>Medico Solicitante</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {this.renderRows()}
                                </tbody>
                            </table>
                        </div>
                    </If>
                </Form>
            </div>
        )
    }
}

ExamRequestSearch = reduxForm({ form: 'examRequestSearch' })(ExamRequestSearch)
const mapStateToProps = state => ({
    list: state.examRequest.resultSearch
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchExamRequest }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ExamRequestSearch)