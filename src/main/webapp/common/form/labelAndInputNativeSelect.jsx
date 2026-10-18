import React from 'react'
import Grid from '../layout/grid'

// Native <select> for short, fixed enums. Option values may be booleans or strings;
// the selected option's original value is published back to redux-form.
export default props => {
    const { cols, label, input, options, placeholder, readOnly, meta: { error } } = props;
    const hasValue = input.value !== undefined && input.value !== null && input.value !== '';
    const selectedValue = hasValue ? `${input.value}` : '';

    const onChange = event => {
        const selected = (options || []).find(option => `${option.value}` === event.target.value);
        input.onChange(selected ? selected.value : null);
    }

    return (
        <Grid cols={cols}>
            <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={input.name} className='form-label'>{label}</label>
                <select id={input.name}
                    name={input.name}
                    className={`form-select ${!!error ? 'is-invalid' : ''}`}
                    value={selectedValue}
                    disabled={!!readOnly}
                    onChange={onChange}
                    onBlur={() => input.onBlur(input.value)}>
                    <option value=''>{placeholder || ''}</option>
                    {(options || []).map(option => (
                        <option key={`${option.value}`} value={`${option.value}`}>{option.label}</option>
                    ))}
                </select>
                {error && <span className="invalid-feedback d-block">{error}</span>}
            </div>
        </Grid>
    );
}
