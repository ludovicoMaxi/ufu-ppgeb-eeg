import React from 'react'
import Grid from '../layout/grid'
import moment from 'moment'

const INPUT_FORMAT = 'DD/MM/YYYY HH:mm:ss'

function toInputValue(value, formatDate) {
    if (!value) {
        return '';
    }
    const parsed = moment(value, formatDate || INPUT_FORMAT);
    return parsed.isValid() ? parsed.format('YYYY-MM-DD') : '';
}

export default props => {
    const { cols, name, label, input, placeholder, readOnly, formatDate, meta: { error } } = props;

    return (
        <Grid cols={cols}>
            <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={name} className='form-label'>{label}</label>
                <input {...input}
                    type='date'
                    className={`form-control ${!!error ? 'is-invalid' : ''}`}
                    id={name}
                    value={toInputValue(input.value, formatDate)}
                    placeholder={placeholder}
                    readOnly={readOnly}
                    onChange={event => {
                        const dateValue = event.target.value;
                        const formatted = dateValue
                            ? moment(dateValue, 'YYYY-MM-DD').format(formatDate || INPUT_FORMAT)
                            : null;
                        input.onChange(formatted);
                    }} />
                {error && <span className="invalid-feedback d-block">{error}</span>}
            </div>
        </Grid>
    )
}
