import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getQuestionById, submitQuestion, removeQuestion } from './questionActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import QuestionForm from './questionForm'
import withRouter from '../common/router/withRouter'

class QuestionEdit extends Component {

    componentDidMount() {
        this.props.init();
        const { questionId } = this.props.match.params;
        this.props.getQuestionById(questionId);
    }

    render() {

        return (
            <div><ContentHeader title='Questões' small='Edição' />
                <Content>
                    <QuestionForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitQuestion}
                        showSystemInfo={true}
                        showRemove={true}
                        onRemove={this.props.removeQuestion} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators(
    { init, getQuestionById, submitQuestion, removeQuestion }, dispatch)
export default withRouter(connect(null, mapDispatchToProps)(QuestionEdit))
