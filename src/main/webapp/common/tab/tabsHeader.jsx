import React from 'react'

export default props => (
    <div className='card-header p-0 pt-1 border-bottom-0'>
        <ul className='nav nav-tabs card-header-tabs'>
            {props.children}
        </ul>
    </div>
)
