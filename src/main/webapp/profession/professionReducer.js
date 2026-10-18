const INITIAL_STATE = { optionsProfession: [] }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'OPTIONS_PROFESSION':
            var options = (action.payload || []).map(item => item.name);
            return { ...state, optionsProfession: options }

        default:
            return state
    }
}
