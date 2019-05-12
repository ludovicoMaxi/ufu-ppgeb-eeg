import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import {
    initialize,
    SubmissionError,
    arrayInsert,
    arrayRemove,
    change as changeFieldValue,
} from 'redux-form'
import { BASE_URL_EPOCH } from '../constants'

const INITIAL_VALUES = { 'epochs': [{}] };

export function getEpochsByExamId(examId) {
    return dispatch => {
        axios.get(`${BASE_URL_EPOCH}/?examId=${examId}`)
            .then(resp => {
                var epochList = { ...INITIAL_VALUES };
                epochList.examId = examId;

                if (!!resp.data) {
                    if (resp.data.length > 0) {
                        epochList.epochs = convertSecondsInMinutesAndSeconds(resp.data);
                    }
                }
                dispatch(initialize('epochListForm', epochList));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar as epocas: \n` + e.response.data.message)
            })
    }
}

function convertSecondsInMinutesAndSeconds(epochs) {
    if (epochs != null && epochs != undefined) {
        for (var i = 0; i < epochs.length; i++) {
            var finalTime = {};
            var finalTimeInSeconds = epochs[i].startTime + epochs[i].duration;
            finalTime.minute = Math.trunc(finalTimeInSeconds / 60);
            finalTime.second = finalTimeInSeconds % 60;
            epochs[i].finalTime = finalTime;

            var startTime = {};
            startTime.minute = Math.trunc(epochs[i].startTime / 60);
            startTime.second = epochs[i].startTime % 60;
            epochs[i].startTime = startTime;
        }
    }

    return epochs;
}

export function addItemList(index, item) {
    return arrayInsert('epochListForm', 'epochs', index, item);
}

export function removeItemList(index) {
    return arrayRemove('epochListForm', 'epochs', index);
}

export function submitUpdateEpochList(values) {
    return dispatch => {
        axios.put(`${BASE_URL_EPOCH}/`, values)
            .then(resp => {
                toastr.success('Sucesso', `Época(s) do exame atualizada(s) com sucesso.`);
                var epochs = convertSecondsInMinutesAndSeconds(resp.data.epochs);
                dispatch(changeFieldValue('epochListForm', 'epochs', epochs));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar a(s) Época(s) do Exame (${values.id}): \n` + e.response.data.message)
            })
    }
}

export function submitEpochList(values) {
    validateEpochList(values);
    var valuesSubmit = { ...values };

    for (var i = 0; i < valuesSubmit.epochs.length; i++) {
        var startTime = Number(valuesSubmit.epochs[i].startTime.minute) * 60 + Number(valuesSubmit.epochs[i].startTime.second);
        valuesSubmit.epochs[i].startTime = startTime;
    }
    return submitUpdateEpochList(valuesSubmit);
}

export function validateEpochList(values) {
    var errors = { 'epochs': [] }

    var hasErrorEpochExamList = false;
    for (var i = 0; i < values.epochs.length; i++) {
        errors.epochs[i] = validateEpoch(values.epochs[i]);
        if (!!errors.epochs[i].description || !!errors.epochs[i].duration ||
            Object.keys(errors.epochs[i].startTime).length !== 0) {
            hasErrorEpochExamList = true;
        }
    }

    if (hasErrorEpochExamList || !!errors._error) {
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
        if (!epoch.startTime.minute && epoch.startTime.minute != 0) {
            errors.startTime.minute = 'Obrigatório';
        }
        if (!epoch.startTime.second && epoch.startTime.second != 0) {
            errors.startTime.second = 'Obrigatório';
        }
    }

    if (!epoch.duration) {
        errors.duration = 'Obrigatório';
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
