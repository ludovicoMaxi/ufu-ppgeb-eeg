import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError } from 'redux-form'
import { BASE_URL_PATIENT } from '../constants'

import { showTabs, selectTab } from '../common/tab/tabActions'

const INITIAL_VALUES = {};

export function getPatientById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_PATIENT}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch([initialize('patientForm', resp.data), { type: 'PATIENT_FETCHED', payload: resp.data }]);
                }
                else
                    toastr.error('Erro', `Paciente de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o paciente (${id}): \n` + e.response.data.message)
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

    if (!values.nacionality) {
        errors.nacionality = 'Nacionalidade é obrigatório!'
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

export function searchPatients(values) {

    var errors = {}

    if (!values.name && !values.documentNumber) {
        errors.name = ' ';
        errors.documentNumber = ' ';
        errors._error = 'Preencha pelo menos um campo para consultar!';
        throw new SubmissionError(errors);
    }

    return dispatch => {
        var params = (!!values.name ? `name=${values.name}` : '')
            + (!!values.documentNumber ? `documentNumber=${values.documentNumber}` : '');
        axios.get(`${BASE_URL_PATIENT}?${params}`)
            .then(resp => {
                if (!!resp.data && resp.data.length > 0) {
                    dispatch({ type: 'PATIENT_SEARCH_RESULT', payload: resp.data });
                }
                else
                    toastr.warning('Atenção', `Nenhum resultado foi encontrado!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao consultar: \n` + e.response.data.message)
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
        axios.put(`${BASE_URL_PATIENT}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Paciente atualizado com sucesso.`);
                dispatch(initialize('patientForm', resp.data));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar o associado (${values.id}): \n` + e.response.data.message)
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
                toastr.error('Erro', `Ocorreu um erro ao cadastrar o paciente: \n` + e.response.data.message)
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
                toastr.error('Erro', 'Ocorreu um erro ao realizar o(a) ${action}: \n' + e.response.data.message)
            })
    }
}

export function init() {
    return [
        initialize('patientForm', INITIAL_VALUES)
    ]
}

export function initRegisterPatient() {
    return [
        showTabs(),
        initialize('patientForm', INITIAL_VALUES)
    ]
}