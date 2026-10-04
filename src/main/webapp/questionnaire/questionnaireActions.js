import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, arrayInsert, arrayRemove } from 'redux-form'

import { BASE_URL_QUESTIONNAIRE } from '../constants'

const FORM_NAME = 'questionnaireForm';

const INITIAL_VALUES = { questions: [{}] };

const DEFAULT_PAGE_SIZE = 10;

export function toQuestionnairePayload(values) {
    return {
        id: values.id,
        title: values.title,
        description: values.description,
        questions: toQuestionPayload(values.questions)
    }
}

function toQuestionPayload(questions) {
    return (questions || [])
        .filter(item => !!item && !!item.question)
        .map((item, index) => ({
            questionId: item.question.id,
            sortOrder: !!item.sortOrder ? Number(item.sortOrder) : index + 1
        }));
}

export function toQuestionnaireFormValues(response) {
    var questions = (response.questions || []).map(link => ({
        id: link.id,
        question: link.question,
        sortOrder: link.sortOrder
    }));
    return {
        ...response,
        questions: questions.length > 0 ? questions : [{}]
    }
}

export function getQuestionnaireById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_QUESTIONNAIRE}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch(initialize(FORM_NAME, toQuestionnaireFormValues(resp.data)));
                }
                else
                    toastr.error('Erro', `Questionário de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o Questionário (${id}): \n` + e.response.data.message)
            })
    }
}

export function submitQuestionnaire(values) {

    validateQuestionnaire(values);

    if (!!values.id) {
        return update(values)
    } else {
        return create(values)
    }
}

export function searchQuestionnaires(values, page) {

    const pageNumber = typeof page === 'number' ? page : 0;
    return dispatch => {
        var params = `page=${pageNumber}&size=${DEFAULT_PAGE_SIZE}`
            + (!!values.id ? `&id=${values.id}` : '')
            + (!!values.title ? `&title=${encodeURIComponent(values.title)}` : '');
        axios.get(`${BASE_URL_QUESTIONNAIRE}?${params}`)
            .then(resp => {
                if (!!resp.data) {
                    if (resp.data.content.length > 0) {
                        dispatch({ type: 'QUESTIONNAIRE_SEARCH_RESULT', payload: resp.data });
                    }
                    else
                        toastr.warning('Atenção', `Nenhum resultado foi encontrado!`)
                }
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao consultar: \n` + e.response.data.message)
            })
    }
}

export function clearSearchQuestionnaires() {

    return dispatch => {
        dispatch({ type: 'QUESTIONNAIRE_SEARCH_RESULT', payload: [] });
    }
}

export function addQuestionItem(index, item) {
    return arrayInsert(FORM_NAME, 'questions', index, item || {});
}

export function removeQuestionItem(index) {
    return arrayRemove(FORM_NAME, 'questions', index);
}

export function removeQuestionnaire(values) {
    return dispatch => {
        axios.delete(`${BASE_URL_QUESTIONNAIRE}/${values.id}`)
            .then(() => {
                toastr.success('Sucesso', `Questionário removido com sucesso.`)
                dispatch([resetForm(FORM_NAME), init()]);
                window.location = `/#/questionnaire/search`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao remover o Questionário (${values.id}): \n` + e.response.data.message)
            })
    }
}

function validateQuestionnaire(values) {
    var errors = {}
    var hasError = false;

    if (!values.title) {
        errors.title = 'O título é obrigatório!'
        hasError = true;
    }

    var selected = (values.questions || []).filter(item => !!item && !!item.question);
    if (selected.length === 0) {
        errors._error = 'O questionário deve ter ao menos uma questão vinculada!'
        hasError = true;
    } else {
        var ids = selected.map(item => item.question.id);
        if (ids.length !== new Set(ids).size) {
            errors._error = 'O questionário não pode ter questões repetidas!'
            hasError = true;
        }
    }

    if (hasError) {
        if (!errors._error) {
            errors._error = 'Preencha todos os campos obrigatórios!';
        }
        throw new SubmissionError(errors);
    }
}

function update(values) {
    return dispatch => {
        axios.put(`${BASE_URL_QUESTIONNAIRE}/${values.id}`, toQuestionnairePayload(values))
            .then(resp => {
                toastr.success('Sucesso', `Questionário atualizado com sucesso.`);
                dispatch(initialize(FORM_NAME, toQuestionnaireFormValues(resp.data)));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar o Questionário (${values.id}): \n` + e.response.data.message)
            })
    }
}

function create(values) {
    return dispatch => {
        axios.post(`${BASE_URL_QUESTIONNAIRE}`, toQuestionnairePayload(values))
            .then(resp => {
                toastr.success('Sucesso', `Questionário cadastrado com sucesso.`)
                dispatch(resetForm(FORM_NAME));
                window.location = `/#/questionnaire/${resp.data.id}`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar o Questionário: \n` + e.response.data.message)
            })
    }
}

export function init() {
    return initialize(FORM_NAME, INITIAL_VALUES);
}
