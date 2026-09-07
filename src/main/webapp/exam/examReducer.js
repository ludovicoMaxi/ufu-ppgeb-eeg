import { pageMetadata } from '../common/pagination/pageMetadata'

const INITIAL_STATE = { resultSearch: [], patient: {}, pagination: {} }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'EXAM_SEARCH_RESULT':
            return {
                ...state,
                resultSearch: action.payload.content,
                pagination: pageMetadata(action.payload)
            }
        case 'PATIENT_FETCHED':
            return { ...state, patient: action.payload }
        default:
            return state
    }
}