import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'
import { Link } from 'react-router-dom'

import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputSelect from '../common/form/labelAndInputSelect'
import LabelAndInputTextarea from '../common/form/labelAndInputTextarea'
import If from '../common/operator/if'
import SystemInfo from '../common/form/systemInfo'
import { onlyNumbers } from '../common/form/formatValues'
import { getOptionsQuestion, questionTypeLabel } from '../question/questionActions'
import { addQuestionItem, removeQuestionItem } from './questionnaireActions'

class QuestionnaireForm extends Component {

    componentDidMount() {
        this.props.getOptionsQuestion();
    }

    renderErrors(errors) {

        if (errors != undefined) {
            return errors.split('\n').map((item, key) => (
                <strong key={key} style={{ 'color': 'red' }}>{item}<br /></strong>
            ))
        } else {
            return false;
        }
    }

    add(index, item) {
        if (!this.props.readOnly) {
            this.props.addQuestionItem(index, item);
        }
    }

    remove(index) {
        if (!this.props.readOnly && (this.props.questions || []).length > 1) {
            this.props.removeQuestionItem(index);
        }
    }

    renderQuestionRows() {
        const { readOnly, optionsQuestion } = this.props;
        const list = this.props.questions || [{}];

        return list.map((item, index) => (
            <div className='card mb-3' key={index} style={{ 'width': '100%' }}>
                <div className='card-body'>
                    <div className='row'>
                        <Field name={`questions[${index}].question`} component={LabelAndInputSelect} readOnly={readOnly}
                            label='Questão' cols='12 6' placeholder='Questão' options={optionsQuestion} />
                        <Field name={`questions[${index}].sortOrder`} component={LabelAndInput} readOnly={readOnly}
                            label='Ordem' cols='6 2' placeholder='Ordem' normalize={onlyNumbers} />
                        <div className='col-6 col-sm-2'>
                            <label className='form-label'>Tipo</label>
                            <div className='form-control-plaintext'>
                                <If test={!!item.question && !!item.question.id}>
                                    <Link to={`/question/${item.question.id}`}>
                                        {questionTypeLabel(item.question.questionType)}
                                    </Link>
                                </If>
                            </div>
                        </div>
                        <If test={!readOnly}>
                            <div className='col-6 col-sm-2 actions-contacts'>
                                <button type='button' className='btn btn-success'
                                    onClick={() => this.add(index + 1)}>
                                    <i className='fa fa-plus'></i>
                                </button>
                                <button type='button' className='btn btn-warning'
                                    onClick={() => {
                                        var itemAdd = { ...item };
                                        itemAdd.id = undefined;
                                        this.add(index + 1, itemAdd);
                                    }}>
                                    <i className='fa fa-copy'></i>
                                </button>
                                <button type='button' className='btn btn-danger'
                                    onClick={() => this.remove(index)}>
                                    <i className='fa fa-trash-alt'></i>
                                </button>
                            </div>
                        </If>
                    </div>
                </div>
            </div>
        ))
    }

    render() {
        const {
            handleSubmit,
            readOnly,
            pristine,
            submitting,
            submitClass,
            submitLabel,
            showSystemInfo,
            showRemove,
            reset,
            id,
            onRemove
        } = this.props;

        return (
            <form role='form' onSubmit={handleSubmit} className='card'>
                <div>
                    <div className='card-body'>
                        <fieldset>
                            <legend>Questionário</legend>
                            <div className='row'>
                                <Field name='title' component={LabelAndInput} readOnly={readOnly}
                                    label='Título' cols='12 8' placeholder='Informe o título' />
                                <Field name='description' component={LabelAndInputTextarea} readOnly={readOnly}
                                    label='Descrição' cols='12 12' placeholder='Informe a descrição' />
                            </div>
                            <If test={showSystemInfo}>
                                <SystemInfo formName='questionnaireForm' />
                            </If>
                            <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                        </fieldset>
                    </div>

                    <div className='card-body' style={{ 'paddingTop': '0px' }}>
                        <fieldset>
                            <legend>Questões</legend>
                            {this.renderQuestionRows()}
                        </fieldset>
                    </div>

                    <If test={!!this.props.error}>
                        <div className='card-body' style={{ 'paddingTop': '0px' }}>
                            {this.renderErrors(this.props.error)}
                        </div>
                    </If>

                    <If test={!readOnly}>
                        <div className='card-footer'>
                            <button type='submit'
                                className={`btn btn-${submitClass}`}
                                disabled={submitting}>
                                {submitLabel}
                            </button>
                            <button type='button' className='btn btn-default'
                                onClick={reset}
                                disabled={pristine || submitting}>
                                {'Cancelar'}
                            </button>
                            <If test={showRemove && !!id}>
                                <button type='button' className='btn btn-danger float-end'
                                    onClick={() => onRemove({ id })}
                                    disabled={submitting}>
                                    {'Remover'}
                                </button>
                            </If>
                        </div>
                    </If>
                </div>
            </form>
        )
    }
}

QuestionnaireForm = reduxForm({ form: 'questionnaireForm', destroyOnUnmount: false })(QuestionnaireForm)
const selector = formValueSelector('questionnaireForm')
const mapStateToProps = state => ({
    optionsQuestion: state.question.optionsQuestion,
    id: selector(state, 'id'),
    questions: selector(state, 'questions')
})

const mapDispatchToProps = dispatch => bindActionCreators(
    { getOptionsQuestion, addQuestionItem, removeQuestionItem }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(QuestionnaireForm)
