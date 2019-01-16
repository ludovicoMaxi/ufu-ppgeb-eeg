import React from 'react'
import { Checkbox } from 'react-icheck'
import Grid from '../layout/grid'

export default props => {
    const { cols, label, input, readOnly, typeStyle } = props;

    return (
        <Grid cols={cols}>
            <div>
                <br />
                <Checkbox
                    {...input} readOnly={readOnly}
                    id={input.name}
                    checkboxClass={typeStyle}
                    increaseArea="20%"
                    label={`  ${label}`}
                    checked={input.value || false}
                    disabled={readOnly}
                />
                {error && <span className="help-block">{error}</span>}
            </div>
        </Grid>
    );
}