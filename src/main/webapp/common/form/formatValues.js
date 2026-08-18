export const upper = value => value && value.toUpperCase();

export function onlyNumbers(value) {

    if (!!value) {
        return value.replace(/[^\d]/g, '');
    }

    return value
}

export const documentNumber = value => {
    if (!value) {
        return value;
    }
    const digits = value.replace(/\D/g, '').slice(0, 11);
    if (digits.length <= 3) {
        return digits;
    }
    if (digits.length <= 6) {
        return `${digits.slice(0, 3)}.${digits.slice(3)}`;
    }
    if (digits.length <= 9) {
        return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`;
    }
    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`;
}