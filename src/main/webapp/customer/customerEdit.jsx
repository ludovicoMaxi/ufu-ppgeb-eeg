import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getCustomerById, submitCustomerView, showUpdate, findContactByCustomer } from './customerActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import CustomerForm from './customerForm'

class CustomerEdit extends Component {

    componentWillMount() {
        this.props.init();
        const { customerId } = this.props.match.params;
        this.props.getCustomerById(customerId);
        this.props.findContactByCustomer(customerId);
    }

    render() {

        return (
            <div><ContentHeader title='Associados' small='Edição' />
                <Content>
                    <CustomerForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitCustomerView}
                        showSystemInfo={true} />
                </Content>
            </div>
        )
    }
}

const mapStateToProps = state => ({ customer: state.customer })
const mapDispatchToProps = dispatch => bindActionCreators({ init, getCustomerById, submitCustomerView, showUpdate, findContactByCustomer }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(CustomerEdit)