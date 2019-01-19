const INITIAL_STATE = { resultSearch: [], list: [], examList: [], examRequestList: [] }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'PATIENT_SEARCH_RESULT':
            return { ...state, resultSearch: action.payload }
        case 'PATIENT_EXAM_FETCHED':
            return { ...state, examList: action.payload }
        case 'PATIENT_EXAM_REQUEST_FETCHED':
            return { ...state, examRequestList: action.payload }
        default:
            return state
    }
}