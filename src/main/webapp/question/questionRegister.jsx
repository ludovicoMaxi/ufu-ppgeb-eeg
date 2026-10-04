import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import { init, submitQuestion } from './questionActions'
import QuestionForm from './questionForm'

class QuestionRegister extends Component {

    componentDidMount() {
        this.props.init();
    }

    render() {

        return (
            <div><ContentHeader title='Questões' small='Cadastro' />
                <Content>
                    <QuestionForm submitLabel='Adicionar'
                        submitClass='primary'
                        onSubmit={this.props.submitQuestion} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ init, submitQuestion }, dispatch)
export default connect(null, mapDispatchToProps)(QuestionRegister)
