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

export default function Pagination({ page, totalPages, totalElements, onPageChange }) {
    if (totalElements == null) {
        return null
    }
    return (
        <div className='row align-items-center mt-2'>
            <div className='col-md-6'>
                <span className='text-secondary'>Total de {totalElements} registro(s)</span>
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