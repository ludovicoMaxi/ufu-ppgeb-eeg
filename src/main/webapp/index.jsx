import React from 'react'
import { createRoot } from 'react-dom/client'
import { applyMiddleware, createStore } from 'redux'
import { Provider } from 'react-redux'

import promise from 'redux-promise'
import multi from 'redux-multi'
import { thunk } from 'redux-thunk'

import App from './app'
import reducers from './reducers'

const devTools = window.__REDUX_DEVTOOLS_EXTENSION__
    && window.__REDUX_DEVTOOLS_EXTENSION__()
const store = applyMiddleware(multi, thunk, promise)(createStore)(reducers, devTools)

const container = document.getElementById('app')
const root = createRoot(container)
root.render(
    <Provider store={store}>
        <App />
    </Provider>
)
