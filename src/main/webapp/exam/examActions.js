import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, change as changeFieldValue } from 'redux-form'

import { showTabs, selectTab } from '../common/tab/tabActions'
import { BASE_URL_EXAM } from '../constants'
import { updateExamMedicamentList } from '../examMedicament/examMedicamentActions'
import { updateExamEquipmentList } from '../examEquipment/examEquipmentActions'

const INITIAL_VALUES = {};

export function getExamById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_EXAM}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch([
                        initialize('examForm', resp.data),
                        initialize('patientForm', resp.data.patient),
                        updateExamMedicamentList(resp.data.examMedicaments),
                        updateExamEquipmentList(resp.data.examEquipments)]);
                }
                else
                    toastr.error('Erro', `Exame de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o Exame (${id}): \n` + e.response.data.message)
            })
    }
}

export function submitExam(values) {

    const errors = {}
    if (!values.achievementDate) {
        errors.achievementDate = 'A data é obrigatório!'
    }

    // if (!values.clinicalData) {
    //     errors.clinicalData = 'Os dados Clinicos são obrigatórios!'
    // }

    // if (!values.medicalReport) {
    //     errors.medicalReport = 'O Laudo é obrigatório!'
    // }

    // if (!values.conclusion) {
    //     errors.conclusion = 'A conclusão é Obrigatório!'
    // }

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

export function searchExam(values) {

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
        axios.get(`${BASE_URL_EXAM}?${params}`)
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

export function clearSearchExam() {

    return dispatch => {
        dispatch({ type: 'EXAM_REQUEST_SEARCH_RESULT', payload: [] });
    }
}

export function remove(values) {
    return submit(values, 'delete', values.id, 'Exclusão')
}

function update(values) {
    return dispatch => {
        axios.put(`${BASE_URL_EXAM}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Exame atualizado com sucesso.`);
                dispatch(initialize('examForm', resp.data));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar o Exame (${values.id}): \n` + e.response.data.message)
            })
    }
}

function create(values) {
    return dispatch => {
        axios.post(`${BASE_URL_EXAM}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Exame cadastrado com sucesso.`)
                dispatch(resetForm('examForm'));
                window.location = `/#/exam/${resp.data.id}`;
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar o Exame: \n` + e.response.data.message)
            })
    }
}

export function init() {
    return [
        showTabs('tabMedicine', 'tabEquipments', 'tabEpochs'),
        selectTab('tabMedicine'),
        initialize('examForm', INITIAL_VALUES)
    ]
}

export function setPatientInForm(patient) {
    return changeFieldValue('examForm', 'patient', patient);
}