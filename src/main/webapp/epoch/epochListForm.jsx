import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'

import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputTextarea from '../common/form/labelAndInputTextarea'
import If from '../common/operator/if'
import SystemInfo from '../common/form/systemInfo'
import { onlyNumbers } from '../common/form/formatValues'
import Grid from '../common/layout/grid'
import {
    init,
    addItemList,
    removeItemList,
    submitEpochList
} from './epochActions'


class EpochListForm extends Component {

    componentWillMount() {
        this.props.init(this.props.examId, this.props.epochList);
    }


    renderErrors(errors) {

        if (errors != undefined) {
            return errors.split('\n').map((item, key) => (
                <strong key={key} style={{ 'color': 'red' }}>{item}<br /></strong>
            ))
        } else {
            return false;
        }
    }

    add(index, item) {
        if (!this.props.readOnly) {
            if (!item) {
                item = { 'exam': { 'id': this.props.examId } };
            }

            this.props.addItemList(index, item);
        }
    }

    remove(index) {
        if (!this.props.readOnly && this.props.list.length > 1) {
            this.props.removeItemList(index)
        }
    }

    renderRows() {
        const { readOnly } = this.props;
        const list = this.props.list || [];

        return list.map((item, index) => (
            <div className='panel panel-default display-table' key={index} style={{ 'width': '100%' }} >
                <legend>{this.props.legend}</legend>
                <Field name={`epochs[${index}].startTime.minute`} component={LabelAndInput} readOnly={readOnly}
                    label='Minutos' cols='6 1' placeholder='XX' normalize={onlyNumbers} />
                <Field name={`epochs[${index}].startTime.second`} component={LabelAndInput} readOnly={readOnly}
                    label='Segundos' cols='6 1' placeholder='XX' normalize={onlyNumbers} />
                <Field name={`epochs[${index}].description`} component={LabelAndInputTextarea} readOnly={readOnly}
                    label='Descrição' cols='12 8' placeholder='Informe a Descrição' />

                <If test={this.props.showSystemInfo}>
                    <SystemInfo field={`${this.props.field}[${index}]`} />
                </If>

                <div className='actions-contacts'>
                    <button type='button' className='btn btn-success'
                        onClick={() => this.add(index + 1)}>
                        <i className="fa fa-plus"></i>
                    </button>
                    <button type='button' className='btn btn-warning'
                        onClick={() => {
                            var itemAdd = { ...item };
                            itemAdd.id = undefined;
                            itemAdd.createdAt = undefined;
                            itemAdd.createdBy = undefined;
                            itemAdd.updatedAt = undefined;
                            itemAdd.updatedBy = undefined;
                            this.add(index + 1, itemAdd)
                        }
                        }>
                        <i className="fa fa-copy"></i>
                    </button>
                    <button type='button' className='btn btn-danger'
                        onClick={() => this.remove(index)}>
                        <i className="fa fa-trash-alt"></i>
                    </button>
                </div>
            </ div>
        ))
    }

    render() {
        const { readOnly, pristine, reset, submitting, showSystemInfo, handleSubmit, submitEpochList } = this.props;

        return (
            <form role='form' onSubmit={handleSubmit(submitEpochList)} className='box box-solid'>
                <div className='box'>
                    <div className='box-body' style={{ 'paddingLeft': '0px' }}>
                        {this.renderRows()}
                    </div>
                    <If test={this.props.error != undefined} >
                        <div style={{ 'paddingBottom': '15px', 'display': 'table' }}>
                            <Grid cols='12 12' >
                                {this.renderErrors(this.props.error)}
                            </Grid>
                        </div>
                    </If>
                    <If test={!readOnly}>
                        <div className='box-footer'>
                            <button type='submit'
                                className={`btn btn-primary`}
                                disabled={submitting}>
                                {'Atualizar'}
                            </button>
                            <button type='button' className='btn btn-default'
                                onClick={reset}
                                disabled={pristine || submitting}>
                                {'Cancelar'}
                            </button>
                        </div>
                    </If>
                </div>
            </form >
        )
    }
}

EpochListForm = reduxForm({ form: 'epochListForm', destroyOnUnmount: false })(EpochListForm)
const selector = formValueSelector('epochListForm')
const mapStateToProps = state => ({
    list: selector(state, 'epochs')
});
const mapDispatchToProps = dispatch => bindActionCreators({
    init,
    removeItemList,
    addItemList,
    submitEpochList
}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(EpochListForm)