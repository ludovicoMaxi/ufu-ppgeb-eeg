import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getPatientById, submitPatient, getExamByPatientId, getExamRequestByPatientId } from './patientActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import PatientForm from './patientForm'
import withRouter from '../common/router/withRouter'

class PatientEdit extends Component {

    componentWillMount() {
        this.props.init();
        const { patientId } = this.props.match.params;
        this.props.getPatientById(patientId);
        this.props.getExamByPatientId(patientId);
        this.props.getExamRequestByPatientId(patientId);
    }

    render() {

        return (
            <div><ContentHeader title='Pacientes' small='Edição' />
                <Content>
                    <PatientForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitPatient}
                        showSystemInfo={true}
                        showTabs={true} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators(
    {
        init,
        getPatientById,
        submitPatient,
        getExamByPatientId,
        getExamRequestByPatientId
    }, dispatch)
export default withRouter(connect(null, mapDispatchToProps)(PatientEdit))