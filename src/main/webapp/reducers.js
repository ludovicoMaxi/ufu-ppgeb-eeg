import { combineReducers } from 'redux'
import { reducer as formReducer } from 'redux-form'
import { reducer as toastrReducer } from 'react-redux-toastr'

import PatientReducer from './patient/patientReducer'
import TabReducer from './common/tab/tabReducer'

const rootReducer = combineReducers({
    toastr: toastrReducer,
    form: formReducer,
    patient: PatientReducer,
    tab: TabReducer
})

export default rootReducer