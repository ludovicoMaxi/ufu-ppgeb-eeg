package br.com.ufu.ppgeb.eeg.utils;

import static java.util.Objects.isNull;

import java.util.Date;
import java.util.Objects;

/**
 * Utility class for comparing dates.
 */
public final class CompareDate {

  private CompareDate() {

  }

  /**
   * Compares two dates for equality.
   *
   * @param a the first date
   * @param b the second date
   * @return true if both dates are equal
   */
  public static boolean compareDates(Date a, Date b) {

    if (isNull(a) || isNull(b)) {
      return a == b;
    }

    return Objects.equals(a, b);
  }
}
