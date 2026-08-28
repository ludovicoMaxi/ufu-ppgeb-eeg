import React from 'react'
import Grid from '../layout/grid'
import moment from 'moment'

const INPUT_FORMAT = 'DD/MM/YYYY HH:mm:ss'
const ISO_FORMAT = 'YYYY-MM-DDTHH:mm:ss.SSSZ'

function toInputValue(value, formatDate) {
    if (!value) {
        return '';
    }
    const iso = moment(value, moment.ISO_8601);
    const parsed = iso.isValid() ? iso : moment(value, formatDate || INPUT_FORMAT);
    return parsed.isValid() ? parsed.format('YYYY-MM-DD') : '';
}

export default props => {
    const { cols, name, label, input, placeholder, readOnly, formatDate, meta: { error } } = props;
    const hasTime = (formatDate || INPUT_FORMAT).includes('HH');

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
                        input.onChange(dateValue
                            ? moment(dateValue, 'YYYY-MM-DD').format(hasTime ? ISO_FORMAT : 'DD/MM/YYYY')
                            : null);
                    }} />
                {error && <span className="invalid-feedback d-block">{error}</span>}
            </div>
        </Grid>
    )
}
