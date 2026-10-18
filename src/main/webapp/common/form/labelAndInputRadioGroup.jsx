import React from 'react'
import Grid from '../layout/grid'

const MULTI_COLUMN_MIN_OPTIONS = 10;

export default props => {
    const { cols, label, input, options, readOnly, meta: { error } } = props;
    const multiColumn = (options || []).length > MULTI_COLUMN_MIN_OPTIONS;

    const renderOption = option => (
        <div className='form-check' key={option.value}>
            <input {...input}
                type='radio'
                className='form-check-input'
                id={`${input.name}_${option.value}`}
                name={input.name}
                value={option.value}
                checked={`${input.value}` === `${option.value}`}
                disabled={readOnly}
                onChange={() => input.onChange([option.value])} />
            <label className='form-check-label'
                htmlFor={`${input.name}_${option.value}`}>{option.label}</label>
        </div>
    );

    return (
        <Grid cols={cols}>
            <label className='form-label'>{label}</label>
            {multiColumn ? (
                <div className='row'>
                    {(options || []).map(option => (
                        <div className='col-12 col-sm-6 col-lg-4' key={option.value}>
                            {renderOption(option)}
                        </div>
                    ))}
                </div>
            ) : (options || []).map(renderOption)}
            {error && <span className="invalid-feedback d-block">{error}</span>}
        </Grid>
    );
}