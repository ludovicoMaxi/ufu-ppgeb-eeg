const INITIAL_STATE = { optionsAnswerOption: [] }

function toOptions(payload) {
    var options = [];
    if (!!payload) {
        for (var i = 0; i < payload.length; i++) {
            var item = payload[i];
            options[i] = { 'value': item, 'label': optionLabel(item) };
        }
    }
    return options;
}

function optionLabel(item) {
    return item.active === false ? `${item.text} (inativa)` : item.text;
}

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'OPTIONS_ANSWER_OPTION':
            return { ...state, optionsAnswerOption: toOptions(action.payload) }
        default:
            return state
    }
}
