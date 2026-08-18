import React, { useEffect } from 'react'
import { bindActionCreators } from 'redux'
import { useDispatch } from 'react-redux'
import { Route, Routes, Navigate } from 'react-router-dom';

import ContentHeader from './common/template/contentHeader'
import Content from './common/template/content'
import Dashboard from './dashboard/dashboard'
import PatientForm from './patient/patientForm'
import PatientSearch from './patient/patientSearch'
import PatientEdit from './patient/patientEdit'
import ExamRequestEdit from './exam-request/examRequestEdit'
import ExamRequestSearch from './exam-request/examRequestSearch'
import ExamRequestRegister from './exam-request/examRequestRegister'
import ExamEdit from './exam/examEdit'
import ExamSearch from './exam/examSearch'
import ExamRegister from './exam/examRegister'
import { submitPatient, initRegisterPatient } from './patient/patientActions'

const Page = ({ title, small, children }) => (
    <div>
        <ContentHeader title={title} small={small} />
        <Content>{children}</Content>
    </div>
)

function PatientAddPage() {
    const dispatch = useDispatch()
    const actions = bindActionCreators({ submitPatient, initRegisterPatient }, dispatch)
    useEffect(() => {
        actions.initRegisterPatient()
    }, [])
    return (
        <Page title='Pacientes' small='Cadastro'>
            <PatientForm submitLabel='Adicionar'
                submitClass='primary' onSubmit={actions.submitPatient} />
        </Page>
    )
}

const AppRoutes = () => (
    <Routes>
            <Route path='/' element={<Dashboard />} />
            <Route path='/patient/add' element={<PatientAddPage />} />
            <Route path='/patient/search' element={
                <Page title='Pacientes' small='Busca'><PatientSearch /></Page>
            } />
            <Route path='/patient/:patientId/exam-request/add' element={<ExamRequestRegister />} />
            <Route path='/exam-request/search' element={
                <Page title='Requerimentos' small='Busca'><ExamRequestSearch /></Page>
            } />
            <Route path='/exam-request/:examRequestId' element={<ExamRequestEdit />} />
            <Route path='/patient/:patientId/exam/add' element={<ExamRegister />} />
            <Route path='/exam/search' element={
                <Page title='Exames' small='Busca'><ExamSearch /></Page>
            } />
            <Route path='/exam/:examId' element={<ExamEdit />} />
            <Route path='/patient/:patientId' element={<PatientEdit />} />
            <Route path='*' element={<Navigate to='/' replace />} />
    </Routes>
)

export default AppRoutes
