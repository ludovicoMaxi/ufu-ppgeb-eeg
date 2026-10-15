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

export const dateMask = value => {
    if (!value) {
        return value;
    }
    const digits = value.replace(/\D/g, '').slice(0, 8);
    if (digits.length <= 2) {
        return digits;
    }
    if (digits.length <= 4) {
        return `${digits.slice(0, 2)}/${digits.slice(2)}`;
    }
    return `${digits.slice(0, 2)}/${digits.slice(2, 4)}/${digits.slice(4)}`;
}

export const dateTimeMask = value => {
    if (!value) {
        return value;
    }
    const digits = value.replace(/\D/g, '').slice(0, 14);
    const date = digits.slice(0, 8);
    const time = digits.slice(8);
    if (date.length <= 2) {
        return date;
    }
    let masked = `${date.slice(0, 2)}/${date.slice(2, 4)}`;
    if (date.length <= 4) {
        return masked;
    }
    masked = `${masked}/${date.slice(4, 8)}`;
    if (time.length === 0) {
        return masked;
    }
    masked = `${masked} ${time.slice(0, 2)}`;
    if (time.length <= 2) {
        return masked;
    }
    masked = `${masked}:${time.slice(2, 4)}`;
    if (time.length <= 4) {
        return masked;
    }
    return `${masked}:${time.slice(4, 6)}`;
}