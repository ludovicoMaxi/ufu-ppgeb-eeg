const INITIAL_STATE = { resultSearch: [], list: [] }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'PATIENT_SEARCH_RESULT':
            return { ...state, resultSearch: action.payload }
        default:
            return state
    }
}