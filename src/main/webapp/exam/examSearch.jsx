import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, Form } from 'redux-form'
import { Link } from 'react-router-dom'

import { searchExam as onSubmit, clearSearchExam } from './examActions'
import LabelAndInput from '../common/form/labelAndInput'
import If from '../common/operator/if'
import { onlyNumbers, upper } from '../common/form/formatValues'

class ExamSearch extends Component {

    componentWillUnmount() {
        this.props.clearSearchExam();
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
                <Form role='form' onSubmit={handleSubmit} className='box box-solid'>
                    <div className='box'>
                        <div className='box-body'>
                            <fieldset>
                                <Field name='id' component={LabelAndInput} readOnly={false}
                                    label='Exame id' cols='4 2' placeholder='Identificador' normalize={onlyNumbers} />
                                <Field name='bed' component={LabelAndInput} readOnly={false}
                                    label='Leito' cols='8 6' placeholder='Leito' normalize={upper} />
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
                                        <th>Exame ID</th>
                                        <th>Leito</th>
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

ExamSearch = reduxForm({ form: 'examSearch' })(ExamSearch)
const mapStateToProps = state => ({
    list: state.exam.resultSearch
})

const mapDispatchToProps = dispatch => bindActionCreators({ onSubmit, clearSearchExam }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ExamSearch)