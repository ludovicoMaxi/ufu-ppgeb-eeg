import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import { submitExamRequest, setPatientInForm, init } from './examRequestActions'
import { init as initPatient, getPatientById } from '../patient/patientActions'
import PatientForm from '../patient/patientForm'
import ExamRequestForm from './examRequestForm'

class ExamRequestRegister extends Component {

    componentWillMount() {
        this.props.init();
        this.props.initPatient();
        const { patientId } = this.props.match.params;
        this.props.getPatientById(patientId);

        const patient = {'id': patientId};
        this.props.setPatientInForm(patient);
    }

    render() {

        return (
            <div><ContentHeader title='Requerimento' small='Cadastro' />
                <Content>
                    <PatientForm readOnly='true' />
                    <ExamRequestForm submitLabel='Adicionar'
                        submitClass='primary' onSubmit={this.props.submitExamRequest} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ initPatient, getPatientById, submitExamRequest, setPatientInForm, init }, dispatch)
export default connect(null, mapDispatchToProps)(ExamRequestRegister)