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

        return (
            <Grid cols={cols}>
                <div className={`form-group ${!!error ? 'has-error' : ''}`}>
                    <label htmlFor={input.name}>{label}</label>
                    <Select {...this.input}
                        id={input.name}
                        isDisabled={readOnly}
                        placeholder={placeholder}
                        onBlur={() => input.onBlur(input.value)}
                        onChange={this.onChange.bind(this)}
                        options={options}
                        value={this.defaultOption()}
                    />
                    {error && <span className="help-block">{error}</span>}
                </div>
            </Grid>
        );
    }
}