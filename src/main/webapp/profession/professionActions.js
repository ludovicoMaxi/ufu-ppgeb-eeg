import axios from 'axios'
import { toastr } from 'react-redux-toastr'

import { BASE_URL_PROFESSION } from '../constants'
import { errorMessage } from '../common/msg/errorMessage'

export function getOptionsProfession() {
    return dispatch => {
        axios.get(`${BASE_URL_PROFESSION}`)
            .then(resp => {
                if (!!resp.data) {
                    dispatch({ type: 'OPTIONS_PROFESSION', payload: resp.data });
                }
                else
                    toastr.error('Erro', `Não existem profissões cadastradas!!!`)
            })
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao buscar as profissões: \n` + errorMessage(e))
            })
    }
}
