import React from 'react'

export default props => (
    <li className='nav-item'>
        <a href='#' className='nav-link' onClick={event => event.preventDefault()}>
            <i className={`nav-icon fa fa-${props.icon}`}></i>
            <p>{props.label}<i className='nav-arrow fa fa-angle-right'></i></p>
        </a>
        <ul className='nav nav-treeview'>
            {props.children}
        </ul>
    </li>
)
