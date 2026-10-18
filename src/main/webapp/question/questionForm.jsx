import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'

import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputSwitch from '../common/form/labelAndInputSwitch'
import LabelAndInputSelect from '../common/form/labelAndInputSelect'
import LabelAndInputNativeSelect from '../common/form/labelAndInputNativeSelect'
import LabelAndInputTextarea from '../common/form/labelAndInputTextarea'
import If from '../common/operator/if'
import SystemInfo from '../common/form/systemInfo'
import { onlyNumbers } from '../common/form/formatValues'
import { getOptionsAnswerOption } from '../answerOption/answerOptionActions'
import { addOptionItem, removeOptionItem, QUESTION_TYPE_OPTIONS, FREE_TEXT } from './questionActions'

class QuestionForm extends Component {

    componentDidMount() {
        this.props.getOptionsAnswerOption();
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
            this.props.addOptionItem(index, item);
        }
    }

    remove(index) {
        if (!this.props.readOnly && (this.props.options || []).length > 1) {
            this.props.removeOptionItem(index);
        }
    }

    renderOptionRows() {
        const { readOnly, optionsAnswerOption } = this.props;
        const list = this.props.options || [{}];

        return list.map((item, index) => (
            <div className='card mb-3' key={index} style={{ 'width': '100%' }}>
                <div className='card-body'>
                    <div className='row'>
                        <Field name={`options[${index}].answerOption`} component={LabelAndInputSelect} readOnly={readOnly}
                            label='Alternativa' cols='12 8' placeholder='Alternativa' options={optionsAnswerOption} />
                        <Field name={`options[${index}].sortOrder`} component={LabelAndInput} readOnly={readOnly}
                            label='Ordem' cols='6 2' placeholder='Ordem' normalize={onlyNumbers} />
                        <If test={!readOnly}>
                            <div className='col-6 col-sm-2 actions-contacts'>
                                <button type='button' className='btn btn-success'
                                    onClick={() => this.add(index + 1)}>
                                    <i className='fa fa-plus'></i>
                                </button>
                                <button type='button' className='btn btn-warning'
                                    onClick={() => {
                                        var itemAdd = { ...item };
                                        itemAdd.id = undefined;
                                        this.add(index + 1, itemAdd);
                                    }}>
                                    <i className='fa fa-copy'></i>
                                </button>
                                <button type='button' className='btn btn-danger'
                                    onClick={() => this.remove(index)}>
                                    <i className='fa fa-trash-alt'></i>
                                </button>
                            </div>
                        </If>
                    </div>
                </div>
            </div>
        ))
    }

    render() {
        const {
            handleSubmit,
            readOnly,
            pristine,
            submitting,
            submitClass,
            submitLabel,
            showSystemInfo,
            showRemove,
            reset,
            id,
            questionType,
            onRemove
        } = this.props;

        const isSelection = !!questionType && questionType !== FREE_TEXT;

        return (
            <form role='form' onSubmit={handleSubmit} className='card'>
                <div>
                    <div className='card-body'>
                        <fieldset>
                            <legend>Questão</legend>
                            <div className='row'>
                                <Field name='text' component={LabelAndInput} readOnly={readOnly}
                                    label='Texto' cols='12 8' placeholder='Informe o texto da questão' />
                                <Field name='questionType' component={LabelAndInputNativeSelect} readOnly={readOnly}
                                    label='Tipo' cols='6 2' placeholder='Tipo' options={QUESTION_TYPE_OPTIONS} />
                                <Field name='active' component={LabelAndInputSwitch} readOnly={readOnly}
                                    label='Ativa' cols='6 2' />
                                <Field name='description' component={LabelAndInputTextarea} readOnly={readOnly}
                                    label='Descrição' cols='12 12' placeholder='Informe a descrição' />
                            </div>
                            <If test={showSystemInfo}>
                                <SystemInfo formName='questionForm' />
                            </If>
                            <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                        </fieldset>
                    </div>

                    <If test={isSelection}>
                        <div className='card-body' style={{ 'paddingTop': '0px' }}>
                            <fieldset>
                                <legend>Alternativas</legend>
                                {this.renderOptionRows()}
                            </fieldset>
                        </div>
                    </If>

                    <If test={!!this.props.error}>
                        <div className='card-body' style={{ 'paddingTop': '0px' }}>
                            {this.renderErrors(this.props.error)}
                        </div>
                    </If>

                    <If test={!readOnly}>
                        <div className='card-footer'>
                            <button type='submit'
                                className={`btn btn-${submitClass}`}
                                disabled={submitting}>
                                {submitLabel}
                            </button>
                            <button type='button' className='btn btn-default'
                                onClick={reset}
                                disabled={pristine || submitting}>
                                {'Cancelar'}
                            </button>
                            <If test={showRemove && !!id}>
                                <button type='button' className='btn btn-danger float-end'
                                    onClick={() => onRemove({ id })}
                                    disabled={submitting}>
                                    {'Remover'}
                                </button>
                            </If>
                        </div>
                    </If>
                </div>
            </form>
        )
    }
}

QuestionForm = reduxForm({ form: 'questionForm', destroyOnUnmount: false })(QuestionForm)
const selector = formValueSelector('questionForm')
const mapStateToProps = state => ({
    optionsAnswerOption: state.answerOption.optionsAnswerOption,
    id: selector(state, 'id'),
    questionType: selector(state, 'questionType'),
    options: selector(state, 'options')
})

const mapDispatchToProps = dispatch => bindActionCreators(
    { getOptionsAnswerOption, addOptionItem, removeOptionItem }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(QuestionForm)
