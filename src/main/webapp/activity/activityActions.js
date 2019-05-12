import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import {
    initialize,
    SubmissionError,
    arrayInsert,
    arrayRemove,
    change as changeFieldValue
} from 'redux-form'
import { BASE_URL_ACTIVITY } from '../constants'

const INITIAL_VALUES = { 'activities': [{}] };

export function getActivitysByExamId(examId) {
    return dispatch => {
        axios.get(`${BASE_URL_ACTIVITY}/?examId=${examId}`)
            .then(resp => {
                var activityList = { ...INITIAL_VALUES };
                activityList.examId = examId;

                if (!!resp.data) {
                    if (resp.data.length > 0) {
                        activityList.activities = convertSecondsInMinutesAndSeconds(resp.data);
                    }
                }
                dispatch(initialize('activityListForm', activityList));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar as epocas: \n` + e.response.data.message)
            })
    }
}

function convertSecondsInMinutesAndSeconds(activities) {
    if (activities != null && activities != undefined) {
        for (var i = 0; i < activities.length; i++) {
            var finalTime = {};
            var finalTimeInSeconds = activities[i].startTime + activities[i].duration;
            finalTime.minute = Math.trunc(finalTimeInSeconds / 60);
            finalTime.second = finalTimeInSeconds % 60;
            activities[i].finalTime = finalTime;

            var startTime = {};
            startTime.minute = Math.trunc(activities[i].startTime / 60);
            startTime.second = activities[i].startTime % 60;
            activities[i].startTime = startTime;
        }
    }

    return activities;
}

export function addItemList(index, item) {
    return arrayInsert('activityListForm', 'activities', index, item);
}

export function removeItemList(index) {
    return arrayRemove('activityListForm', 'activities', index);
}

export function submitUpdateActivityList(values) {
    return dispatch => {
        axios.put(`${BASE_URL_ACTIVITY}/`, values)
            .then(resp => {
                toastr.success('Sucesso', `Atividade(s) do exame atualizada(s) com sucesso.`);
                var activities = convertSecondsInMinutesAndSeconds(resp.data.activities);
                dispatch(changeFieldValue('activityListForm', 'activities', activities));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar a(s) Atividade(s) do Exame (${values.id}): \n` + e.response.data.message)
            })
    }
}

export function submitActivityList(values) {
    validateActivityList(values);
    var valuesSubmit = { ...values };

    for (var i = 0; i < valuesSubmit.activities.length; i++) {
        var startTime = Number(valuesSubmit.activities[i].startTime.minute) * 60 + Number(valuesSubmit.activities[i].startTime.second);
        valuesSubmit.activities[i].startTime = startTime;
    }
    return submitUpdateActivityList(valuesSubmit);
}

export function validateActivityList(values) {
    var errors = { 'activities': [] }

    var hasErrorActivityExamList = false;
    for (var i = 0; i < values.activities.length; i++) {
        errors.activities[i] = validateActivity(values.activities[i]);
        if (!!errors.activities[i].description || !!errors.activities[i].duration ||
            Object.keys(errors.activities[i].startTime).length !== 0) {
            hasErrorActivityExamList = true;
        }
    }

    if (hasErrorActivityExamList || !!errors._error) {
        throw new SubmissionError(errors);
    }
}

function validateActivity(activity) {
    var errors = {};
    errors.startTime = {};
    if (!activity.startTime) {
        errors.startTime.minute = 'Obrigatório';
        errors.startTime.second = 'Obrigatório';
    } else {
        if (!activity.startTime.minute && activity.startTime.minute != 0) {
            errors.startTime.minute = 'Obrigatório';
        }
        if (!activity.startTime.second && activity.startTime.second != 0) {
            errors.startTime.second = 'Obrigatório';
        }
    }

    if (!activity.duration) {
        errors.duration = 'Obrigatório';
    }

    if (!activity.description) {
        errors.description = 'Descrição é obrigatória!'
    }

    return errors;
}

export function init(examId) {
    return getActivitysByExamId(examId);
}

export function updateActivityList(activityList) {
    return changeFieldValue('activityListForm', 'activities', activityList);
}
