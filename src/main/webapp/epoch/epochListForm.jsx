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
    submitEpochList,
    updateFinalTime
} from './epochActions'


class EpochListForm extends Component {

    componentWillMount() {
        this.props.init(this.props.examId);
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
                item = {};
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
        const { readOnly, change } = this.props;
        const list = this.props.list || [];

        const handleChangeDuration = function (event, value) {
            var index = getIndex(event);

            if (!list[index].startTime) {
                return;
            }
            var initialMinutes = list[index].startTime.minute;
            var initialSeconds = list[index].startTime.second;

            if ((!!initialMinutes || initialMinutes == 0) && (!!initialSeconds || initialSeconds == 0)) {
                var finalTime = Number(initialMinutes) * 60 + Number(initialSeconds) + Number(value);
                updateFinalTime(finalTime, event);
            }
        }

        const handleChangeMinute = function (event, value) {
            var index = getIndex(event);

            if (!list[index].startTime) {
                return;
            }
            var duration = list[index].duration;
            var initialSeconds = list[index].startTime.second;

            if ((!!duration || duration == 0) && (!!initialSeconds || initialSeconds == 0)) {
                var finalTime = Number(value) * 60 + Number(initialSeconds) + Number(duration);
                updateFinalTime(finalTime, event);
            }
        }

        const handleChangeSecond = function (event, value) {
            var index = getIndex(event);

            if (!list[index].startTime) {
                return;
            }
            var initialMinutes = list[index].startTime.minute;
            var duration = list[index].duration;

            if ((!!initialMinutes || initialMinutes == 0) && (!!duration || duration == 0)) {
                var finalTime = Number(initialMinutes) * 60 + Number(value) + Number(duration);
                updateFinalTime(finalTime, event);
            }
        }

        const getIndex = function (event) {
            var onlyNumbers = new RegExp('\\d+');
            return event.target.id.match(onlyNumbers);
        }

        const updateFinalTime = function (finalTime, event) {
            var name = event.target.name.split('.')[0];
            change(name + '.finalTime.minute', Math.trunc(finalTime / 60));
            change(name + '.finalTime.second', finalTime % 60);
        }

        return list.map((item, index) => (
            <div className='panel panel-default display-table' key={index} style={{ 'width': '100%' }} >
                <legend>{this.props.legend}</legend>
                <Field name={`epochs[${index}].startTime.minute`} component={LabelAndInput} readOnly={readOnly}
                    label='Minutos' cols='4 1' placeholder='XX' normalize={onlyNumbers}
                    onChange={handleChangeMinute} />
                <Field name={`epochs[${index}].startTime.second`} component={LabelAndInput} readOnly={readOnly}
                    label='Segundos' cols='4 1' placeholder='XX' normalize={onlyNumbers}
                    onChange={handleChangeSecond} />
                <Field name={`epochs[${index}].duration`} component={LabelAndInput} readOnly={readOnly}
                    label='Duração (Segundos)' cols='4 2' placeholder='XXX' normalize={onlyNumbers}
                    onChange={handleChangeDuration} />
                <Field name={`epochs[${index}].finalTime.minute`} component={LabelAndInput} readOnly={true}
                    label='Minutos' cols='6 1' placeholder='XX' normalize={onlyNumbers} />
                <Field name={`epochs[${index}].finalTime.second`} component={LabelAndInput} readOnly={true}
                    label='Segundos' cols='6 1' placeholder='XX' normalize={onlyNumbers} />

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

                <Field name={`epochs[${index}].description`} component={LabelAndInputTextarea} readOnly={readOnly}
                    label='Descrição' cols='12 12' placeholder='Informe a Descrição' />

            </ div>
        ))
    }

    render() {
        const { readOnly, pristine, reset, submitting, showSystemInfo, handleSubmit, submitEpochList } = this.props;

        return (
            <form role='form' onSubmit={handleSubmit(submitEpochList)} className='box box-solid'>
                <div className='box'>
                    <div className='box-body' style={{ 'paddingLeft': '0px' }}>
                        <div style={{ 'display': 'none' }}>
                            <Field name={'examI'} component={LabelAndInput} readOnly={true} normalize={onlyNumbers} />
                        </div>
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
    submitEpochList,
    updateFinalTime
}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(EpochListForm)