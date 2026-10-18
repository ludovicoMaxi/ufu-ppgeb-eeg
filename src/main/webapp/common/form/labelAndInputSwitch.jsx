import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, label, input, readOnly, helpText } = props;

    return (
        <Grid cols={cols}>
            <div className='mb-3'>
                <label htmlFor={input.name} className='form-label'>{label}</label>
                <div className='form-check form-switch'>
                    <input {...input}
                        type='checkbox'
                        className='form-check-input'
                        id={input.name}
                        checked={input.value || false}
                        disabled={readOnly}
                        onChange={event => input.onChange(event.target.checked)} />
                </div>
                {helpText && <div className='form-text'>{helpText}</div>}
            </div>
        </Grid>
    );
}
