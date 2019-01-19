const INITIAL_STATE = { resultSearch: [], patient: {} }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'EXAM_REQUEST_SEARCH_RESULT':
            return { ...state, resultSearch: action.payload }
        case 'PATIENT_FETCHED':
            return { ...state, patient: action.payload }
        case 'EXAM_REQUEST_EXAM_FETCHED':
            return { ...state, examList: action.payload }            
        default:
            return state
    }
}