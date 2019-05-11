var host = window.location.hostname;

if (host === 'localhost') {
    host = 'http://' + host + ':8080';
} else {
    host = 'https://' + host;
}

const BASE_URL = host;
const BASE_URL_PATIENT = `${BASE_URL}/api/patient`
const BASE_URL_EXAM_REQUEST = `${BASE_URL}/api/exam-request`
const BASE_URL_EXAM = `${BASE_URL}/api/exam`
const BASE_URL_UNIT = `${BASE_URL}/api/unit`
const BASE_URL_MEDICAMENT = `${BASE_URL}/api/medicament`
const BASE_URL_EQUIPMENT = `${BASE_URL}/api/equipment`
const BASE_URL_EPOCH = `${BASE_URL}/api/epoch`
const BASE_URL_ACTIVITY = `${BASE_URL}/api/activity`

export {
    BASE_URL,
    BASE_URL_PATIENT,
    BASE_URL_EXAM_REQUEST,
    BASE_URL_EXAM,
    BASE_URL_UNIT,
    BASE_URL_MEDICAMENT,
    BASE_URL_EQUIPMENT,
    BASE_URL_EPOCH,
    BASE_URL_ACTIVITY
};