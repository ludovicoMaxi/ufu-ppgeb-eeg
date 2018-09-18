import React from 'react'
import Grid from '../layout/grid'
import DateTimeField from 'react-bootstrap-datetimepicker'
import moment from 'moment'

export default props => {
    const { cols, name, label, input, placeholder, readOnly, formatDate, mode, meta: { error } } = props;

    return (
        <Grid cols={cols}>
            <div className={`form-group ${!!error ? 'has-error' : ''} ${!!readOnly ? 'disable-icon-date' : ''}`}>
                <label htmlFor={name}>{label}</label>
                <DateTimeField
                    inputFormat={formatDate}
                    dateTime={input.value || moment().format(formatDate)}
                    format={formatDate}
                    defaultText={input.value || ''}
                    mode={mode}
                    inputProps={{ 'placeholder': placeholder, 'readOnly': readOnly }}
                    {...input} />
                {error && <span className="help-block">{error}</span>}
            </div>
        </Grid>
    )
}