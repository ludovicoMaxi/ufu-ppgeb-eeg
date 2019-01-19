import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, arrayInsert, arrayRemove, change as changeFieldValue } from 'redux-form'
import { BASE_URL_UNIT, BASE_URL_EQUIPMENT, BASE_URL_EXAM } from '../constants'

const INITIAL_VALUES = { 'examEquipments': [{}] };

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

export function getOptionsEquipment() {
    return dispatch => {
        axios.get(`${BASE_URL_EQUIPMENT}/`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'OPTIONS_EQUIPMENT', payload: resp.data });
                }
                else
                    toastr.error('Erro', `Não existe Equipamentos!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar os equipamentos: \n` + e.response.data.message)
            })
    }
}

export function addItemList(index, item) {
    return arrayInsert('examEquipmentListForm', 'examEquipments', index, item);
}

export function removeItemList(index) {
    return arrayRemove('examEquipmentListForm', 'examEquipments', index);
}

export function submitUpdateExamEquipmentList(values) {
    return dispatch => {
        axios.put(`${BASE_URL_EXAM}/equipment`, values)
            .then(resp => {
                toastr.success('Sucesso', `Equipamentos do exame atualizado com sucesso.`);
                dispatch([changeFieldValue('examEquipmentListForm', 'examEquipments', resp.data.examEquipments), getOptionsEquipment()]);
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar os Equipamentos do Exame (${values.id}): \n` + e.response.data.message)
            })
    }
}

export function submitExamEquipmentList(values) {
    validateExamEquipmentList(values);

    var valuesSubmit = { ...values };
    for (var i = 0; i < valuesSubmit.examEquipments.length; i++) {
        if (valuesSubmit.examEquipments[i].equipment.name == 'outro') {
            valuesSubmit.examEquipments[i].equipment.id = undefined;
            valuesSubmit.examEquipments[i].equipment.name = valuesSubmit.examEquipments[i].name;
            valuesSubmit.examEquipments[i].name = undefined;
            valuesSubmit.examEquipments[i].equipment.description = valuesSubmit.examEquipments[i].description;
            valuesSubmit.examEquipments[i].description = undefined;
        }
    }

    return submitUpdateExamEquipmentList(valuesSubmit);
}

export function validateExamEquipmentList(values) {
    var errors = { 'examEquipments': {} }

    if (!values.id) {
        errors._error = 'ID do exame não foi configurado!';
    }

    var hasErrorEquipmentExamList = false;
    for (var i = 0; i < values.examEquipments.length; i++) {
        errors.examEquipments[i] = validateExamEquipment(values.examEquipments[i]);
        if (Object.keys(errors.examEquipments[i]).length !== 0) {
            hasErrorEquipmentExamList = true;
        }
    }

    if (hasErrorEquipmentExamList || !!errors._error) {
        throw new SubmissionError(errors);
    }
}

function validateExamEquipment(examEquipment) {
    var errors = {}
    if (!examEquipment.equipment) {
        errors.equipment = 'EquipAmento é obrigatório!'
    }

    if (!examEquipment.amount) {
        errors.amount = 'Quantidade é obrigatório!'
    }

    if (!examEquipment.unit) {
        errors.unit = 'Unidade é obrigatório!'
    }

    if (examEquipment.equipment.name == 'outro') {
        if (!examEquipment.name) {
            errors.name = 'Nome Equipamento é obrigatório!'
        }
        if (!examEquipment.description) {
            errors.description = 'Descrição é obrigatória!'
        }
    }

    return errors;
}

export function init(examId, examEquipmentList) {

    var initialValues = { ...INITIAL_VALUES };
    if (!!examId) {
        initialValues.id = examId;
    }

    return initialize('examEquipmentListForm', initialValues);
}

export function updateExamEquipmentList(examEquipmentList) {
    if (!examEquipmentList || examEquipmentList.length == 0) {
        examEquipmentList = [{}];
    }
    return changeFieldValue('examEquipmentListForm', 'examEquipments', examEquipmentList);
}
