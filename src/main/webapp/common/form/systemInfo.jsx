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
        
        return (
            <div className='clear-both'>
                <If test={viewCreatedAt}>
                    <Field name={`${this.props.field}.createdAt`} component={DateTimeInput} readOnly={true}
                        label='Criado em' cols='12 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                </If>
                <If test={viewCreatedBy}>
                    <Field name={`${this.props.field}.createdBy`} component={LabelAndInput} readOnly={true}
                        label='Criado por' cols='12 3' />
                </If>
                <If test={viewUpdatedAt}>
                    <Field name={`${this.props.field}.updatedAt`} component={DateTimeInput} readOnly={true}
                        label='Atualizado em' cols='12 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                </If>
                <If test={viewUpdatedBy}>
                    <Field name={`${this.props.field}.updatedBy`} component={LabelAndInput} readOnly={true}
                        label='Atualizado por' cols='12 3' />
                </If>
            </div>
        )
    }
}

const selector = formValueSelector('customerForm')
const mapStateToProps = (state, props) => ({
    createdAt: selector(state, `${props.field}.createdAt`),
    createdBy: selector(state, `${props.field}.createdBy`),
    updatedAt: selector(state, `${props.field}.updatedAt`),
    updatedBy: selector(state, `${props.field}.updatedBy`)
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(SystemInfo)