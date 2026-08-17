package br.com.ufu.ppgeb.eeg.utils;

import java.util.Date;
import java.util.Objects;

import static java.util.Objects.isNull;


public final class CompareDate {

    private CompareDate() {

    }


    static public boolean compareDates(Date a, Date b) {

        if ( isNull( a ) || isNull( b ) ) {
            return a == b;
        }

        return Objects.equals(a, b);
    }
}

