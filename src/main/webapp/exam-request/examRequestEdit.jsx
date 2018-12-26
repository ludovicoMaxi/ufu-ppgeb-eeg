import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { getExamRequestById, submitExamRequest } from './examRequestActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import PatientForm from '../patient/patientForm'
import ExamRequestForm from './examRequestForm'

class PatientEdit extends Component {

    componentWillMount() {
        const { examRequestId } = this.props.match.params;
        this.props.getExamRequestById(examRequestId);
    }

    render() {

        return (
            <div><ContentHeader title='Requerimento' small='Edição' />
                <Content>
                    <PatientForm readOnly='true' />
                    <ExamRequestForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitExamRequest}
                        showSystemInfo={true} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ getExamRequestById, submitExamRequest }, dispatch)
export default connect(null, mapDispatchToProps)(PatientEdit)