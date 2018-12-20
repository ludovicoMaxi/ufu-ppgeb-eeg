import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getPatientById, submitPatient, showUpdate } from './patientActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import PatientForm from './patientForm'

class PatientEdit extends Component {

    componentWillMount() {
        this.props.init();
        const { patientId } = this.props.match.params;
        this.props.getPatientById(patientId);
    }

    render() {

        return (
            <div><ContentHeader title='Pacientes' small='Edição' />
                <Content>
                    <PatientForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitPatient}
                        showSystemInfo={true} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ init, getPatientById, submitPatient, showUpdate }, dispatch)
export default connect(null, mapDispatchToProps)(PatientEdit)