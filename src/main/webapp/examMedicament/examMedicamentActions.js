import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, arrayInsert, arrayRemove, change as changeFieldValue } from 'redux-form'
import { BASE_URL_UNIT, BASE_URL_MEDICAMENT, BASE_URL_EXAM } from '../constants'

const INITIAL_VALUES = { 'examMedicaments': [{}] };

export function getOptionsUnit() {
    return dispatch => {
        axios.get(`${BASE_URL_UNIT}/`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'OPTIONS_UNIT', payload: resp.data });
                }
                else
                    toastr.error('Erro', `Não existe unidades!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar as unidades: \n` + e.response.data.message)
            })
    }
}

export function getOptionsMedicament() {
    return dispatch => {
        axios.get(`${BASE_URL_MEDICAMENT}/`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'OPTIONS_MEDICAMENT', payload: resp.data });
                }
                else
                    toastr.error('Erro', `Não existe Medicamentos!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar os medicamentos: \n` + e.response.data.message)
            })
    }
}

export function addItemList(index, item) {
    return arrayInsert('examMedicamentListForm', 'examMedicaments', index, item);
}

export function removeItemList(index) {
    return arrayRemove('examMedicamentListForm', 'examMedicaments', index);
}

export function submitUpdateExamMedicamentList(values) {
    return dispatch => {
        axios.put(`${BASE_URL_EXAM}/medicament`, values)
            .then(resp => {
                toastr.success('Sucesso', `Medicamentos do exame atualizado com sucesso.`);
                dispatch([changeFieldValue('examMedicamentListForm', 'examMedicaments', resp.data.examMedicaments), getOptionsMedicament()]);
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar os Medicamentos do Exame (${values.id}): \n` + e.response.data.message)
            })
    }
}

export function submitExamMedicamentList(values) {
    validateExamMedicamentList(values);

    var valuesSubmit = { ...values };
    for (var i = 0; i < valuesSubmit.examMedicaments.length; i++) {
        if (valuesSubmit.examMedicaments[i].medicament.name == 'outro') {
            valuesSubmit.examMedicaments[i].medicament.id = undefined;
            valuesSubmit.examMedicaments[i].medicament.name = valuesSubmit.examMedicaments[i].name;
            valuesSubmit.examMedicaments[i].name = undefined;
            valuesSubmit.examMedicaments[i].medicament.description = valuesSubmit.examMedicaments[i].description;
            valuesSubmit.examMedicaments[i].description = undefined;
        }
    }

    return submitUpdateExamMedicamentList(valuesSubmit);
}

export function validateExamMedicamentList(values) {
    var errors = { 'examMedicaments': {} }

    if (!values.id) {
        errors._error = 'ID do exame não foi configurado!';
    }

    var hasErrorMedicamentExamList = false;
    for (var i = 0; i < values.examMedicaments.length; i++) {
        errors.examMedicaments[i] = validateExamMedicament(values.examMedicaments[i]);
        if (Object.keys(errors.examMedicaments[i]).length !== 0) {
            hasErrorMedicamentExamList = true;
        }
    }

    if (hasErrorMedicamentExamList || !!errors._error) {
        throw new SubmissionError(errors);
    }
}

function validateExamMedicament(examMedicament) {
    var errors = {}
    if (!examMedicament.medicament) {
        errors.medicament = 'Medicamento é obrigatório!'
    }

    if (!examMedicament.amount) {
        errors.amount = 'Quantidade é obrigatório!'
    }

    if (!examMedicament.unit) {
        errors.unit = 'Unidade é obrigatório!'
    }

    if (examMedicament.medicament.name == 'outro') {
        if (!examMedicament.name) {
            errors.name = 'Nome Medicamento é obrigatório!'
        }
        if (!examMedicament.description) {
            errors.description = 'Descrição é obrigatória!'
        }
    }

    return errors;
}

export function init(examId, examMedicamentList) {

    var initialValues = { ...INITIAL_VALUES };
    if (!!examId) {
        initialValues.id = examId;
    }
    
    return initialize('examMedicamentListForm', initialValues);
}

export function updateExamMedicamentList(examMedicamentList) {
    if (!examMedicamentList || examMedicamentList.length == 0) {
        examMedicamentList = [{}];
    }
    return changeFieldValue('examMedicamentListForm', 'examMedicaments', examMedicamentList);
}
