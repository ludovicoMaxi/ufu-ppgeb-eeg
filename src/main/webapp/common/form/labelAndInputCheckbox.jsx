import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, label, input, readOnly } = props;

    return (
        <Grid cols={cols}>
            <div className='form-check mt-4'>
                <input {...input}
                    type='checkbox'
                    className='form-check-input'
                    id={input.name}
                    checked={input.value || false}
                    disabled={readOnly}
                    onChange={event => input.onChange(event.target.checked)} />
                <label htmlFor={input.name} className='form-check-label'>{` ${label}`}</label>
            </div>
        </Grid>
    );
}
