import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'

import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputCheckbox from '../common/form/labelAndInputCheckbox'
import If from '../common/operator/if'
import SystemInfo from '../common/form/systemInfo'

class AnswerOptionForm extends Component {

    renderErrors(errors) {

        if (errors != undefined) {
            return errors.split('\n').map((item, key) => (
                <strong key={key} style={{ 'color': 'red' }}>{item}<br /></strong>
            ))
        } else {
            return false;
        }
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
            onRemove
        } = this.props;

        return (
            <form role='form' onSubmit={handleSubmit} className='card'>
                <div>
                    <div className='card-body'>
                        <fieldset>
                            <legend>Alternativa de Resposta</legend>
                            <div className='row'>
                                <Field name='text' component={LabelAndInput} readOnly={readOnly}
                                    label='Texto' cols='12 8' placeholder='Informe o texto da alternativa' />
                                <Field name='active' component={LabelAndInputCheckbox} readOnly={readOnly}
                                    label='Ativa' cols='12 4' />
                            </div>
                            <If test={showSystemInfo}>
                                <SystemInfo formName='answerOptionForm' />
                            </If>
                            <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                        </fieldset>
                    </div>
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

AnswerOptionForm = reduxForm({ form: 'answerOptionForm', destroyOnUnmount: false })(AnswerOptionForm)
const selector = formValueSelector('answerOptionForm')
const mapStateToProps = state => ({
    id: selector(state, 'id')
})

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(AnswerOptionForm)
