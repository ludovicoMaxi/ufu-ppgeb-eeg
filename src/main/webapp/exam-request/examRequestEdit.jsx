import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { getExamRequestById, submitExamRequest, getExamByExamRequestId } from './examRequestActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import PatientForm from '../patient/patientForm'
import ExamRequestForm from './examRequestForm'

class PatientEdit extends Component {

    componentWillMount() {
        const { examRequestId } = this.props.match.params;
        this.props.getExamRequestById(examRequestId);
        this.props.getExamByExamRequestId(examRequestId);
    }

    render() {

        return (
            <div><ContentHeader title='Requerimento' small='Edição' />
                <Content>
                    <PatientForm readOnly='true' />
                    <ExamRequestForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitExamRequest}
                        showSystemInfo={true}
                        showTabs={true} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ getExamRequestById, submitExamRequest, getExamByExamRequestId }, dispatch)
export default connect(null, mapDispatchToProps)(PatientEdit)