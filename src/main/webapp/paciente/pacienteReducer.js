const INITIAL_STATE = {list: []}

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'CUSTOMER_FETCHED':
            return { ...state, customer: action.payload.data }
        default:
            return state
    }
}