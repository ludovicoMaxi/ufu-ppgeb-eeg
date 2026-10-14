import React, { useEffect, useRef, useState } from 'react'
import Grid from '../layout/grid'
import moment from 'moment'
import { dateMask } from './formatValues'

const ISO_DATE = 'YYYY-MM-DD'
const ISO_DATE_TIME = 'YYYY-MM-DDTHH:mm:ss.SSSZ'
const DISPLAY_DATE = 'DD/MM/YYYY'
const DISPLAY_DATE_TIME = 'DD/MM/YYYY HH:mm:ss'

function formatDisplay(value, hasTime) {
    if (!value) {
        return '';
    }
    const iso = moment(value, moment.ISO_8601, true);
    const parsed = iso.isValid()
        ? iso
        : moment(value, [DISPLAY_DATE_TIME, DISPLAY_DATE, ISO_DATE], true);
    return parsed.isValid() ? parsed.format(hasTime ? DISPLAY_DATE_TIME : DISPLAY_DATE) : '';
}

function toBackend(text, hasTime) {
    const parsed = moment(text, DISPLAY_DATE, true);
    return parsed.isValid() ? parsed.format(hasTime ? ISO_DATE_TIME : ISO_DATE) : null;
}

function toPickerValue(text) {
    const parsed = moment(text, DISPLAY_DATE, true);
    return parsed.isValid() ? parsed.format(ISO_DATE) : '';
}

export default props => {
    const { cols, label, input, placeholder, readOnly, formatDate, meta: { error } } = props;
    const hasTime = (formatDate || DISPLAY_DATE_TIME).includes('HH');
    const [text, setText] = useState(() => formatDisplay(input.value, hasTime));
    const pickerRef = useRef(null);

    useEffect(() => {
        if (input.value !== toBackend(text, hasTime)) {
            setText(formatDisplay(input.value, hasTime));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [input.value]);

    if (readOnly) {
        return (
            <Grid cols={cols}>
                <div className='mb-3'>
                    <label htmlFor={input.name} className='form-label'>{label}</label>
                    <input className='form-control' id={input.name}
                        value={formatDisplay(input.value, hasTime)} readOnly />
                </div>
            </Grid>
        );
    }

    const openPicker = () => {
        const picker = pickerRef.current;
        if (!picker) {
            return;
        }
        if (typeof picker.showPicker === 'function') {
            try {
                picker.showPicker();
            } catch (e) {
                picker.focus();
            }
        } else {
            picker.focus();
        }
    };

    return (
        <Grid cols={cols}>
            <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={input.name} className='form-label'>{label}</label>
                <div className='input-group'>
                    <input type='text'
                        className={`form-control ${!!error ? 'is-invalid' : ''}`}
                        id={input.name}
                        name={input.name}
                        value={text}
                        placeholder={placeholder}
                        maxLength={10}
                        onChange={event => {
                            const masked = dateMask(event.target.value);
                            setText(masked);
                            input.onChange(toBackend(masked, hasTime));
                        }}
                        onBlur={() => {
                            if (toBackend(text, hasTime) === null) {
                                setText('');
                            }
                        }} />
                    <button type='button' className='btn btn-outline-secondary' tabIndex={-1}
                        onClick={openPicker}>
                        <i className='fa fa-calendar' />
                    </button>
                    <input ref={pickerRef} type='date' tabIndex={-1} aria-hidden='true'
                        className='date-picker-hidden'
                        value={toPickerValue(text)}
                        onChange={event => {
                            const isoValue = event.target.value;
                            if (isoValue) {
                                const display = moment(isoValue, ISO_DATE, true).format(DISPLAY_DATE);
                                setText(display);
                                input.onChange(toBackend(display, hasTime));
                            }
                        }} />
                </div>
                {error && <span className="invalid-feedback d-block">{error}</span>}
            </div>
        </Grid>
    )
}
