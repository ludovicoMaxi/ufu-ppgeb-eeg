import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchQuestionnaires as onSubmit, clearSearchQuestionnaires } from './questionnaireActions'
import LabelAndInput from '../common/form/labelAndInput'
import If from '../common/operator/if'
import Pagination from '../common/pagination/pagination'
import { onlyNumbers } from '../common/form/formatValues'

class QuestionnaireSearch extends Component {

    componentWillUnmount() {
        this.props.clearSearchQuestionnaires();
    }

    submit = values => {
        this.props.onSubmit(values, 0);
    }

    onPageChange = (page) => {
        this.props.onSubmit(this.props.formValues, page);
    }

    renderRows() {
        const list = this.props.list || []
        return list.map(questionnaire => (
            <tr key={questionnaire.id}>
                <td><Link to={`/questionnaire/${questionnaire.id}`}>{questionnaire.id}</Link></td>
                <td><Link to={`/questionnaire/${questionnaire.id}`}>{questionnaire.title}</Link></td>
                <td>{questionnaire.description}</td>
                <td>{(questionnaire.questions || []).length}</td>
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
                                        label='Questionário id' cols='4 2' placeholder='Identificador' normalize={onlyNumbers} />
                                    <Field name='title' component={LabelAndInput} readOnly={false}
                                        label='Título' cols='8 6' placeholder='Informe o título' />
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
                            <Link to='/questionnaire/add' className='btn btn-success'>
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
                                        <th>Questionário ID</th>
                                        <th>Título</th>
                                        <th>Descrição</th>
                                        <th>Questões</th>
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

QuestionnaireSearch = reduxForm({ form: 'questionnaireSearch' })(QuestionnaireSearch)
const SEARCH_FIELDS = ['id', 'title'];
const selector = formValueSelector('questionnaireSearch')
const mapStateToProps = state => ({
    list: state.questionnaire.resultSearch,
    pagination: state.questionnaire.pagination,
    formValues: selector(state, ...SEARCH_FIELDS)
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchQuestionnaires }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(QuestionnaireSearch)
