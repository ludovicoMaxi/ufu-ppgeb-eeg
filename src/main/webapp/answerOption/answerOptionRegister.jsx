import React, { Component } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import ContentHeader from '../common/template/contentHeader'
import Content from '../common/template/content'
import { init, submitAnswerOption } from './answerOptionActions'
import AnswerOptionForm from './answerOptionForm'

class AnswerOptionRegister extends Component {

    componentDidMount() {
        this.props.init();
    }

    render() {

        return (
            <div><ContentHeader title='Alternativas' small='Cadastro' />
                <Content>
                    <AnswerOptionForm submitLabel='Adicionar'
                        submitClass='primary'
                        onSubmit={this.props.submitAnswerOption} />
                </Content>
            </div>
        )
    }
}

const mapDispatchToProps = dispatch => bindActionCreators({ init, submitAnswerOption }, dispatch)
export default connect(null, mapDispatchToProps)(AnswerOptionRegister)
