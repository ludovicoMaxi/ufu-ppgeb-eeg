import React from 'react'
import Grid from '../layout/grid'

export default props => {
    const { cols, name, label, input, placeholder, optionList, readOnly, type, meta: { error } } = props;
    const optionsListInit = optionList || [];

    return (
        <Grid cols={cols}>
            <div className={`form-group ${!!error ? 'has-error' : ''}`}>
                <label htmlFor={name}>{label}</label>
                <select {...input} className='form-control'
                    placeholder={placeholder}
                    readOnly={readOnly} type={type}>
                    <option></option>
                    {
                        optionsListInit.map((item, index) => (
                            <option value={item.value} key={index}>{item.label}</option>
                        ))
                    }
                </select>
                {error && <span className="help-block">{error}</span>}
            </div>
        </Grid>
    );
}