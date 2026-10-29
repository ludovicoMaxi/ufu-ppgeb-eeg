import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError } from 'redux-form'

import { BASE_URL_ANSWER_OPTION } from '../constants'
import { errorMessage } from '../common/msg/errorMessage'

const INITIAL_VALUES = { active: true };

const OPTIONS_PAGE_SIZE = 200;

export function toAnswerOptionPayload(values) {
    return {
        id: values.id,
        text: values.text,
        active: !!values.active
    }
}

export function getAnswerOptionById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_ANSWER_OPTION}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch(initialize('answerOptionForm', resp.data));
                }
                else
                    toastr.error('Erro', `Alternativa de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar a Alternativa (${id}): \n` + errorMessage(e))
            })
    }
}

export function getOptionsAnswerOption() {
    return dispatch => {
        axios.get(`${BASE_URL_ANSWER_OPTION}?page=0&size=${OPTIONS_PAGE_SIZE}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'OPTIONS_ANSWER_OPTION', payload: resp.data.content });
                }
                else
                    toastr.error('Erro', `Não existem alternativas de resposta!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar as alternativas: \n` + errorMessage(e))
            })
    }
}

export function submitAnswerOption(values) {

    const errors = {}
    if (!values.text) {
        errors.text = 'O texto é obrigatório!'
    }

    if (Object.keys(errors).length !== 0) {
        errors._error = 'Preencha todos os campos obrigatórios!';
        throw new SubmissionError(errors);
    }

    if (!!values.id) {
        return update(values)
    } else {
        return create(values)
    }
}

export const ACTIVE_OPTIONS = [
    { 'value': true, 'label': 'Ativa' },
    { 'value': false, 'label': 'Inativa' }
];

export function removeAnswerOption(values) {
    return dispatch => {
        axios.delete(`${BASE_URL_ANSWER_OPTION}/${values.id}`)
            .then(() => {
                toastr.success('Sucesso', `Alternativa removida com sucesso.`)
                dispatch([resetForm('answerOptionForm'), init()]);
                window.location = `/#/answer-option/search`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao remover a Alternativa (${values.id}): \n` + errorMessage(e))
            })
    }
}

function update(values) {
    return dispatch => {
        axios.put(`${BASE_URL_ANSWER_OPTION}/${values.id}`, toAnswerOptionPayload(values))
            .then(resp => {
                toastr.success('Sucesso', `Alternativa atualizada com sucesso.`);
                dispatch(initialize('answerOptionForm', resp.data));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar a Alternativa (${values.id}): \n` + errorMessage(e))
            })
    }
}

function create(values) {
    return dispatch => {
        axios.post(`${BASE_URL_ANSWER_OPTION}`, toAnswerOptionPayload(values))
            .then(resp => {
                toastr.success('Sucesso', `Alternativa cadastrada com sucesso.`)
                dispatch(resetForm('answerOptionForm'));
                window.location = `/#/answer-option/${resp.data.id}`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar a Alternativa: \n` + errorMessage(e))
            })
    }
}

export function init() {
    return initialize('answerOptionForm', INITIAL_VALUES);
}
