import React, { Component } from 'react'
import { Field, formValueSelector } from 'redux-form'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import LabelAndInput from './labelAndInput'
import DateTimeInput from './dateTimeInput'
import If from '../operator/if'

class SystemInfo extends Component {
    render() {
        const viewCreatedAt = !!this.props.createdAt;
        const viewCreatedBy = !!this.props.createdBy;
        const viewUpdatedAt = !!this.props.updatedAt;
        const viewUpdatedBy = !!this.props.updatedBy;

        const prefix = this.props.field ? this.props.field + '.' : '';

        return (
            <div className='clear-both'>
                <If test={viewCreatedAt}>
                    <Field name={`${prefix}createdAt`} component={DateTimeInput} readOnly={true}
                        label='Criado em' cols='12 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                </If>
                <If test={viewCreatedBy}>
                    <Field name={`${prefix}createdBy`} component={LabelAndInput} readOnly={true}
                        label='Criado por' cols='12 3' />
                </If>
                <If test={viewUpdatedAt}>
                    <Field name={`${prefix}updatedAt`} component={DateTimeInput} readOnly={true}
                        label='Atualizado em' cols='12 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                </If>
                <If test={viewUpdatedBy}>
                    <Field name={`${prefix}updatedBy`} component={LabelAndInput} readOnly={true}
                        label='Atualizado por' cols='12 3' />
                </If>
            </div>
        )
    }
}


const mapStateToProps = (state, props) => {

    const selector = formValueSelector(props.formName);
    const prefix = props.field ? props.field + '.' : '';

    return {
        createdAt: selector(state, `${prefix}createdAt`),
        createdBy: selector(state, `${prefix}createdBy`),
        updatedAt: selector(state, `${prefix}updatedAt`),
        updatedBy: selector(state, `${prefix}updatedBy`)
    }
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(SystemInfo)