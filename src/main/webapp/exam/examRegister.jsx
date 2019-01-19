import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import * as qs from 'query-string'

import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import { init, submitExam, setPatientInForm } from './examActions'
import { init as initPatient, getPatientById } from '../patient/patientActions'
import PatientForm from '../patient/patientForm'
import ExamForm from './examForm'

class ExamRegister extends Component {

    componentWillMount() {
        const queryParams = qs.parse(this.props.location.search);
        this.props.init(queryParams.examRequestId);
        this.props.initPatient();
        const { patientId } = this.props.match.params;
        this.props.getPatientById(patientId);

        const patient = { 'id': patientId };
        this.props.setPatientInForm(patient);
    }

    render() {

        return (
            <div><ContentHeader title='Exame' small='Cadastro' />
                <Content>
                    <PatientForm readOnly='true' />
                    <ExamForm submitLabel='Adicionar'
                        submitClass='primary' onSubmit={this.props.submitExam} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ init, initPatient, getPatientById, submitExam, setPatientInForm }, dispatch)
export default connect(null, mapDispatchToProps)(ExamRegister)