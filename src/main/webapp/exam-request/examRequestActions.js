import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, change as changeFieldValue } from 'redux-form'

import { showTabs, selectTab } from '../common/tab/tabActions'
import { BASE_URL_EXAM_REQUEST } from '../constants'

const INITIAL_VALUES = {};

export function getExamRequestById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_EXAM_REQUEST}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch([initialize('examRequestForm', resp.data), initialize('patientForm', resp.data.patient)]);
                }
                else
                    toastr.error('Erro', `Requerimento de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o Requerimento (${id}): \n` + e.response.data.message)
            })
    }
}

export function submitExamRequest(values) {

    const errors = {}
    if (!values.medicalRecord) {
        errors.medicalRecord = 'Prontuário ID é obrigatório!'
    }

    if (!values.medicalRequest) {
        errors.medicalRequest = 'Requisição ID é obrigatório!'
    }

    if (!values.sector) {
        errors.sector = 'Setor é obrigatório!'
    }

    if (!values.doctorRequestant) {
        errors.doctorRequestant = 'Medico Solicitante é Obrigatório!'
    }

    if (!values.user) {
        errors.user = 'Usuario é obrigatório!'
    }

    if (!values.requestDate) {
        errors.requestDate = 'Data do Pedido é obrigatório!'
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

export function searchExamRequest(values) {

    var errors = {}

    if (!values.medicalRecord && !values.medicalRequest && !values.doctorRequestant) {
        errors.medicalRecord = ' ';
        errors.medicalRequest = ' ';
        errors.doctorRequestant = ' ';
        errors._error = 'Preencha pelo menos um campo para consultar!';
        throw new SubmissionError(errors);
    }

    return dispatch => {
        var params = (!!values.medicalRecord ? `medicalRecord=${values.medicalRecord}` : '')
            + (!!values.medicalRequest ? `medicalRequest=${values.medicalRequest}` : '')
            + (!!values.doctorRequestant ? `doctorRequestant=${values.doctorRequestant}` : '');
        axios.get(`${BASE_URL_EXAM_REQUEST}?${params}`)
            .then(resp => {
                if (!!resp.data && resp.data.length > 0) {
                    dispatch({ type: 'EXAM_REQUEST_SEARCH_RESULT', payload: resp.data });
                }
                else
                    toastr.warning('Atenção', `Nenhum resultado foi encontrado!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao consultar: \n` + e.response.data.message)
            })
    }
}

export function clearSearchExamRequest() {

    return dispatch => {
        dispatch({ type: 'EXAM_REQUEST_SEARCH_RESULT', payload: [] });
    }
}

export function remove(values) {
    return submit(values, 'delete', values.id, 'Exclusão')
}

function update(values) {
    return dispatch => {
        axios.put(`${BASE_URL_EXAM_REQUEST}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Requerimento atualizado com sucesso.`);
                dispatch(initialize('examRequestForm', resp.data));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar o Requerimento (${values.id}): \n` + e.response.data.message)
            })
    }
}

function create(values) {
    return dispatch => {
        axios.post(`${BASE_URL_EXAM_REQUEST}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Requerimento cadastrado com sucesso.`)
                dispatch(resetForm('examRequestForm'));
                window.location = `/#/exam-request/${resp.data.id}`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar o Requerimento: \n` + e.response.data.message)
            })
    }
}

export function init() {
    return [
        initialize('examRequestForm', INITIAL_VALUES)
    ]
}

export function setPatientInForm(patient) {
    return changeFieldValue('examRequestForm', 'patient', patient);
}