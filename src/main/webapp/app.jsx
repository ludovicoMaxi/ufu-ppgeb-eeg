import './common/template/dependencies'
import React from 'react'
import { HashRouter } from 'react-router-dom'

import Header from './common/template/header'
import Sidebar from './common/template/sidebar'
import Footer from './common/template/footer'
import Routes from './routes'
import Messages from './common/msg/messages'

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props)
        this.state = { hasError: false, error: null }
    }

    static getDerivedStateFromError(error) {
        return { hasError: true, error: error && error.message ? error.message : String(error) }
    }

    componentDidCatch(error, info) {
        console.error('Erro de renderização do React:', error, info)
    }

    render() {
        if (this.state.hasError) {
            return (
                <div style={{ padding: '2rem', fontFamily: 'sans-serif', color: '#333' }}>
                    <h3>Erro ao renderizar a aplicação</h3>
                    <pre style={{ whiteSpace: 'pre-wrap', background: '#f5f5f5', padding: '1rem' }}>
                        {this.state.error}
                    </pre>
                    <button
                        className='btn btn-primary'
                        onClick={() => this.setState({ hasError: false, error: null })}>
                        Tentar novamente
                    </button>
                </div>
            )
        }
        return this.props.children
    }
}

export default props => (
    <ErrorBoundary>
        <HashRouter>
            <div className='app-wrapper'>
                <Header />
                <Sidebar />
                <main className='app-main'>
                    <Routes />
                </main>
                <Footer />
                <Messages />
            </div>
        </HashRouter>
    </ErrorBoundary>
)
