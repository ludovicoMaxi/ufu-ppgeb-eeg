var host = window.location.hostname;

if (host === 'localhost') {
    host = 'http://' + host + ':8080';
} else {
    host = 'https://' + host;
}

const BASE_URL = host;
const BASE_URL_PATIENT = `${BASE_URL}/api/patient`
const BASE_URL_EXAM_REQUEST = `${BASE_URL}/api/exam-request`

export {
    BASE_URL,
    BASE_URL_PATIENT,
    BASE_URL_EXAM_REQUEST
};