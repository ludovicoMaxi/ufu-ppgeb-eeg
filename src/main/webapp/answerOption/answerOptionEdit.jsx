import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { init, getAnswerOptionById, submitAnswerOption, removeAnswerOption } from './answerOptionActions'
import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import AnswerOptionForm from './answerOptionForm'
import withRouter from '../common/router/withRouter'

class AnswerOptionEdit extends Component {

    componentDidMount() {
        this.props.init();
        const { answerOptionId } = this.props.match.params;
        this.props.getAnswerOptionById(answerOptionId);
    }

    render() {

        return (
            <div><ContentHeader title='Alternativas' small='Edição' />
                <Content>
                    <AnswerOptionForm submitLabel='Alterar'
                        submitClass='primary'
                        onSubmit={this.props.submitAnswerOption}
                        showSystemInfo={true}
                        showRemove={true}
                        onRemove={this.props.removeAnswerOption} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators(
    { init, getAnswerOptionById, submitAnswerOption, removeAnswerOption }, dispatch)
export default withRouter(connect(null, mapDispatchToProps)(AnswerOptionEdit))
