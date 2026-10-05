import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchExam as onSubmit, clearSearchExam } from './examActions'
import LabelAndInput from '../common/form/labelAndInput'
import If from '../common/operator/if'
import Pagination from '../common/pagination/pagination'
import { onlyNumbers, upper } from '../common/form/formatValues'

class ExamSearch extends Component {

    componentWillUnmount() {
        this.props.clearSearchExam();
    }

    onPageChange = (page) => {
        this.props.onSubmit(this.props.formValues, page);
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
                <Form role='form' onSubmit={handleSubmit} className='card'>
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
                            <table className='table'>
                                <thead>
                                    <tr>
                                        <th>Exame ID</th>
                                        <th>Leito</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {this.renderRows()}
                                </tbody>
                            </table>
                            <Pagination page={this.props.pagination.number}
                                totalPages={this.props.pagination.totalPages}
                                totalElements={this.props.pagination.totalElements}
                                onPageChange={this.onPageChange} />
                        </div>
                    </If>
                </Form>
            </div>
        )
    }
}

ExamSearch = reduxForm({ form: 'examSearch' })(ExamSearch)
const SEARCH_FIELDS = ['id', 'bed'];
const selector = formValueSelector('examSearch')
const mapStateToProps = state => ({
    list: state.exam.resultSearch,
    pagination: state.exam.pagination,
    formValues: selector(state, ...SEARCH_FIELDS)
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchExam }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ExamSearch)