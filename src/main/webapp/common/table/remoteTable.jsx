import React, { Component } from 'react'
import axios from 'axios'
import { TabulatorFull as Tabulator } from 'tabulator-tables'
import { toastr } from 'react-redux-toastr'

import { errorMessage } from '../msg/errorMessage'

const DEFAULT_PAGE_SIZE = 10;
const PAGE_SIZE_OPTIONS = [10, 25, 50];

// Tabulator pages are 1-based; Spring Data pages are 0-based.
const toSpringPage = page => (page || 1) - 1;

// Drops unset filters so the backend does not receive empty values for typed parameters.
const definedFilters = filters => Object.fromEntries(
    Object.entries(filters || {})
        .filter(([, value]) => value !== '' && value !== null && value !== undefined)
);

/**
 * Table bound to a paginated Spring endpoint (Page&lt;T&gt;).
 * Pagination and sorting run on the server; filters come from the `filters` prop.
 */
export default class RemoteTable extends Component {

    componentDidMount() {
        this.table = new Tabulator(this.container, {
            layout: 'fitColumns',
            columns: this.props.columns,
            placeholder: this.props.placeholder || 'Nenhum registro encontrado',
            ajaxURL: this.props.url,
            ajaxRequestFunc: (url, config, params) => this.request(url, params),
            pagination: true,
            paginationMode: 'remote',
            paginationSize: DEFAULT_PAGE_SIZE,
            paginationSizeSelector: PAGE_SIZE_OPTIONS,
            dataReceiveParams: { last_page: 'totalPages', data: 'content' },
            sortMode: 'remote'
        });
    }

    componentDidUpdate(prevProps) {
        if (prevProps.filters !== this.props.filters && this.table) {
            this.reloadFromFirstPage();
        }
    }

    componentWillUnmount() {
        if (this.table) {
            this.table.destroy();
            this.table = null;
        }
    }

    // range 'active' = linhas da pagina atual (o backend nao e consultado de novo).
    downloadCurrentPage() {
        if (this.table) {
            this.table.download('csv', `${this.props.exportName || 'tabela'}.csv`, { range: 'active', delimiter: ';' });
        }
    }

    reloadFromFirstPage() {
        if (this.table.getPage() !== 1) {
            this.table.setPage(1);
        } else {
            this.table.setData();
        }
    }

    request(url, params) {
        const sort = (params.sort || []).map(item => `${item.field},${item.dir}`);
        const query = {
            ...definedFilters(this.props.filters),
            page: toSpringPage(params.page),
            size: params.size || DEFAULT_PAGE_SIZE,
            sort
        };

        // Spring reads repeated "sort" keys (sort=field,dir); axios would send "sort[]" by default.
        return axios.get(url, { params: query, paramsSerializer: { indexes: null } })
            .then(resp => resp.data)
            .catch(e => {
                toastr.error('Erro', `Ocorreu um erro ao consultar: \n` + errorMessage(e));
                return Promise.reject(e);
            });
    }

    render() {
        return (
            <div>
                <div className='d-flex justify-content-end mb-2'>
                    <button type='button' className='btn btn-outline-secondary btn-sm'
                        onClick={() => this.downloadCurrentPage()}>
                        <i className='fa fa-file-csv'></i> Exportar página (CSV)
                    </button>
                </div>
                <div ref={element => { this.container = element }} />
            </div>
        );
    }
}
