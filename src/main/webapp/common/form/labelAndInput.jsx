import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, name, label, input, placeholder, readOnly, type, meta: { error } } = props;

    return (
        <Grid cols={cols}>
            <div className={`form-group ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={input.name}>{label}</label>
                <input {...input} className='form-control'
                    id={input.name}
                    placeholder={placeholder}
                    readOnly={readOnly} type={type} />
                {error && <span className="help-block">{error}</span>}
            </div>
        </Grid>
    );
}