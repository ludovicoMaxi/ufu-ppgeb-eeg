import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, arrayInsert, arrayRemove } from 'redux-form'

import { BASE_URL_QUESTION } from '../constants'

const FORM_NAME = 'questionForm';

const INITIAL_VALUES = { active: true, options: [{}] };

const DEFAULT_PAGE_SIZE = 10;
const DEFAULT_OPTIONS_PAGE_SIZE = 200;

export const SINGLE_SELECT = 'SINGLE_SELECT';
export const MULTI_SELECT = 'MULTI_SELECT';
export const FREE_TEXT = 'FREE_TEXT';

export const QUESTION_TYPE_OPTIONS = [
    { 'value': SINGLE_SELECT, 'label': 'Seleção única' },
    { 'value': MULTI_SELECT, 'label': 'Seleção múltipla' },
    { 'value': FREE_TEXT, 'label': 'Texto livre' }
];

export const ACTIVE_OPTIONS = [
    { 'value': true, 'label': 'Ativa' },
    { 'value': false, 'label': 'Inativa' }
];

export function questionTypeLabel(questionType) {
    const found = QUESTION_TYPE_OPTIONS.filter(option => option.value === questionType);
    return found.length > 0 ? found[0].label : questionType;
}

export function toQuestionPayload(values) {
    return {
        id: values.id,
        text: values.text,
        description: values.description,
        questionType: values.questionType,
        active: !!values.active,
        options: values.questionType === FREE_TEXT ? [] : toOptionPayload(values.options)
    }
}

function toOptionPayload(options) {
    return (options || [])
        .filter(item => !!item && !!item.answerOption)
        .map((item, index) => ({
            answerOptionId: item.answerOption.id,
            sortOrder: !!item.sortOrder ? Number(item.sortOrder) : index + 1
        }));
}

export function toQuestionFormValues(response) {
    var options = (response.options || []).map(link => ({
        id: link.id,
        answerOption: link.answerOption,
        sortOrder: link.sortOrder
    }));
    return {
        ...response,
        options: options.length > 0 ? options : [{}]
    }
}

export function getQuestionById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_QUESTION}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch(initialize(FORM_NAME, toQuestionFormValues(resp.data)));
                }
                else
                    toastr.error('Erro', `Questão de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar a Questão (${id}): \n` + e.response.data.message)
            })
    }
}

export function getOptionsQuestion() {
    return dispatch => {
        axios.get(`${BASE_URL_QUESTION}?page=0&size=${DEFAULT_OPTIONS_PAGE_SIZE}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'OPTIONS_QUESTION', payload: resp.data.content });
                }
                else
                    toastr.error('Erro', `Não existem questões!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar as questões: \n` + e.response.data.message)
            })
    }
}

export function submitQuestion(values) {

    validateQuestion(values);

    if (!!values.id) {
        return update(values)
    } else {
        return create(values)
    }
}

export function searchQuestions(values, page) {

    const pageNumber = typeof page === 'number' ? page : 0;
    return dispatch => {
        var params = `page=${pageNumber}&size=${DEFAULT_PAGE_SIZE}`
            + (!!values.id ? `&id=${values.id}` : '')
            + (!!values.text ? `&text=${encodeURIComponent(values.text)}` : '')
            + (!!values.questionType ? `&questionType=${values.questionType}` : '')
            + (values.active === true || values.active === false ? `&active=${values.active}` : '');
        axios.get(`${BASE_URL_QUESTION}?${params}`)
            .then(resp => {
                if (!!resp.data) {
                    if (resp.data.content.length > 0) {
                        dispatch({ type: 'QUESTION_SEARCH_RESULT', payload: resp.data });
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

export function clearSearchQuestions() {

    return dispatch => {
        dispatch({ type: 'QUESTION_SEARCH_RESULT', payload: [] });
    }
}

export function addOptionItem(index, item) {
    return arrayInsert(FORM_NAME, 'options', index, item || {});
}

export function removeOptionItem(index) {
    return arrayRemove(FORM_NAME, 'options', index);
}

export function removeQuestion(values) {
    return dispatch => {
        axios.delete(`${BASE_URL_QUESTION}/${values.id}`)
            .then(() => {
                toastr.success('Sucesso', `Questão removida com sucesso.`)
                dispatch([resetForm(FORM_NAME), init()]);
                window.location = `/#/question/search`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao remover a Questão (${values.id}): \n` + e.response.data.message)
            })
    }
}

function validateQuestion(values) {
    var errors = {}
    var hasError = false;

    if (!values.text) {
        errors.text = 'O texto da questão é obrigatório!'
        hasError = true;
    }

    if (!values.questionType) {
        errors.questionType = 'O tipo da questão é obrigatório!'
        hasError = true;
    }

    var selected = (values.options || []).filter(item => !!item && !!item.answerOption);
    if (values.questionType === FREE_TEXT) {
        if (selected.length > 0) {
            errors._error = 'Questão de texto livre não pode ter alternativas vinculadas!'
            hasError = true;
        }
    } else if (!!values.questionType) {
        if (selected.length === 0) {
            errors._error = 'Questão de seleção deve ter ao menos uma alternativa vinculada!'
            hasError = true;
        } else {
            var ids = selected.map(item => item.answerOption.id);
            if (ids.length !== new Set(ids).size) {
                errors._error = 'A questão não pode ter alternativas repetidas!'
                hasError = true;
            }
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
        axios.put(`${BASE_URL_QUESTION}/${values.id}`, toQuestionPayload(values))
            .then(resp => {
                toastr.success('Sucesso', `Questão atualizada com sucesso.`);
                dispatch(initialize(FORM_NAME, toQuestionFormValues(resp.data)));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar a Questão (${values.id}): \n` + e.response.data.message)
            })
    }
}

function create(values) {
    return dispatch => {
        axios.post(`${BASE_URL_QUESTION}`, toQuestionPayload(values))
            .then(resp => {
                toastr.success('Sucesso', `Questão cadastrada com sucesso.`)
                dispatch(resetForm(FORM_NAME));
                window.location = `/#/question/${resp.data.id}`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar a Questão: \n` + e.response.data.message)
            })
    }
}

export function init() {
    return initialize(FORM_NAME, INITIAL_VALUES);
}
