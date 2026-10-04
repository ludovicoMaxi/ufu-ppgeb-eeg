import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import { init, submitQuestionnaire } from './questionnaireActions'
import QuestionnaireForm from './questionnaireForm'

class QuestionnaireRegister extends Component {

    componentDidMount() {
        this.props.init();
    }

    render() {

        return (
            <div><ContentHeader title='Questionários' small='Cadastro' />
                <Content>
                    <QuestionnaireForm submitLabel='Adicionar'
                        submitClass='primary'
                        onSubmit={this.props.submitQuestionnaire} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ init, submitQuestionnaire }, dispatch)
export default connect(null, mapDispatchToProps)(QuestionnaireRegister)
