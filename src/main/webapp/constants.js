var host = window.location.hostname;

if (host === 'localhost') {
    host = 'http://' + host + ':8080';
} else {
    host = 'https://' + host;
}

const BASE_URL = host;
const BASE_URL_PATIENT = `${BASE_URL}/api/patients`
const BASE_URL_EXAM_REQUEST = `${BASE_URL}/api/exam-requests`
const BASE_URL_EXAM = `${BASE_URL}/api/exams`
const BASE_URL_UNIT = `${BASE_URL}/api/units`
const BASE_URL_MEDICAMENT = `${BASE_URL}/api/medicaments`
const BASE_URL_EQUIPMENT = `${BASE_URL}/api/equipments`

export {
    BASE_URL,
    BASE_URL_PATIENT,
    BASE_URL_EXAM_REQUEST,
    BASE_URL_EXAM,
    BASE_URL_UNIT,
    BASE_URL_MEDICAMENT,
    BASE_URL_EQUIPMENT
};
