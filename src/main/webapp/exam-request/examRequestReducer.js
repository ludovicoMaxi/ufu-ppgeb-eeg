const INITIAL_STATE = { resultSearch: [], patient: {} }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'PATIENT_FETCHED':
            return { ...state, patient: action.payload }
        default:
            return state
    }
}