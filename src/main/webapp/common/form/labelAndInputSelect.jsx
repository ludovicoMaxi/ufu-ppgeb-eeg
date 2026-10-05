import React, { Component } from 'react'
import Select from 'react-select'
import Grid from '../layout/grid'

import deepCompare from './deepCompare'

export default class LabelAndInputSelect extends Component {

    onChange(event) {

        if (this.props.input.onChange && event != null) {
            // To be aligned with how redux-form publishes its CHANGE action payload. The event received is an object with 2 keys: "value" and "label"
            this.props.input.onChange(event.value);
        } else {
            // Clear the input field
            this.props.input.onChange(null)
        }
    }

    defaultOption() {
        const value = this.props.input.value;
        const options = this.props.options;

        if (typeof value !== 'boolean' && !value) {
            return null;
        }

        if (!!options) {
            for (var i = 0; i < options.length; i++) {
                if (deepCompare(value, options[i].value)) {
                    return options[i];
                }
            }
        }
    }

    render() {
        const { cols, label, input, options, placeholder, readOnly, meta: { error } } = this.props;
        const isDisabled = !!readOnly;

        return (
            <Grid cols={cols}>
                <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                    <label htmlFor={input.name} className='form-label'>{label}</label>
                    <Select {...input}
                        id={input.name}
                        isDisabled={isDisabled}
                        placeholder={placeholder}
                        onBlur={() => input.onBlur(input.value)}
                        onChange={this.onChange.bind(this)}
                        options={options}
                        value={this.defaultOption()}
                        styles={{
                            control: base => ({
                                ...base,
                                minHeight: 'var(--app-select-min-height)',
                                height: 'var(--app-select-min-height)',
                                alignItems: 'center',
                                padding: 'var(--app-select-padding)',
                                borderRadius: 'var(--app-select-radius)',
                                backgroundColor: isDisabled ? 'var(--app-select-disabled-bg)' : 'var(--app-select-bg)',
                                color: 'var(--app-select-color)',
                                borderColor: error ? 'var(--app-select-danger)' : 'var(--app-select-border)',
                                boxShadow: 'none',
                                '&:hover, &:focus': {
                                    borderColor: error ? 'var(--app-select-danger)' : 'var(--app-select-focus-border)'
                                }
                            }),
                            valueContainer: base => ({
                                ...base,
                                padding: '0',
                                color: 'var(--app-select-color)'
                            }),
                            input: base => ({
                                ...base,
                                color: 'var(--app-select-color)'
                            }),
                            singleValue: base => ({
                                ...base,
                                color: 'var(--app-select-color)'
                            }),
                            placeholder: base => ({
                                ...base,
                                color: 'var(--app-select-placeholder)'
                            }),
                            indicatorSeparator: base => ({
                                ...base,
                                backgroundColor: 'transparent'
                            }),
                            dropdownIndicator: base => ({
                                ...base,
                                padding: '0 4px',
                                color: 'var(--app-select-placeholder)',
                                ':hover': {
                                    color: 'var(--app-select-color)'
                                }
                            }),
                            menu: base => ({
                                ...base,
                                borderRadius: 'var(--app-select-radius)',
                                backgroundColor: 'var(--app-select-bg)',
                                boxShadow: 'var(--app-select-shadow)'
                            }),
                            menuList: base => ({
                                ...base,
                                padding: '0.25rem 0'
                            }),
                            option: base => ({
                                ...base,
                                backgroundColor: 'var(--app-select-bg)',
                                color: 'var(--app-select-color)',
                                '&:hover, &:focus': {
                                    backgroundColor: 'var(--app-select-option-hover-bg)'
                                },
                                '&:active': {
                                    backgroundColor: 'var(--app-select-option-hover-bg)'
                                }
                            })
                        }}
                    />
                    {error && <span className="invalid-feedback d-block">{error}</span>}
                </div>
            </Grid>
        );
    }
}
