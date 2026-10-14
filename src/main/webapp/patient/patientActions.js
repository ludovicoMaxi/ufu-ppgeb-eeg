import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError } from 'redux-form'
import { BASE_URL_PATIENT, BASE_URL_EXAM, BASE_URL_EXAM_REQUEST } from '../constants'
import { onlyNumbers, documentNumber as documentNumberMask } from '../common/form/formatValues'
import { errorMessage } from '../common/msg/errorMessage'

import { showTabs, selectTab } from '../common/tab/tabActions'

const INITIAL_VALUES = {};

const DEFAULT_PAGE_SIZE = 10;

export function getPatientById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_PATIENT}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    const patient = { ...resp.data, documentNumber: documentNumberMask(resp.data.documentNumber) };
                    dispatch([initialize('patientForm', patient), { type: 'PATIENT_FETCHED', payload: resp.data }]);
                }
                else
                    toastr.error('Erro', `Paciente de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o paciente (${id}): \n` + errorMessage(e))
            })
    }
}

export function getExamByPatientId(id) {
    return dispatch => {
        axios.get(`${BASE_URL_EXAM}?patientId=${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'PATIENT_EXAM_FETCHED', payload: resp.data.content });
                }
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar os exames do paciente (${id}): \n` + errorMessage(e))
            })
    }
}

export function getExamRequestByPatientId(id) {
    return dispatch => {
        axios.get(`${BASE_URL_EXAM_REQUEST}?patientId=${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'PATIENT_EXAM_REQUEST_FETCHED', payload: resp.data });
                }
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar os requerimentos do paciente (${id}): \n` + errorMessage(e))
            })
    }
}

export function submitPatient(values) {
    const errors = {}
    if (!values.name) {
        errors.name = 'Nome é obrigatório!'
    }

    if (!values.documentNumber) {
        errors.documentNumber = 'CPF é obrigatório!'
    }

    if (!values.sex) {
        errors.sex = 'Sexo é obrigatório!'
    }

    if (!values.birthDate) {
        errors.birthDate = 'Data de Nascimento é Obrigatório!'
    }

    if (!values.nationality) {
        errors.nationality = 'Nacionalidade é obrigatório!'
    }

    if (Object.keys(errors).length !== 0) {
        errors._error = 'Preencha todos os campos obrigatórios!';
        throw new SubmissionError(errors);
    }

    const payload = { ...values, documentNumber: onlyNumbers(values.documentNumber) };

    if (!!payload.id) {
        return update(payload)
    } else {
        return create(payload)
    }
}

export function searchPatients(values, page) {

    var errors = {}

    if (!values.name && !values.documentNumber) {
        errors.name = ' ';
        errors.documentNumber = ' ';
        errors._error = 'Preencha pelo menos um campo para consultar!';
        throw new SubmissionError(errors);
    }

    return dispatch => {
        var params = `page=${page || 0}&size=${DEFAULT_PAGE_SIZE}`
            + (!!values.name ? `&name=${values.name}` : '')
            + (!!values.documentNumber ? `&documentNumber=${onlyNumbers(values.documentNumber)}` : '');
        axios.get(`${BASE_URL_PATIENT}?${params}`)
            .then(resp => {
                if (!!resp.data) {
                    if (resp.data.content.length > 0) {
                        dispatch({ type: 'PATIENT_SEARCH_RESULT', payload: resp.data });
                    }
                    else
                        toastr.warning('Atenção', `Nenhum resultado foi encontrado!`)
                }
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao consultar: \n` + errorMessage(e))
            })
    }
}

export function clearSearchPatients() {

    return dispatch => {
        dispatch({ type: 'PATIENT_SEARCH_RESULT', payload: [] });
    }
}

export function remove(values) {
    return submit(values, 'delete', values.id, 'Exclusão')
}

function update(values) {
    return dispatch => {
        axios.put(`${BASE_URL_PATIENT}/${values.id}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Paciente atualizado com sucesso.`);
                dispatch(initialize('patientForm',
                    { ...resp.data, documentNumber: documentNumberMask(resp.data.documentNumber) }));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar o associado (${values.id}): \n` + errorMessage(e))
            })
    }
}

function create(values) {
    return dispatch => {
        axios.post(`${BASE_URL_PATIENT}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Paciente cadastrado com sucesso.`)
                dispatch(resetForm('patientForm'));
                window.location = `/#/patient/${resp.data.id}`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar o paciente: \n` + errorMessage(e))
            })
    }
}

function submit(values, method, action, idCustomer) {
    return dispatch => {
        const id = idCustomer ? idCustomer : ''

        axios[method](`${BASE_URL_PATIENT}/${id}`, values)
            .then(resp => {
                toastr.success('Sucesso', `${action} realizado(a) com sucesso.`)
                dispatch(init())
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao realizar o(a) ${action}: \n` + errorMessage(e))
            })
    }
}

export function init() {
    return [
        showTabs('tabExams', 'tabRequests'),
        selectTab('tabExams'),
        initialize('patientForm', INITIAL_VALUES)
    ]
}

export function initRegisterPatient() {
    return [
        showTabs(),
        initialize('patientForm', INITIAL_VALUES)
    ]
}