import React, { Component } from 'react'
import { reduxForm, Field, Form } from 'redux-form'
import { Link } from 'react-router-dom'

import { ACTIVE_OPTIONS } from './answerOptionActions'
import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputNativeSelect from '../common/form/labelAndInputNativeSelect'
import RemoteTable from '../common/table/remoteTable'
import { onlyNumbers } from '../common/form/formatValues'
import { BASE_URL_ANSWER_OPTION } from '../constants'

const ANSWER_OPTION_PATH = '#/answer-option/'

const COLUMNS = [
    { title: 'Alternativa ID', field: 'id', width: 160,
        formatter: 'link', formatterParams: { urlPrefix: ANSWER_OPTION_PATH } },
    { title: 'Texto', field: 'text',
        formatter: 'link', formatterParams: { urlPrefix: ANSWER_OPTION_PATH, urlField: 'id', labelField: 'text' } },
    { title: 'Ativa', field: 'active', width: 120,
        formatter: cell => (cell.getValue() ? 'Sim' : 'Não') }
]

class AnswerOptionSearch extends Component {

    state = { filters: {} }

    submit = values => {
        this.setState({ filters: values });
    }

    render() {
        const { handleSubmit, pristine, submitting, reset } = this.props;

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
                                    <Field name='active' component={LabelAndInputNativeSelect} readOnly={false}
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
                </Form>

                <div className='card mt-3'>
                    <div className='card-body'>
                        <h3>Resultado da Busca</h3>
                        <RemoteTable url={BASE_URL_ANSWER_OPTION} columns={COLUMNS} filters={this.state.filters}
                            exportName='alternativas' />
                    </div>
                </div>
            </div>
        )
    }
}

export default reduxForm({ form: 'answerOptionSearch' })(AnswerOptionSearch)
