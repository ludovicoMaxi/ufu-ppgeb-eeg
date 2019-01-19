import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, arrayInsert, arrayRemove, change as changeFieldValue } from 'redux-form'
import { BASE_URL_EPOCH } from '../constants'

const INITIAL_VALUES = { 'epochs': [{}] };

export function getEpochsByExamId(examId) {
    return dispatch => {
        axios.get(`${BASE_URL_EPOCH}/?examId=${examId}`)
            .then(resp => {
                if (!!resp.data) {
                    var epochsList = { ...INITIAL_VALUES };

                    if (resp.data.length > 0) {
                        epochsList.epochs = resp.data;
                        for (var i = 0; i < epochsList.epochs.length; i++) {
                            var startTime = {};
                            startTime.minute = Math.trunc(epochsList.epochs[i].startTime / 60);
                            startTime.second = epochsList.epochs[i].startTime % 60;
                            epochsList.epochs[i].startTime = startTime;
                        }
                    }
                    dispatch(initialize('epochListForm', epochsList));
                }
                else
                    dispatch(initialize('epochListForm', INITIAL_VALUES));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar as epocas: \n` + e.response.data.message)
            })
    }
}

export function addItemList(index, item) {
    return arrayInsert('epochListForm', 'epochs', index, item);
}

export function removeItemList(index) {
    return arrayRemove('epochListForm', 'epochs', index);
}

export function submitUpdateEpochList(values) {
    return dispatch => {
        axios.put(`${BASE_URL_EPOCH}/equipment`, values)
            .then(resp => {
                toastr.success('Sucesso', `Equipamentos do exame atualizado com sucesso.`);
                dispatch([changeFieldValue('epochListForm', 'epochs', resp.data.epochs), getOptionsEquipment()]);
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar os Equipamentos do Exame (${values.id}): \n` + e.response.data.message)
            })
    }
}

export function submitEpochList(values) {
    validateEpochList(values);

    var valuesSubmit = { ...values };
    for (var i = 0; i < valuesSubmit.epochs.length; i++) {
        var startTime = valuesSubmit.epochs[i].startTime.minute * 60 + valuesSubmit.epochs[i].startTime.second;
        valuesSubmit.epochs[i].startTime = startTime;
    }

    return submitUpdateEpochList(valuesSubmit);
}

export function validateEpochList(values) {
    var errors = { 'epochs': {} }

    var hasErrorEquipmentExamList = false;
    for (var i = 0; i < values.epochs.length; i++) {
        errors.epochs[i] = validateEpoch(values.epochs[i]);
        if (!!errors.epochs[i].description || Object.keys(errors.epochs[i]).startTime !== 0) {
            hasErrorEquipmentExamList = true;
        }
    }

    if (hasErrorEquipmentExamList || !!errors._error) {
        throw new SubmissionError(errors);
    }
}

function validateEpoch(epoch) {
    var errors = {};
    errors.startTime = {};

    if (!epoch.startTime) {
        errors.startTime.minute = 'Obrigatório';
        errors.startTime.second = 'Obrigatório';
    } else {
        if (!epoch.startTime.minute) {
            errors.startTime.minute = 'Obrigatório';
        }
        if (!epoch.startTime.second) {
            errors.startTime.second = 'Obrigatório';
        }
    }

    if (!epoch.description) {
        errors.description = 'Descrição é obrigatória!'
    }

    return errors;
}

export function init(examId) {
    return getEpochsByExamId(examId);
}

export function updateEpochList(epochList) {
    return changeFieldValue('epochListForm', 'epochs', epochList);
}
