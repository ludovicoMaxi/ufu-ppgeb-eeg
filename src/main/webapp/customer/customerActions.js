import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, change as changeFieldValue } from 'redux-form'

import { showTabs, selectTab } from '../common/tab/tabActions'

import { BASE_URL_CUSTOMER, BASE_URL_CONTACT, BASE_URL_ADDRESS } from '../constants'

const INITIAL_VALUES = { 'customer': {}, 'contactList': [{}], 'address': {} }

export function getList() {
    const request = axios.get(`${BASE_URL_CUSTOMER}`)
    return {
        type: 'CUSTOMERS_FETCHED',
        payload: request
    }
}

export function getCustomerById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_CUSTOMER}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch(changeFieldValue('customerForm', 'customer', resp.data));
                }
                else
                    toastr.error('Erro', `Associado de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o associado (${id}): \n` + e.response.data.message)
            })
    }
}

function validateContact(contact) {

    var errors = {}
    if (!contact.name) {
        errors.name = 'Nome é obrigatório!'
    }

    if (!contact.cellphone) {
        errors.cellphone = 'Celular é obrigatório!'
    }

    return errors;
}

export function submitCustomerView(values) {
    const errors = { 'customer': {}, 'contactList': [{}] }
    if (!values.customer.name) {
        errors.customer.name = 'Nome é obrigatório!'
    }

    if (!values.customer.documentNumber) {
        errors.customer.documentNumber = 'CPF / CNPJ é obrigatório!'
    }
    // else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.documentNumber)) {
    //     errors.documentNumber = 'CPF ou CNPJ inválido'
    // }

    if (!values.customer.birthDate) {
        errors.customer.birthDate = 'Data de Nascimento é Obrigatório!'
    }

    if (!values.customer.nacionality) {
        errors.customer.nacionality = 'A Nacionalidade é Obrigatória!'
    }

    var hasErrorContactList = false;
    var hasContactMain = false;
    var msgContactMainError = '';
    for (var i = 0; i < values.contactList.length; i++) {
        errors.contactList[i] = validateContact(values.contactList[i]);
        if (Object.keys(errors.contactList[i]).length !== 0) {
            hasErrorContactList = true;
        }
        if (!!values.contactList[i].main) {
            if (hasContactMain) {
                msgContactMainError = '\nMarque apenas um contato como principal!';
                hasErrorContactList = true;
            }
            hasContactMain = true;
        }
    }

    if (hasContactMain === false) {
        msgContactMainError = '\nMarque um contato como principal!';
        hasErrorContactList = true;
    }

    if (Object.keys(errors.customer).length !== 0 || hasErrorContactList) {
        errors._error = 'Preencha todos os campos obrigatórios!' + msgContactMainError
        throw new SubmissionError(errors);
    }

    if (!!values.customer.id) {
        return update(values)
    } else {
        return create(values)
    }
}

export function remove(values) {
    return submit(values, 'delete', values.id, 'Exclusão')
}

function update(values) {
    return dispatch => {
        axios.put(`${BASE_URL_CUSTOMER}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Associado atualizado com sucesso.`)
                var customerView = INITIAL_VALUES;
                customerView.customer = resp.data;
                dispatch(initialize('customerForm', customerView));
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao atualizar o associado (${values.id}): \n` + e.response.data.message)
            })
    }
}

export function findContactByCustomer(customerID) {
    return dispatch => {
        axios.get(`${BASE_URL_CONTACT}?objectType=1&objectId=${customerID}`)
            .then(resp => {
                if (!!resp.data && resp.data.length > 0) {
                    dispatch(changeFieldValue('customerForm', 'contactList', resp.data));
                }
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu ao buscar os contatos para o customer de id=${customerID}`)
            })
    }
}

function create(values) {
    return dispatch => {
        axios.post(`${BASE_URL_CUSTOMER}`, values)
            .then(resp => {
                toastr.success('Sucesso', `Associado cadastrado com sucesso.`)
                dispatch(initialize({ 'customer': resp.data }))
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar o associado: \n` + e.response.data.message)
            })
    }
}

function submit(values, method, action, idCustomer) {
    return dispatch => {
        const id = idCustomer ? idCustomer : ''

        axios[method](`${BASE_URL_CUSTOMER}/${id}`, values)
            .then(resp => {
                toastr.success('Sucesso', `${action} realizado(a) com sucesso.`)
                dispatch(init())
            })
            .catch(e => {
                toastr.error('Erro', 'Ocorreu um erro ao realizar o(a) ${action}: \n' + e.response.data.message)
            })
    }
}

export function showDelete(customer) {
    var customerView = INITIAL_VALUES;
    customerView.customer = resp.data;

    return [
        dispatch(initialize('customerForm', customerView))
    ]
}

export function init() {
    return [
        showTabs('tabContact', 'tabAddress'),
        selectTab('tabContact'),
        initialize('customerForm', INITIAL_VALUES)
    ]
}