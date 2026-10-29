import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchExam as onSubmit, clearSearchExam } from './examActions'
import LabelAndInput from '../common/form/labelAndInput'
import If from '../common/operator/if'
import Pagination, { PAGE_SIZE_OPTIONS } from '../common/pagination/pagination'
import exportCsv from '../common/table/exportCsv'
import { onlyNumbers, upper } from '../common/form/formatValues'

class ExamSearch extends Component {

    state = { pageSize: PAGE_SIZE_OPTIONS[0] }

    componentWillUnmount() {
        this.props.clearSearchExam();
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
        exportCsv('exames.csv', EXPORT_COLUMNS, this.props.list);
    }

    renderRows() {
        const list = this.props.list || []
        return list.map(exam => (
            <tr key={exam.id}>
                <td><Link to={`/exam/${exam.id}`}>{exam.id}</Link></td>
                <td><Link to={`/exam/${exam.id}`}>{exam.bed}</Link></td>
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
                                    <Field name='id' component={LabelAndInput} readOnly={false}
                                        label='Exame id' cols='4 2' placeholder='Identificador' normalize={onlyNumbers} />
                                    <Field name='bed' component={LabelAndInput} readOnly={false}
                                        label='Leito' cols='8 6' placeholder='Leito' normalize={upper} />
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
                                        <th>Exame ID</th>
                                        <th>Leito</th>
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

ExamSearch = reduxForm({ form: 'examSearch' })(ExamSearch)
const SEARCH_FIELDS = ['id', 'bed'];
const EXPORT_COLUMNS = [
    { label: 'Exame ID', value: exam => exam.id },
    { label: 'Leito', value: exam => exam.bed }
];
const selector = formValueSelector('examSearch')
const mapStateToProps = state => ({
    list: state.exam.resultSearch,
    pagination: state.exam.pagination,
    formValues: selector(state, ...SEARCH_FIELDS)
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchExam }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ExamSearch)