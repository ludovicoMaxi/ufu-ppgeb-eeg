import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, label, input, options, readOnly, meta: { error } } = props;

    return (
        <Grid cols={cols}>
            <label className='form-label'>{label}</label>
            {(options || []).map(option => (
                <div className='form-check' key={option.value}>
                    <input {...input}
                        type='radio'
                        className='form-check-input'
                        id={`${input.name}_${option.value}`}
                        name={input.name}
                        value={option.value}
                        checked={`${input.value}` === `${option.value}`}
                        disabled={readOnly}
                        onChange={() => input.onChange(option.value)} />
                    <label className='form-check-label'
                        htmlFor={`${input.name}_${option.value}`}>{option.label}</label>
                </div>
            ))}
            {error && <span className="invalid-feedback d-block">{error}</span>}
        </Grid>
    );
}