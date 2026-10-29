// Exporta as linhas recebidas (ex.: a pagina atual de uma busca) como CSV.
// columns: [{ label: 'Nome', value: row => row.name }]
// Separador ';' e BOM UTF-8 para abrir corretamente no Excel em pt-BR.

const SEPARATOR = ';';

const escapeCell = value => {
    if (value === null || value === undefined) {
        return '';
    }
    const text = String(value);
    return /[;"\r\n]/.test(text) ? `"${text.replace(/"/g, '""')}"` : text;
};

export default function exportCsv(filename, columns, rows) {
    const header = columns.map(column => escapeCell(column.label)).join(SEPARATOR);
    const lines = (rows || []).map(row =>
        columns.map(column => escapeCell(column.value(row))).join(SEPARATOR)
    );
    const content = '\uFEFF' + [header, ...lines].join('\r\n');

    const url = URL.createObjectURL(new Blob([content], { type: 'text/csv;charset=utf-8' }));
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    link.remove();
    URL.revokeObjectURL(url);
}
