import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, name, label, input, placeholder, readOnly, type, meta: { error } } = props;

    return (
        <Grid cols={cols}>
            <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={input.name} className='form-label'>{label}</label>
                <input {...input} className={`form-control ${!!error ? 'is-invalid' : ''}`}
                    id={input.name}
                    placeholder={placeholder}
                    readOnly={readOnly} type={type} />
                {error && <span className="invalid-feedback d-block">{error}</span>}
            </div>
        </Grid>
    );
}
