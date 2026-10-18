import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, label, input, placeholder, readOnly, options, meta: { error } } = props;
    const listId = `${input.name}_list`;

    return (
        <Grid cols={cols}>
            <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={input.name} className='form-label'>{label}</label>
                <input {...input} className={`form-control ${!!error ? 'is-invalid' : ''}`}
                    id={input.name}
                    placeholder={placeholder}
                    readOnly={readOnly}
                    list={listId}
                    autoComplete='off' />
                <datalist id={listId}>
                    {(options || []).map(option => (
                        <option key={option} value={option} />
                    ))}
                </datalist>
                {error && <span className="invalid-feedback d-block">{error}</span>}
            </div>
        </Grid>
    );
}
