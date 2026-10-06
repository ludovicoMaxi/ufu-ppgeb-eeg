import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchQuestions as onSubmit, clearSearchQuestions, QUESTION_TYPE_OPTIONS, ACTIVE_OPTIONS, questionTypeLabel } from './questionActions'
import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputSelect from '../common/form/labelAndInputSelect'
import If from '../common/operator/if'
import Pagination from '../common/pagination/pagination'
import { onlyNumbers } from '../common/form/formatValues'

class QuestionSearch extends Component {

    componentWillUnmount() {
        this.props.clearSearchQuestions();
    }

    submit = values => {
        this.props.onSubmit(values, 0);
    }

    onPageChange = (page) => {
        this.props.onSubmit(this.props.formValues, page);
    }

    renderRows() {
        const list = this.props.list || []
        return list.map(question => (
            <tr key={question.id}>
                <td><Link to={`/question/${question.id}`}>{question.id}</Link></td>
                <td><Link to={`/question/${question.id}`}>{question.text}</Link></td>
                <td>{questionTypeLabel(question.questionType)}</td>
                <td>{question.active ? 'Sim' : 'Não'}</td>
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
                                        label='Questão id' cols='4 2' placeholder='Identificador' normalize={onlyNumbers} />
                                    <Field name='text' component={LabelAndInput} readOnly={false}
                                        label='Texto' cols='8 6' placeholder='Informe o texto' />
                                    <Field name='questionType' component={LabelAndInputSelect} readOnly={false}
                                        label='Tipo' cols='6 2' placeholder='Tipo' options={QUESTION_TYPE_OPTIONS} />
                                    <Field name='active' component={LabelAndInputSelect} readOnly={false}
                                        label='Situação' cols='6 2' placeholder='Situação' options={ACTIVE_OPTIONS} />
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
                            <Link to='/question/add' className='btn btn-success'>
                                Adicionar
                            </Link>
                        </div>
                    </div>

                    <If test={!!list && list.length > 0}>
                        <div>
                            <h3>Resultado da Busca</h3>
                            <table className='table'>
                                <thead>
                                    <tr>
                                        <th>Questão ID</th>
                                        <th>Texto</th>
                                        <th>Tipo</th>
                                        <th>Ativa</th>
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

QuestionSearch = reduxForm({ form: 'questionSearch' })(QuestionSearch)
const SEARCH_FIELDS = ['id', 'text', 'questionType', 'active'];
const selector = formValueSelector('questionSearch')
const mapStateToProps = state => ({
    list: state.question.resultSearch,
    pagination: state.question.pagination,
    formValues: selector(state, ...SEARCH_FIELDS)
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchQuestions }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(QuestionSearch)
