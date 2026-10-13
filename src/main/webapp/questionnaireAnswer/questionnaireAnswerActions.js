import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { initialize, SubmissionError } from 'redux-form'

import { BASE_URL_EXAM, BASE_URL_QUESTIONNAIRE } from '../constants'
import { FREE_TEXT } from '../question/questionActions'

const FORM_NAME = 'questionnaireAnswerForm';

const OPTIONS_PAGE_SIZE = 200;
const APPLIED_PAGE_SIZE = 200;

const INITIAL_VALUES = { answers: [] };

export function answersUrl(examId, questionnaireId) {
    return `${BASE_URL_EXAM}/${examId}/questionnaires/${questionnaireId}/answers`
}

export function answerOptionLabel(answerOption) {
    return answerOption.active === false ? `${answerOption.text} (inativa)` : answerOption.text
}

export function toAnswerFormValues(examId, questionnaireId, questionnaire, answers) {

    const answerByQuestion = new Map();
    ((answers || []).filter(answer => !!answer && !!answer.questionId))
        .forEach(answer => answerByQuestion.set(`${answer.questionId}`, answer));

    const questions = (((questionnaire || {}).questions || [])
        .filter(link => !!link && !!link.question)
        .sort((first, second) => linkOrder(first) - linkOrder(second)))
        .map(link => toAnswerFormItem(link.question, answerByQuestion.get(`${link.question.id}`)));

    return { ...INITIAL_VALUES, examId: examId, questionnaireId: questionnaireId, answers: questions };
}

export function toAnswerFormItem(question, answer) {

    const selectedIds = (!!answer && !!answer.answerOptions)
        ? answer.answerOptions.map(option => option.id) : [];

    return {
        id: !!answer ? answer.id : undefined,
        questionId: question.id,
        questionText: question.text,
        questionType: question.questionType,
        options: toOptionItems(question),
        answerOptionIds: selectedIds,
        freeText: !!answer && !!answer.freeText ? answer.freeText : ''
    }
}

export function toOptionItems(question) {

    return ((question.options || [])
        .filter(link => !!link && !!link.answerOption)
        .sort((first, second) => linkOrder(first) - linkOrder(second))
        .map(link => ({ value: link.answerOption.id, label: answerOptionLabel(link.answerOption) })));
}

export function toAnswerOptionIds(answerOptionIds) {

    if (Array.isArray(answerOptionIds)) {
        return answerOptionIds;
    }

    if (answerOptionIds === undefined || answerOptionIds === null || answerOptionIds === '') {
        return [];
    }

    return [answerOptionIds];
}

export function toAnswerPayload(values) {

    return ((values.answers || []).filter(answer => isAnswered(answer)))
        .map(answer => ({
            id: answer.id,
            questionId: answer.questionId,
            freeText: !!answer.freeText ? answer.freeText : null,
            answerOptionIds: toAnswerOptionIds(answer.answerOptionIds).map(optionId => Number(optionId))
        }));
}

export function isAnswered(answer) {

    if (!answer || !answer.questionId) {
        return false;
    }

    if (answer.questionType === FREE_TEXT) {
        return !!answer.freeText;
    }

    return toAnswerOptionIds(answer.answerOptionIds).length > 0;
}

export function loadQuestionnaire(examId, questionnaireId) {

    return dispatch => {
        if (!questionnaireId) {
            dispatch(initialize(FORM_NAME, { ...INITIAL_VALUES, examId: examId }));
            return;
        }

        Promise.all([
            axios.get(`${BASE_URL_QUESTIONNAIRE}/${questionnaireId}`),
            axios.get(answersUrl(examId, questionnaireId))
        ])
            .then(responses => {
                dispatch(initialize(FORM_NAME, toAnswerFormValues(examId, questionnaireId,
                    responses[0].data, responses[1].data)));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o questionário (${questionnaireId}): \n`
                    + errorMessage(e));
            })
    }
}

export function getOptionsQuestionnaireToAnswer() {

    return dispatch => {
        axios.get(`${BASE_URL_QUESTIONNAIRE}?page=0&size=${OPTIONS_PAGE_SIZE}`)
            .then(resp => {
                const content = !!resp.data && !!resp.data.content ? resp.data.content : [];
                dispatch({
                    type: 'QUESTIONNAIRE_TO_ANSWER_OPTIONS',
                    payload: content.map(item => ({ value: item.id, label: item.title }))
                });
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar os questionários: \n` + errorMessage(e));
            })
    }
}

export function getAppliedQuestionnaires(examId) {

    return dispatch => {
        axios.get(`${BASE_URL_EXAM}/${examId}/questionnaires?page=0&size=${APPLIED_PAGE_SIZE}`)
            .then(resp => {
                const content = !!resp.data && !!resp.data.content ? resp.data.content : [];
                dispatch({ type: 'QUESTIONNAIRE_TO_ANSWER_APPLIED', payload: content });
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar os questionários aplicados: \n`
                    + errorMessage(e));
            })
    }
}

export function submitAnswers(values) {

    if (!values.questionnaireId) {
        throw new SubmissionError({ questionnaireId: 'Selecione o questionário para registrar as respostas!' });
    }

    return dispatch => {
        axios.put(answersUrl(values.examId, values.questionnaireId), toAnswerPayload(values))
            .then(() => {
                toastr.success('Sucesso', `Respostas do questionário salvas com sucesso.`);
                dispatch(getAppliedQuestionnaires(values.examId));
                dispatch(loadQuestionnaire(values.examId, values.questionnaireId));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao salvar as respostas do questionário: \n`
                    + errorMessage(e));
            })
    }
}

export function init(examId) {

    return dispatch => {
        dispatch(initialize(FORM_NAME, { ...INITIAL_VALUES, examId: examId }));
        dispatch(getOptionsQuestionnaireToAnswer());
        dispatch(getAppliedQuestionnaires(examId));
    }
}

function linkOrder(link) {
    return !!link.sortOrder ? Number(link.sortOrder) : 0;
}

function errorMessage(e) {

    const data = !!e && !!e.response ? e.response.data : null;

    if (!!data && !!data.message) {
        return data.message;
    }

    if (!!data && !!data.detail) {
        return data.detail;
    }

    return 'Tente novamente mais tarde.';
}