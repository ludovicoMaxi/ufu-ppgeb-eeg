import React, { Component } from 'react'

import Grid from '../layout/grid'

class LabelAndInputTextarea extends Component {

    render() {
        const { cols, name, label, input, placeholder, readOnly, type, meta: { error } } = this.props;

        return (
            <Grid cols={cols}>
                <div className={`mb-3 ${!!error ? 'has-error' : ''}`}>
                    <label htmlFor={input.name} className='form-label'>{label}</label>
                    <textarea {...input} className={`form-control ${!!error ? 'is-invalid' : ''}`}
                        id={input.name}
                        placeholder={placeholder}
                        readOnly={readOnly} type={type} />
                    {error && <span className="invalid-feedback d-block">{error}</span>}
                </div>
            </Grid>
        )
    };
}

export default LabelAndInputTextarea
LabelAndInputTextarea.defaultProps = {
    meta: {}
};
