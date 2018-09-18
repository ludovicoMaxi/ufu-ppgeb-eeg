import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'

import { init } from './customerActions'
import LabelAndInput from '../common/form/labelAndInput'
import DateTimeInput from '../common/form/dateTimeInput'
import Row from '../common/layout/row'
import If from '../common/operator/if'
import Tabs from '../common/tab/tabs';
import TabsHeader from '../common/tab/tabsHeader'
import TabsContent from '../common/tab/tabsContent'
import TabHeader from '../common/tab/tabHeader'
import TabContent from '../common/tab/tabContent'
import ContactList from './contactList'

class CustomerForm extends Component {

    render() {
        const { handleSubmit, readOnly, pristine, reset, submitting, contactList } = this.props
        return (
            <form role='form' onSubmit={handleSubmit} className='box box-solid'>
                <div className='box'>
                    <div className='box-body'>
                        <fieldset>
                            <Row>
                                <Field name='customer.name' component={LabelAndInput} readOnly={readOnly}
                                    label='Nome' cols='12 8' placeholder='Informe o nome' />
                                <Field name='customer.documentNumber' component={LabelAndInput} readOnly={readOnly}
                                    label='CPF / CNPJ' cols='12 4' placeholder='Informe o CPF/CNPF' />
                            </Row>
                            <Row>
                                <Field name='customer.birthDate' component={DateTimeInput} readOnly={readOnly} mode='date'
                                    label='Data de Nascimento' cols='12 3' placeholder='Data Nascimento' formatDate='DD/MM/YYYY' />
                                <Field name='customer.nacionality' component={LabelAndInput} readOnly={readOnly}
                                    label='Nacionalidade' cols='12 3' placeholder='Informe a Nacionalidade' />
                                <Field name='customer.secondurayDocumentType' component={LabelAndInput} readOnly={readOnly}
                                    label='Tipo Doc.' cols='3 2' placeholder='Tipo' />
                                <Field name='customer.secondurayDocumentNumber' component={LabelAndInput} readOnly={readOnly}
                                    label='N° Documento' cols='9 4' placeholder='Informe o N° Documento' />
                            </Row>
                            <Row>
                                <Field name='customer.civilStatus' component={LabelAndInput} readOnly={readOnly}
                                    label='Estado Civil' cols='12 3' placeholder='Estado Civil' />
                                <Field name='customer.job' component={LabelAndInput} readOnly={readOnly}
                                    label='Profissão' cols='12 3' placeholder='Informe a Profissão' />
                            </Row>
                            <If test={this.props.showSystemInfo}>
                                <Row>
                                    <Field name='customer.createdAt' component={DateTimeInput} readOnly={true}
                                        label='Criado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                    <Field name='customer.createdBy' component={LabelAndInput} readOnly={true}
                                        label='Criado por' cols='6 3' />
                                    <Field name='customer.updatedAt' component={DateTimeInput} readOnly={true}
                                        label='Atualizado em' cols='6 3' formatDate='DD/MM/YYYY HH:mm:ss' />
                                    <Field name='customer.updatedBy' component={LabelAndInput} readOnly={true}
                                        label='Atualizado por' cols='6 3' />
                                </Row>
                            </If>
                            <hr style={{ 'marginTop': '0px', 'marginBottom': '0px' }} />
                        </fieldset>
                    </div>
                    <Tabs>
                        <TabsHeader>
                            <TabHeader label='Contato' icon='address-card' target='tabContact' />
                            <TabHeader label='Endereço' icon='map-marker' target='tabAddress' />
                        </TabsHeader>
                        <TabsContent>
                            <TabContent id='tabContact'>
                                <ContactList list={contactList} readOnly={readOnly}
                                    field='contactList'></ContactList>
                            </TabContent>
                            <TabContent id='tabAddress'><h1>Aba Endereço em construção</h1>
                            </TabContent>
                        </TabsContent>
                    </Tabs>
                    {this.props.error && <strong>{this.props.error}</strong>}
                    <div className='box-footer'>
                        <button type='submit'
                            className={`btn btn-${this.props.submitClass}`}
                            disabled={submitting}>
                            {this.props.submitLabel}
                        </button>
                        <button type='button' className='btn btn-default'
                            onClick={this.props.init}
                            disabled={pristine || submitting}>
                            {'Cancelar'}
                        </button>
                    </div>
                </div>
            </form >
        )
    }
}

CustomerForm = reduxForm({ form: 'customerForm', destroyOnUnmount: false })(CustomerForm)
const selector = formValueSelector('customerForm')
const mapStateToProps = state => ({
    birthDate: selector(state, 'customer.birthDate'),
    createdAt: selector(state, 'customer.createdAt'),
    updateAt: selector(state, 'customer.updateAt'),
    contactList: selector(state, 'contactList')
})

const mapDispatchToProps = dispatch => bindActionCreators({ init }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(CustomerForm)