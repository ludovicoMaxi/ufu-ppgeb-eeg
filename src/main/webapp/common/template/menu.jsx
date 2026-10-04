import React from 'react'
import MenuItem from './menuItem'
import MenuTree from './menuTree'

export default props => (
    <ul className="nav sidebar-menu flex-column" data-lte-toggle="treeview" role="menu" data-accordion="false">
        <li className="nav-header">MAIN NAVIGATION</li>
        <MenuItem path='/' label='Dashboard' icon='tachometer-alt' />
        <MenuTree label='Pacientes' icon='users'>
            <MenuItem path='/patient/add'
                label='Cadastro' icon='user-plus' />
            <MenuItem path='/patient/search'
                label='Busca' icon='search' />
        </MenuTree>
        <MenuTree label='Requerimentos' icon='box'>
            <MenuItem path='/exam-request/search'
                label='Busca' icon='search' />
        </MenuTree>
        <MenuTree label='Exames' icon='notes-medical'>
            <MenuItem path='/exam/search'
                label='Busca' icon='search' />
        </MenuTree>
        <MenuTree label='Questionários' icon='clipboard-list'>
            <MenuItem path='/questionnaire/add'
                label='Cadastro' icon='plus' />
            <MenuItem path='/questionnaire/search'
                label='Busca' icon='search' />
        </MenuTree>
        <MenuTree label='Questões' icon='question'>
            <MenuItem path='/question/add'
                label='Cadastro' icon='plus' />
            <MenuItem path='/question/search'
                label='Busca' icon='search' />
        </MenuTree>
        <MenuTree label='Alternativas' icon='list-ul'>
            <MenuItem path='/answer-option/add'
                label='Cadastro' icon='plus' />
            <MenuItem path='/answer-option/search'
                label='Busca' icon='search' />
        </MenuTree>
    </ul>
)
