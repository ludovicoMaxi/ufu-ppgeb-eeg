import React from 'react'

const ELLIPSIS = 'ellipsis'

function range(start, end) {
    const result = []
    for (let i = start; i < end; i++) {
        result.push(i)
    }
    return result
}

function getPageItems(page, totalPages) {
    if (totalPages <= 7) {
        return range(0, totalPages)
    }
    const candidates = [0, totalPages - 1, page - 1, page, page + 1]
        .filter(item => item >= 0 && item < totalPages)
    const items = []
    let previous = -1
    Array.from(new Set(candidates)).sort((a, b) => a - b).forEach(current => {
        if (previous >= 0 && current - previous > 1) {
            items.push(ELLIPSIS)
        }
        items.push(current)
        previous = current
    })
    return items
}

export const PAGE_SIZE_OPTIONS = [10, 25, 50]

export default function Pagination({ page, totalPages, totalElements, onPageChange, pageSize, onPageSizeChange, onExport }) {
    if (totalElements == null) {
        return null
    }
    return (
        <div className='row align-items-center mt-2'>
            <div className='col-md-6'>
                <div className='d-flex flex-wrap align-items-center gap-2'>
                    <span className='text-secondary'>Total de {totalElements} registro(s)</span>
                    {!!onPageSizeChange && (
                        <label className='d-flex align-items-center gap-2 mb-0 text-secondary'>
                            Por página
                            <select className='form-select form-select-sm w-auto'
                                value={pageSize}
                                onChange={event => onPageSizeChange(Number(event.target.value))}>
                                {PAGE_SIZE_OPTIONS.map(size => (
                                    <option key={size} value={size}>{size}</option>
                                ))}
                            </select>
                        </label>
                    )}
                    {!!onExport && (
                        <button type='button' className='btn btn-outline-secondary btn-sm'
                            onClick={onExport}>
                            <i className='fa fa-file-csv'></i> Exportar página (CSV)
                        </button>
                    )}
                </div>
            </div>
            {totalPages > 1 && (
                <div className='col-md-6'>
                    <nav aria-label='Paginação'>
                        <ul className='pagination pagination-sm justify-content-md-end mb-0'>
                            <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
                                <button type='button' className='page-link'
                                    onClick={() => onPageChange(page - 1)}>
                                    Anterior
                                </button>
                            </li>
                            {getPageItems(page, totalPages).map((item, index) => {
                                if (item === ELLIPSIS) {
                                    return (
                                        <li key={`ellipsis-${index}`} className='page-item disabled'>
                                            <span className='page-link'>…</span>
                                        </li>
                                    )
                                }
                                return (
                                    <li key={item} className={`page-item ${item === page ? 'active' : ''}`}>
                                        <button type='button' className='page-link'
                                            onClick={() => onPageChange(item)}>
                                            {item + 1}
                                        </button>
                                    </li>
                                )
                            })}
                            <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
                                <button type='button' className='page-link'
                                    onClick={() => onPageChange(page + 1)}>
                                    Próxima
                                </button>
                            </li>
                        </ul>
                    </nav>
                </div>
            )}
        </div>
    )
}