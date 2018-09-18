import { combineReducers } from 'redux'
import { reducer as formReducer } from 'redux-form'
import { reducer as toastrReducer } from 'react-redux-toastr'

import CustomerReducer from './customer/customerReducer'
import TabReducer from './common/tab/tabReducer'

const rootReducer = combineReducers({
    toastr: toastrReducer,
    form: formReducer,
    customer: CustomerReducer,
    tab: TabReducer
})

export default rootReducer