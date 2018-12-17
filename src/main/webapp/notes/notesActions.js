import { initialize, arrayInsert, change as changeFieldValue } from 'redux-form'
import axios from 'axios'
import { toastr } from 'react-redux-toastr'

import { BASE_URL_NOTES } from '../constants'

export function init(notesList) {
    return dispatch => {
        var notesForm = { 'notesList': notesList || [] }
        dispatch(initialize('notesForm', notesForm));
    }
}

export function find(objectId, objectType) {
    return dispatch => {
        axios.get(`${BASE_URL_NOTES}?objectType=${objectType}&objectId=${objectId}`)
            .then(resp => {
                if (!!resp.data && resp.data.length > 0) {
                    dispatch(initialize('notesForm', { 'notesList': resp.data }));
                }
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um ERRO ao buscar as notas para o id=${objectId}`)
            })
    }
}

export function create(note) {
    return dispatch => {
        axios.post(`${BASE_URL_NOTES}`, note)
            .then(resp => {
                toastr.success('Sucesso', `Nota cadastrada com sucesso.`)
                dispatch([
                    changeFieldValue('notesForm', 'note', ''),
                    arrayInsert('notesForm', 'notesList', 0, resp.data)
                ]);
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao cadastrar a nota: \n` + e.response.data.message)
            })
    }
}

