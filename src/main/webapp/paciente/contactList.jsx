import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { Field, arrayInsert, arrayRemove } from 'redux-form'
import LabelAndInput from '../common/form/labelAndInput'
import LabelAndInputCheckbox from '../common/form/labelAndInputCheckbox'

class ItemList extends Component {

    add(index, item = {}) {
        if (!this.props.readOnly) {
            this.props.arrayInsert('customerForm', this.props.field, index, item)
        }
    }

    remove(index) {
        if (!this.props.readOnly && this.props.list.length > 1) {
            this.props.arrayRemove('customerForm', this.props.field, index)
        }
    }

    renderRows() {
        const list = this.props.list || []
        return list.map((item, index) => (
            <div className='panel panel-default overflow-auto' key={index}>
                <legend>{this.props.legend}</legend>
                <Field name={`${this.props.field}[${index}].name`} component={LabelAndInput} readOnly={this.props.readOnly}
                    label='Nome' cols='12 4' placeholder='Informe o nome' />
                <Field name={`${this.props.field}[${index}].phone`} component={LabelAndInput} readOnly={this.props.readOnly}
                    label='Telefone' cols='6 2' placeholder='Informe o telefone' />
                <Field name={`${this.props.field}[${index}].cellphone`} component={LabelAndInput} readOnly={this.props.readOnly}
                    label='Celular' cols='6 2' placeholder='Informe o Celular' />
                <Field name={`${this.props.field}[${index}].email`} component={LabelAndInput} readOnly={this.props.readOnly}
                    label='E-mail' cols='12 4' placeholder='Informe o e-mail' />
                <Field name={`${this.props.field}[${index}].facebook`} component={LabelAndInput} readOnly={this.props.readOnly}
                    label='Facebook' cols='12 4' placeholder='Informe o Facebook' />
                <Field name={`${this.props.field}[${index}].instagram`} component={LabelAndInput} readOnly={this.props.readOnly}
                    label='Instagran' cols='12 4' placeholder='Informe o Instagram' />
                <Field name={`${this.props.field}[${index}].whatsapp`} component={LabelAndInput} readOnly={this.props.readOnly}
                    label='Whatsapp' cols='6 2' placeholder='Informe o Whatsapp' />
                <Field name={`${this.props.field}[${index}].main`} component={LabelAndInputCheckbox} readOnly={this.props.readOnly}
                    label='Principal' cols='6 2' typeStyle='icheckbox_flat-blue' />
                <div className='actions-contacts'>
                    <button type='button' className='btn btn-success'
                        onClick={() => this.add(index + 1)}>
                        <i className="fa fa-plus"></i>
                    </button>
                    <button type='button' className='btn btn-warning'
                        onClick={() => this.add(index + 1, item)}>
                        <i className="fa fa-clone"></i>
                    </button>
                    <button type='button' className='btn btn-danger'
                        onClick={() => this.remove(index)}>
                        <i className="fa fa-trash-o"></i>
                    </button>
                </div>
            </div>
        ))
    }

    render() {
        return (
            <div>
                {this.renderRows()}
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ arrayInsert, arrayRemove }, dispatch)
export default connect(null, mapDispatchToProps)(ItemList)