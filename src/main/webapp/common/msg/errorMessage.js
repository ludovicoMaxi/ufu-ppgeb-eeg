export function errorMessage(e) {

    const data = !!e && !!e.response ? e.response.data : null;

    if (!!data && !!data.detail) {
        return data.detail;
    }

    if (!!data && !!data.message) {
        return data.message;
    }

    return 'Tente novamente mais tarde.';
}
