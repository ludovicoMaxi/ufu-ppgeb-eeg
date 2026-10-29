import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchPatients as onSubmit, clearSearchPatients } from './patientActions'
import LabelAndInput from '../common/form/labelAndInput'
import If from '../common/operator/if'
import Pagination, { PAGE_SIZE_OPTIONS } from '../common/pagination/pagination'
import exportCsv from '../common/table/exportCsv'
import { upper, documentNumber as documentNumberMask } from '../common/form/formatValues'

class PatientSearch extends Component {

    state = { pageSize: PAGE_SIZE_OPTIONS[0] }

    componentWillUnmount() {
        this.props.clearSearchPatients();
    }

    submit = values => {
        this.props.onSubmit(values, 0, this.state.pageSize);
    }

    onPageChange = (page) => {
        this.props.onSubmit(this.props.formValues, page, this.state.pageSize);
    }

    onPageSizeChange = (pageSize) => {
        this.setState({ pageSize });
        this.props.onSubmit(this.props.formValues, 0, pageSize);
    }

    onExport = () => {
        exportCsv('pacientes.csv', EXPORT_COLUMNS, this.props.list);
    }

    renderRows() {
        const list = this.props.list || []
        return list.map(patient => (
            <tr key={patient.id}>
                <td><Link to={`/patient/${patient.id}`}>{patient.name}</Link></td>
                <td><Link to={`/patient/${patient.id}`}>{documentNumberMask(patient.documentNumber)}</Link></td>
            </tr>
        ))
    }

    render() {
        const { handleSubmit, pristine, submitting, reset, list } = this.props;

        return (
            <div>
                <Form role='form' onSubmit={handleSubmit(this.submit)} className='card'>
                    <div>
                        <div className='card-body'>
                            <fieldset>
                                <div className='row'>
                                    <Field name='name' component={LabelAndInput} readOnly={false}
                                        label='Nome' cols='12 8' placeholder='Informe o nome' normalize={upper} />
                                    <Field name='documentNumber' component={LabelAndInput} readOnly={false}
                                        label='CPF' cols='12 4' placeholder='Informe o CPF' normalize={documentNumberMask} />
                                </div>
                                <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                            </fieldset>
                        </div>
                        {this.props.error && <strong style={{ 'color': 'red' }}>{this.props.error}</strong>}
                        <div className='card-footer'>
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
                            <div className='table-responsive'><table className='table table-hover align-middle'>
                                <thead>
                                    <tr>
                                        <th>Nome</th>
                                        <th>CPF</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {this.renderRows()}
                                </tbody>
                            </table></div>
                            <Pagination page={this.props.pagination.number}
                                totalPages={this.props.pagination.totalPages}
                                totalElements={this.props.pagination.totalElements}
                                onPageChange={this.onPageChange}
                                pageSize={this.state.pageSize}
                                onPageSizeChange={this.onPageSizeChange}
                                onExport={this.onExport} />
                        </div>
                    </If>
                </Form>
            </div>
        )
    }
}

PatientSearch = reduxForm({ form: 'patientSearch' })(PatientSearch)
const SEARCH_FIELDS = ['name', 'documentNumber'];
const EXPORT_COLUMNS = [
    { label: 'Nome', value: patient => patient.name },
    { label: 'CPF', value: patient => documentNumberMask(patient.documentNumber) }
];
const selector = formValueSelector('patientSearch')
const mapStateToProps = state => ({
    list: state.patient.resultSearch,
    pagination: state.patient.pagination,
    formValues: selector(state, ...SEARCH_FIELDS)
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchPatients }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(PatientSearch)