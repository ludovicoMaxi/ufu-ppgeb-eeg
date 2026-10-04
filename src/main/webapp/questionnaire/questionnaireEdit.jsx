import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getQuestionnaireById, submitQuestionnaire, removeQuestionnaire } from './questionnaireActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import QuestionnaireForm from './questionnaireForm'
import withRouter from '../common/router/withRouter'

class QuestionnaireEdit extends Component {

    componentDidMount() {
        this.props.init();
        const { questionnaireId } = this.props.match.params;
        this.props.getQuestionnaireById(questionnaireId);
    }

    render() {

        return (
            <div><ContentHeader title='Questionários' small='Edição' />
                <Content>
                    <QuestionnaireForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitQuestionnaire}
                        showSystemInfo={true}
                        showRemove={true}
                        onRemove={this.props.removeQuestionnaire} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators(
    { init, getQuestionnaireById, submitQuestionnaire, removeQuestionnaire }, dispatch)
export default withRouter(connect(null, mapDispatchToProps)(QuestionnaireEdit))
