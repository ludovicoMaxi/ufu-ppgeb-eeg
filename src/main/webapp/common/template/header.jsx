import React from 'react'
import { Link } from 'react-router-dom'

export default props => (
    <header className='app-header navbar navbar-expand-lg bg-body'>
        <div className='container-fluid'>
            <ul className='navbar-nav'>
                <li className='nav-item'>
                    <a className='nav-link' data-lte-toggle='sidebar' href='#' role='button'>
                        <i className='fa fa-bars'></i>
                    </a>
                </li>
            </ul>
            <Link to='/' className='navbar-brand'>
                <i className='fa fa-medkit'></i>
                <span className='ms-2'>
                    <b> UFU</b> PPGEB EEG
                </span>
            </Link>
        </div>
    </header>
)
