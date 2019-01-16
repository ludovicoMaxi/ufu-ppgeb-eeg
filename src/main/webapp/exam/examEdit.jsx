import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getExamById, submitExam } from './examActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import PatientForm from '../patient/patientForm'
import ExamForm from './examForm'

class PatientEdit extends Component {

    componentWillMount() {
        const { examId } = this.props.match.params;
        this.props.init(examId);
        this.props.getExamById(examId);
    }

    render() {

        return (
            <div><ContentHeader title='Exame' small='Edição' />
                <Content>
                    <PatientForm readOnly='true' />
                    <ExamForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitExam}
                        showSystemInfo={true}
                        examId={this.props.match.params.examId} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ init, getExamById, submitExam }, dispatch)
export default connect(null, mapDispatchToProps)(PatientEdit)