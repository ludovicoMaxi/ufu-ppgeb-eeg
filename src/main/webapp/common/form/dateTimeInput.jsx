import React, { useEffect, useRef, useState } from 'react'
import Grid from '../layout/grid'
import moment from 'moment'
import { dateMask, dateTimeMask } from './formatValues'

const ISO_DATE = 'YYYY-MM-DD'
const ISO_DATE_TIME = 'YYYY-MM-DDTHH:mm:ss.SSSZ'
const PICKER_DATE_TIME = 'YYYY-MM-DDTHH:mm:ss'
const PICKER_DATE_TIME_NO_SECONDS = 'YYYY-MM-DDTHH:mm'
const DISPLAY_DATE = 'DD/MM/YYYY'
const DISPLAY_DATE_TIME = 'DD/MM/YYYY HH:mm:ss'
const DISPLAY_DATE_TIME_LENGTH = DISPLAY_DATE_TIME.length

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
    const formats = hasTime ? [DISPLAY_DATE_TIME, DISPLAY_DATE] : [DISPLAY_DATE];
    const parsed = moment(text, formats, true);
    return parsed.isValid() ? parsed.format(hasTime ? ISO_DATE_TIME : ISO_DATE) : null;
}

function toPickerValue(text, hasTime) {
    const parsed = moment(text, [DISPLAY_DATE_TIME, DISPLAY_DATE], true);
    if (!parsed.isValid()) {
        return '';
    }
    return parsed.format(hasTime ? PICKER_DATE_TIME : ISO_DATE);
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

    const mask = hasTime ? dateTimeMask : dateMask;
    const maxLength = hasTime ? DISPLAY_DATE_TIME_LENGTH : DISPLAY_DATE.length;

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

    const onPickerChange = event => {
        const isoValue = event.target.value;
        if (!isoValue) {
            return;
        }
        const picked = hasTime
            ? moment(isoValue, [PICKER_DATE_TIME, PICKER_DATE_TIME_NO_SECONDS], true)
            : moment(isoValue, ISO_DATE, true);
        if (!picked.isValid()) {
            return;
        }
        let display = picked.format(DISPLAY_DATE);
        if (hasTime) {
            display = picked.format(DISPLAY_DATE_TIME);
        }
        setText(display);
        input.onChange(toBackend(display, hasTime));
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
                        maxLength={maxLength}
                        onChange={event => {
                            const masked = mask(event.target.value);
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
                    <input ref={pickerRef}
                        type={hasTime ? 'datetime-local' : 'date'}
                        step={hasTime ? 1 : undefined}
                        tabIndex={-1} aria-hidden='true'
                        className='date-picker-hidden'
                        value={toPickerValue(text, hasTime)}
                        onChange={onPickerChange} />
                </div>
                {error && <span className="invalid-feedback d-block">{error}</span>}
            </div>
        </Grid>
    )
}
