import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, label, input, options, readOnly, meta: { error } } = props;
    const selected = (input.value || []).map(optionId => `${optionId}`);

    const toggle = option => {
        const optionId = `${option.value}`;
        const next = selected.includes(optionId)
            ? selected.filter(value => value !== optionId)
            : selected.concat([optionId]);
        input.onChange(next);
    }

    return (
        <Grid cols={cols}>
            <label className='form-label'>{label}</label>
            {(options || []).map(option => (
                <div className='form-check' key={option.value}>
                    <input {...input}
                        type='checkbox'
                        className='form-check-input'
                        id={`${input.name}_${option.value}`}
                        value={option.value}
                        checked={selected.includes(`${option.value}`)}
                        disabled={readOnly}
                        onChange={() => toggle(option)} />
                    <label className='form-check-label'
                        htmlFor={`${input.name}_${option.value}`}>{option.label}</label>
                </div>
            ))}
            {error && <span className="invalid-feedback d-block">{error}</span>}
        </Grid>
    );
}