export const INITIAL_STATE = { optionsQuestionnaire: [], appliedQuestionnaires: [] }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'QUESTIONNAIRE_TO_ANSWER_OPTIONS':
            return { ...state, optionsQuestionnaire: action.payload }
        case 'QUESTIONNAIRE_TO_ANSWER_APPLIED':
            return { ...state, appliedQuestionnaires: action.payload }
        default:
            return state
    }
}