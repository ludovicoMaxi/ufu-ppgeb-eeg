import React, { Component } from 'react'

import Grid from '../layout/grid'

class LabelAndInputTextarea extends Component {

    render() {
        const { cols, name, label, input, placeholder, readOnly, type, meta: { error } } = this.props;

        return (
            <Grid cols={cols}>
                <div className={`form-group ${!!error ? 'has-error' : ''}`}>
                    <label htmlFor={name}>{label}</label>
                    <textarea {...input} className='form-control'
                        placeholder={placeholder}
                        readOnly={readOnly} type={type} />
                    {error && <span className="help-block">{error}</span>}
                </div>
            </Grid>
        )
    };
}

export default LabelAndInputTextarea
LabelAndInputTextarea.defaultProps = {
    meta: {}
};