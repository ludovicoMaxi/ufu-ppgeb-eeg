import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchAnswerOptions as onSubmit, clearSearchAnswerOptions, ACTIVE_OPTIONS } from './answerOptionActions'
import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputSelect from '../common/form/labelAndInputSelect'
import If from '../common/operator/if'
import Pagination from '../common/pagination/pagination'
import { onlyNumbers } from '../common/form/formatValues'

class AnswerOptionSearch extends Component {

    componentDidMount() {
        this.props.onSubmit({}, 0);
    }

    componentWillUnmount() {
        this.props.clearSearchAnswerOptions();
    }

    submit = values => {
        this.props.onSubmit(values, 0);
    }

    onPageChange = (page) => {
        this.props.onSubmit(this.props.formValues, page);
    }

    renderRows() {
        const list = this.props.list || []
        return list.map(answerOption => (
            <tr key={answerOption.id}>
                <td><Link to={`/answer-option/${answerOption.id}`}>{answerOption.id}</Link></td>
                <td><Link to={`/answer-option/${answerOption.id}`}>{answerOption.text}</Link></td>
                <td>{answerOption.active ? 'Sim' : 'Não'}</td>
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
                                <legend>Alternativas de Resposta</legend>
                                <span className='text-secondary'>
                                    As alternativas são o catálogo reutilizado pelas questões de seleção.
                                </span>
                                <div className='row mt-2'>
                                    <Field name='id' component={LabelAndInput} readOnly={false}
                                        label='Alternativa id' cols='4 2' placeholder='Identificador' normalize={onlyNumbers} />
                                    <Field name='text' component={LabelAndInput} readOnly={false}
                                        label='Texto' cols='8 7' placeholder='Informe o texto' />
                                    <Field name='active' component={LabelAndInputSelect} readOnly={false}
                                        label='Situação' cols='6 3' placeholder='Situação' options={ACTIVE_OPTIONS} />
                                </div>
                                <hr style={{ 'marginTop': '10px', 'marginBottom': '0px' }} />
                            </fieldset>
                        </div>
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
                            <Link to='/answer-option/add' className='btn btn-success'>
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
                                        <th>Alternativa ID</th>
                                        <th>Texto</th>
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

AnswerOptionSearch = reduxForm({ form: 'answerOptionSearch' })(AnswerOptionSearch)
const selector = formValueSelector('answerOptionSearch')
const mapStateToProps = state => ({
    list: state.answerOption.resultSearch,
    pagination: state.answerOption.pagination,
    formValues: {
        id: selector(state, 'id'),
        text: selector(state, 'text'),
        active: selector(state, 'active')
    }
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchAnswerOptions }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(AnswerOptionSearch)
