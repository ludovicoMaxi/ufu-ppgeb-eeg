import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, label, input, placeholder, readOnly, addon, helpText, type, meta: { error } } = props;

    return (
        <Grid cols={cols}>
            <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={input.name} className='form-label'>{label}</label>
                <div className='input-group'>
                    <input {...input} className={`form-control ${!!error ? 'is-invalid' : ''}`}
                        id={input.name}
                        placeholder={placeholder}
                        readOnly={readOnly} type={type} />
                    {addon && <span className='input-group-text'>{addon}</span>}
                </div>
                {error && <span className="invalid-feedback d-block">{error}</span>}
                {helpText && <div className='form-text'>{helpText}</div>}
            </div>
        </Grid>
    );
}
