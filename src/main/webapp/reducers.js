import { combineReducers } from 'redux'
import { reducer as formReducer } from 'redux-form'
import { reducer as toastrReducer } from 'react-redux-toastr'

import PatientReducer from './patient/patientReducer'
import TabReducer from './common/tab/tabReducer'
import ExamRequestReducer from './exam-request/examRequestReducer'
import ExamReducer from './exam/examReducer'
import ExamMedicamentReducer from './examMedicament/examMedicamentReducer'
import ExamEquipmentReducer from './examEquipment/examEquipmentReducer'

const rootReducer = combineReducers({
    toastr: toastrReducer,
    form: formReducer,
    patient: PatientReducer,
    tab: TabReducer,
    examRequest: ExamRequestReducer,
    exam: ExamReducer,
    examMedicament: ExamMedicamentReducer,
    examEquipment: ExamEquipmentReducer
})

export default rootReducer