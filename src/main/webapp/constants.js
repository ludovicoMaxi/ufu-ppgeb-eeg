var host = window.location.hostname;

if (host === 'localhost') {
    host = 'http://' + host + ':8080';
} else {
    host = 'https://' + host;
}

const BASE_URL = host;
const BASE_URL_PATIENT = `${BASE_URL}/api/v1/patients`
const BASE_URL_EXAM_REQUEST = `${BASE_URL}/api/v1/exam-requests`
const BASE_URL_EXAM = `${BASE_URL}/api/v1/exams`
const BASE_URL_UNIT = `${BASE_URL}/api/v1/units`
const BASE_URL_MEDICAMENT = `${BASE_URL}/api/v1/medicaments`
const BASE_URL_EQUIPMENT = `${BASE_URL}/api/v1/equipments`
const BASE_URL_NOTES = `${BASE_URL}/api/v1/notes`
const BASE_URL_QUESTIONNAIRE = `${BASE_URL}/api/v1/questionnaires`
const BASE_URL_QUESTION = `${BASE_URL}/api/v1/questions`
const BASE_URL_ANSWER_OPTION = `${BASE_URL}/api/v1/answer-options`

export {
    BASE_URL,
    BASE_URL_PATIENT,
    BASE_URL_EXAM_REQUEST,
    BASE_URL_EXAM,
    BASE_URL_UNIT,
    BASE_URL_MEDICAMENT,
    BASE_URL_EQUIPMENT,
    BASE_URL_NOTES,
    BASE_URL_QUESTIONNAIRE,
    BASE_URL_QUESTION,
    BASE_URL_ANSWER_OPTION
};
