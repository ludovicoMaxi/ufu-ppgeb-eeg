import React from 'react'

export default props => (
    <header className='main-header'>
        <a href='/#/' className='logo'>
            <span className='logo-mini'><b>EEG</b></span>
            <span className='logo-lg'>
                <i className='fa fa-medkit'></i>
                <b> UFU</b> PPGEB EEG
            </span>
        </a>
        <nav className='navbar navbar-static-top'>
            <a href="#" className='sidebar-toggle' data-toggle='push-menu' role="button">
                <span className="sr-only">Toggle navigation</span>
            </a>
        </nav>
    </header>
)