import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchPatients as onSubmit, clearSearchPatients } from './patientActions'
import LabelAndInput from '../common/form/labelAndInput'
import If from '../common/operator/if'


class PatientSearch extends Component {

    componentWillUnmount() {
        this.props.clearSearchPatients();
    }

    renderRows() {
        const list = this.props.list || []
        return list.map(patient => (
            <tr key={patient.id}>
                <td><Link to={`/patient/${patient.id}`}>{patient.name}</Link></td>
                <td><Link to={`/patient/${patient.id}`}>{patient.documentNumber}</Link></td>
            </tr>
        ))
    }

    render() {
        const { handleSubmit, pristine, submitting, resetForm, list } = this.props;
        const upper = value => value && value.toUpperCase();

        return (
            <div>
                <Form role='form' onSubmit={handleSubmit} className='box box-solid'>
                    <div className='box'>
                        <div className='box-body'>
                            <fieldset>
                                <Field name='name' component={LabelAndInput} readOnly={false}
                                    label='Nome' cols='12 8' placeholder='Informe o nome' normalize={upper} />
                                <Field name='documentNumber' component={LabelAndInput} readOnly={false}
                                    label='CPF' cols='12 4' placeholder='Informe o CPF' />
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
                                onClick={resetForm}
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
                                        <th>Nome</th>
                                        <th>CPF</th>
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

PatientSearch = reduxForm({ form: 'patientSearch' })(PatientSearch)
const mapStateToProps = state => ({
    list: state.patient.resultSearch
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchPatients }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(PatientSearch)