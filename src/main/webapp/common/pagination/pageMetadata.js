export function pageMetadata(page) {
    return {
        number: page.number,
        totalPages: page.totalPages,
        totalElements: page.totalElements,
        size: page.size
    }
}