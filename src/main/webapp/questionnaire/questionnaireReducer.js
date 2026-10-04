import { pageMetadata } from '../common/pagination/pageMetadata'

const INITIAL_STATE = { resultSearch: [], pagination: {} }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'QUESTIONNAIRE_SEARCH_RESULT':
            return {
                ...state,
                resultSearch: action.payload.content,
                pagination: pageMetadata(action.payload)
            }
        default:
            return state
    }
}
