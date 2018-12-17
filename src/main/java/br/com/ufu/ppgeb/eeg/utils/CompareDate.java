package br.com.ufu.ppgeb.eeg.utils;

import java.util.Date;


public class CompareDate {

    static public boolean compareDates(Date a, Date b) {

        if (a == null && b == null) {
            return true;
        } else if (a != null) {
            return a.compareTo(b) == 0;
        }

        return false;
    }
}

