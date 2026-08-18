import React from 'react'
import { useParams, useLocation, useNavigate } from 'react-router-dom'

export default function withRouter(Component) {
    return function WithRouter(props) {
        const params = useParams()
        const location = useLocation()
        const navigate = useNavigate()
        return <Component {...props} match={{ params }} location={location} navigate={navigate} />
    }
}
