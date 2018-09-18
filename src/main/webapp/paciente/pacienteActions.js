import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { reset as resetForm, initialize, SubmissionError, change as changeFieldValue } from 'redux-form'

import { showTabs, selectTab } from '../common/tab/tabActions'

const BASE_URL_CUSTOMER = 'http://localhost:8080/api/paciente'
const BASE_URL_CONTACT = 'http://localhost:8080/api/contact'
const INITIAL_VALUES = { 'customer': {}, 'contactList': [{}] }

export function getPacienteById(id) {
    return dispatch => {
        axios.get(`${BASE_URL_CUSTOMER}/${id}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch(initialize('pacienteForm',  resp.data));
                }
                else
                    toastr.error('Erro', `Paciente de ID ${id} não existe!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar o paciente (${id}): \n` + e.response.data.message)
            })
    }
}

export function submitPaciente(values) {
    const errors = {}
    if (!valuesname) {
        errors.name = 'Nome é obrigatório!'
    }

    if (!values.sexo) {
        errors.sexo = 'Sexo é obrigatório!'
    }

    if (!values.dataNascimento) {
        errors.dataNascimento = 'Data de Nascimento é Obrigatório!'
    }

    if (Object.keys(errors.customer).length !== 0) {
        errors._error = 'Preencha todos os campos obrigatórios!';
        throw new SubmissionError(errors);
    }

    if (!!values.id) {
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
                toastr.success('Sucesso', `Paciente atualizado com sucesso.`)
                var customerView = INITIAL_VALUES;
                customerView.customer = resp.data;
                dispatch(initialize('pacienteForm', customerView));
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
                    dispatch(changeFieldValue('pacienteForm', 'contactList', resp.data));
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
        dispatch(initialize('pacienteForm', customerView))
    ]
}

export function init() {
    return [
        showTabs('tabPedido', 'tabExame'),
        selectTab('tabPedido'),
        initialize('pacienteForm', INITIAL_VALUES)
    ]
}