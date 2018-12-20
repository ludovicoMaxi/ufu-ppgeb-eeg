var host = window.location.hostname;

if (host === 'localhost') {
    host = 'http://' + host + ':8080';
} else {
    host = 'https://' + host;
}

const BASE_URL = host;
const BASE_URL_CUSTOMER = `${BASE_URL}/api/customer`
const BASE_URL_CONTACT = `${BASE_URL}/api/contact`
const BASE_URL_ADDRESS = `${BASE_URL}/api/address`
const BASE_URL_NOTES = `${BASE_URL}/api/notes`
const BASE_URL_PAYMENT = `${BASE_URL}/api/payment`

export {
    BASE_URL,
    BASE_URL_CUSTOMER,
    BASE_URL_CONTACT,
    BASE_URL_ADDRESS,
    BASE_URL_NOTES,
    BASE_URL_PAYMENT
};