import React from 'react'
import MenuItem from './menuItem'
import MenuTree from './menuTree'

export default props => (
    <ul className="sidebar-menu tree" data-widget="tree">
        <li className="header">MAIN NAVIGATION</li>
        <MenuItem path='#/' label='Dashboard' icon='dashboard' />
        <MenuTree label='Pacientes' icon='users'>
            <MenuItem path='#/patient/add'
                label='Cadastro' icon='user-plus' />
            <MenuItem path='#/patient/search'
                label='Busca' icon='search' />
        </MenuTree>
    </ul>
)