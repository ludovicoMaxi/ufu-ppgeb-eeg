import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import {
    reduxForm,
    Field,
    formValueSelector,
    SubmissionError,
    submit
} from 'redux-form'

import LabelAndInputTextarea from '../common/form/labelAndInputTextarea'
import { init, find, create } from './notesActions'
import Grid from '../common/layout/grid'

class NotesList extends Component {

    componentWillMount() {
        this.props.find(this.props.objectId, this.props.objectType);
    }

    add(item = {}) {
        this.props.formArrayInsert('notesForm', 'notesList', this.props.notesList.length + 1, item)
    }

    renderRows() {
        const list = this.props.notesList || []

        return list.map((item, index) => (
            <div className='panel panel-default display-table' key={index} style={{ 'width': '100%' }}>
                <legend>{this.props.legend}</legend>
                <Field name={`notesList[${index}].message`} component={LabelAndInputTextarea} readOnly={true}
                    label={<div>Nota <small>({this.props.notesList[index].createdBy} {this.props.notesList[index].createdAt})</small></div>} cols='12 12' placeholder='Informe a Nota' />
            </div>
        ))
    }

    sendSubmit() {

        this.props.submit('notesForm');

        if (!!this.props.note) {
            this.props.create({
                'message': this.props.note,
                'objectId': this.props.objectId,
                'objectType': this.props.objectType
            });
        }
    }

    render() {
        return (
            <div>
                {this.renderRows()}
                <div className='panel panel-default display-table' style={{ 'width': '100%' }}>
                    <legend>{this.props.legend}</legend>
                    <Field name={'note'} component={LabelAndInputTextarea} readOnly={this.props.readOnly}
                        label='Nota' cols='12 10 11' placeholder='Informe a Nota' />

                    <div className='actions-notes'>
                        <Grid cols='2 1'>
                            <button type='button' className='btn btn-success'
                                onClick={() => {
                                    this.sendSubmit()
                                }}>
                                <i className="fa fa-plus"></i>
                            </button>
                        </Grid>
                    </div>
                </div>
            </div>
        )
    }
}

function handleSubmit(values) {
    const errors = { 'note': {} }
    if (!values.note) {
        errors.note = 'A nota é obrigatória!'
        throw new SubmissionError(errors);
    }
};

NotesList = reduxForm({ form: 'notesForm', destroyOnUnmount: false, onSubmit: handleSubmit })(NotesList)
const selector = formValueSelector('notesForm')
const mapStateToProps = (state) => ({
    notesList: selector(state, 'notesList'),
    list: state.notes.list,
    note: selector(state, 'note')
});
const mapDispatchToProps = dispatch => bindActionCreators({
    init,
    find,
    create,
    submit
}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(NotesList)