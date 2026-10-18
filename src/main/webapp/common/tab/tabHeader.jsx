import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import If from '../operator/if'
import { selectTab } from './tabActions'

class TabHeader extends Component {
    render() {
        const selected = this.props.tab.selected === this.props.target
        const visible = this.props.tab.visible[this.props.target]
        return (
            <If test={visible}>
                <li className='nav-item' role='presentation'>
                    <button type='button'
                        className={`nav-link ${selected ? 'active' : ''}`}
                        onClick={() => this.props.selectTab(this.props.target)}
                        data-target={this.props.target}
                        role='tab'
                        aria-selected={selected}>
                        <i className={`fa fa-${this.props.icon}`}></i> {this.props.label}
                    </button>
                </li>
            </If>
        )
    }
}

const mapStateToPorps = state => ({ tab: state.tab })
const mapDispatchToProps = dispatch => bindActionCreators({ selectTab }, dispatch)
export default connect(mapStateToPorps, mapDispatchToProps)(TabHeader)