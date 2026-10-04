import { pageMetadata } from '../common/pagination/pageMetadata'

const INITIAL_STATE = { resultSearch: [], optionsAnswerOption: [], pagination: {} }

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
        case 'ANSWER_OPTION_SEARCH_RESULT':
            return {
                ...state,
                resultSearch: action.payload.content,
                pagination: pageMetadata(action.payload)
            }
        case 'OPTIONS_ANSWER_OPTION':
            return { ...state, optionsAnswerOption: toOptions(action.payload) }
        default:
            return state
    }
}
