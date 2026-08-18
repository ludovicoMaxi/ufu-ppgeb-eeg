import React from 'react'
import { Link } from 'react-router-dom'
import Menu from './menu'

export default props => (
    <aside className='app-sidebar bg-body-secondary shadow' data-bs-theme='dark'>
        <div className='sidebar-brand'>
            <Link to='/' className='brand-link'>
                <span className='brand-text fw-light'>EEG</span>
            </Link>
        </div>
        <div className='sidebar-wrapper'>
            <nav className='mt-2'>
                <Menu />
            </nav>
        </div>
    </aside>
)
