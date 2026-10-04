import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Form } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchAnswerOptions as onSubmit, clearSearchAnswerOptions } from './answerOptionActions'
import If from '../common/operator/if'
import Pagination from '../common/pagination/pagination'

class AnswerOptionSearch extends Component {

    componentDidMount() {
        this.props.onSubmit({}, 0);
    }

    componentWillUnmount() {
        this.props.clearSearchAnswerOptions();
    }

    onPageChange = (page) => {
        this.props.onSubmit({}, page);
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
        const { handleSubmit, submitting, list } = this.props;

        return (
            <div>
                <Form role='form' onSubmit={handleSubmit} className='card'>
                    <div>
                        <div className='card-body'>
                            <fieldset>
                                <legend>Alternativas de Resposta</legend>
                                <span className='text-secondary'>
                                    As alternativas são o catálogo reutilizado pelas questões de seleção.
                                </span>
                                <hr style={{ 'marginTop': '10px', 'marginBottom': '0px' }} />
                            </fieldset>
                        </div>
                        <div className='card-footer'>
                            <button type='submit'
                                className={`btn btn-primary`}
                                disabled={submitting}>
                                Buscar
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
const mapStateToProps = state => ({
    list: state.answerOption.resultSearch,
    pagination: state.answerOption.pagination
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchAnswerOptions }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(AnswerOptionSearch)
