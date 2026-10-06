import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { Field, reduxForm, formValueSelector } from 'redux-form'

import LabelAndInputSelect from '../common/form/labelAndInputSelect'
import LabelAndInputTextarea from '../common/form/labelAndInputTextarea'
import LabelAndInputRadioGroup from '../common/form/labelAndInputRadioGroup'
import LabelAndInputCheckboxGroup from '../common/form/labelAndInputCheckboxGroup'
import If from '../common/operator/if'
import { FREE_TEXT, MULTI_SELECT, SINGLE_SELECT } from '../question/questionActions'
import { init, loadQuestionnaire, submitAnswers } from './questionnaireAnswerActions'

class QuestionnaireAnswerListForm extends Component {

    componentDidMount() {
        this.props.init(this.props.examId);
    }

    componentDidUpdate(prevProps) {
        if (prevProps.examId !== this.props.examId) {
            this.props.init(this.props.examId);
        }
    }

    loadQuestionnaire(questionnaireId) {
        this.props.loadQuestionnaire(this.props.examId, questionnaireId);
    }

    renderObservation(index, readOnly) {

        return (
            <Field name={`answers[${index}].freeText`} component={LabelAndInputTextarea} readOnly={readOnly}
                label='Observação' cols='12 12' placeholder='Informe a observação (opcional)' />
        );
    }

    renderAnswerFields(answer, index, readOnly) {

        return (
            <div key={answer.questionId}>
                <If test={answer.questionType === FREE_TEXT}>
                    <Field name={`answers[${index}].freeText`} component={LabelAndInputTextarea} readOnly={readOnly}
                        label='Resposta' cols='12 12' placeholder='Informe a resposta' />
                </If>
                <If test={answer.questionType === SINGLE_SELECT}>
                    <Field name={`answers[${index}].answerOptionIds`} component={LabelAndInputRadioGroup}
                        readOnly={readOnly} label='Alternativa' cols='12 12' options={answer.options} />
                    {this.renderObservation(index, readOnly)}
                </If>
                <If test={answer.questionType === MULTI_SELECT}>
                    <Field name={`answers[${index}].answerOptionIds`} component={LabelAndInputCheckboxGroup}
                        readOnly={readOnly} label='Alternativas' cols='12 12' options={answer.options} />
                    {this.renderObservation(index, readOnly)}
                </If>
            </div>
        );
    }

    renderQuestions() {

        const { readOnly, answers, questionnaireId } = this.props;

        if (!questionnaireId) {
            return false;
        }

        return answers.map((answer, index) => (
            <div className='card mb-3' key={answer.questionId} style={{ 'width': '100%' }}>
                <legend>{`${index + 1}. ${answer.questionText}`}</legend>
                <div className='card-body'>
                    {this.renderAnswerFields(answer, index, readOnly)}
                </div>
            </div>
        ));
    }

    renderAppliedQuestionnaires() {

        const { appliedQuestionnaires, readOnly } = this.props;

        if (!appliedQuestionnaires || appliedQuestionnaires.length === 0) {
            return false;
        }

        return (
            <div className='card mb-3' style={{ 'width': '100%' }}>
                <legend>Questionários respondidos</legend>
                <div className='card-body'>
                    <ul className='list-group'>
                        {appliedQuestionnaires.map(questionnaire => (
                            <li className='list-group-item' key={questionnaire.id}>
                                {!!readOnly
                                    ? questionnaire.title
                                    : <a href='#' onClick={event => {
                                        event.preventDefault();
                                        this.loadQuestionnaire(questionnaire.id);
                                    }}>{questionnaire.title}</a>}
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        );
    }

    render() {

        const { handleSubmit, submitAnswers, readOnly, pristine, reset, submitting, questionnaireId } = this.props;

        return (
            <div>
                {this.renderAppliedQuestionnaires()}
                <form role='form' onSubmit={handleSubmit(submitAnswers)} className='card'>
                    <div className='card-body'>
                        <legend>Respostas do questionário</legend>
                        <Field name='questionnaireId' component={LabelAndInputSelect} readOnly={readOnly}
                            label='Questionário' cols='12 6' placeholder='Selecione o questionário'
                            options={this.props.optionsQuestionnaire}
                            onChange={(event, newValue) => this.loadQuestionnaire(newValue)} />
                        <If test={!questionnaireId}>
                            <div className='form-control-plaintext'>
                                Selecione um questionário para registrar as respostas do exame.
                            </div>
                        </If>
                        {this.renderQuestions()}
                    </div>
                    <If test={!readOnly}>
                        <div className='card-footer'>
                            <If test={!!questionnaireId}>
                                <button type='submit'
                                    className='btn btn-primary'
                                    disabled={submitting}>
                                    {'Salvar'}
                                </button>
                            </If>
                            <button type='button' className='btn btn-default'
                                onClick={reset}
                                disabled={pristine || submitting}>
                                {'Cancelar'}
                            </button>
                        </div>
                    </If>
                </form>
            </div>
        );
    }
}

QuestionnaireAnswerListForm = reduxForm({ form: 'questionnaireAnswerForm', destroyOnUnmount: false })(QuestionnaireAnswerListForm)
const selector = formValueSelector('questionnaireAnswerForm')
const mapStateToProps = state => ({
    questionnaireId: selector(state, 'questionnaireId'),
    answers: selector(state, 'answers'),
    optionsQuestionnaire: state.questionnaireAnswer.optionsQuestionnaire,
    appliedQuestionnaires: state.questionnaireAnswer.appliedQuestionnaires
});
const mapDispatchToProps = dispatch => bindActionCreators({
    init,
    loadQuestionnaire,
    submitAnswers
}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(QuestionnaireAnswerListForm)