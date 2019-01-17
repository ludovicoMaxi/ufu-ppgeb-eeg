const INITIAL_STATE = { optionsUnit: [] }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'OPTIONS_UNIT':
            var options = [];
            if (!!action.payload) {
                for (var i = 0; i < action.payload.length; i++) {
                    var item = action.payload[i];
                    options[i] = { 'value': item, 'label': item.name };
                }
            }
            return { ...state, optionsUnit: options }

        case 'OPTIONS_EQUIPMENT':
            var options = [];
            if (!!action.payload) {
                for (var i = 0; i < action.payload.length; i++) {
                    var item = action.payload[i];
                    options[i] = { 'value': item, 'label': item.name };
                }
            }
            options.push({ 'value': { 'name': 'outro' }, 'label': 'OUTRO' });
            return { ...state, optionsEquipment: options }

        default:
            return state
    }
}