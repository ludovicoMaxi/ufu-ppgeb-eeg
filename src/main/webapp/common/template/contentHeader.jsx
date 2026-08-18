import React from 'react'

export default props => (
    <section className='app-content-header'>
        <div className='container-fluid'>
            <div className='row'>
                <div className='col-sm-6'>
                    <h3 className='mb-0'>{props.title} <small className='text-muted'>{props.small}</small></h3>
                </div>
            </div>
        </div>
    </section>
)
