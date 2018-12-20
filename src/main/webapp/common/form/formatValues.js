import { createNumberMask, createTextMask } from 'redux-form-input-masks'

export function onlyNumbers(value) {

    if (!!value) {
        return value.replace(/[^\d]/g, '');
    }

    return value
}

export const currencyMask = createNumberMask({
    prefix: 'R$ ',
    decimalPlaces: 2,
    locale: 'pt-BR',
});

export const phoneMask = createTextMask({
    pattern: '(999) 9999-9999',
});

export const cellPhoneMask = createTextMask({
    pattern: '(999) 99999-9999',
});

export const documentNumber = createTextMask({
    pattern: '999.999.999-99',
});