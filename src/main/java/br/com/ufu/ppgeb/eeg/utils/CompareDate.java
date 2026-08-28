package br.com.ufu.ppgeb.eeg.utils;

import static java.util.Objects.isNull;

import java.util.Objects;

/**
 * Utility class for comparing dates.
 */
public final class CompareDate {

  private CompareDate() {

  }

  /**
   * Compares two values for equality.
   *
   * @param a the first value
   * @param b the second value
   * @return true if both values are equal
   */
  public static boolean compareDates(Object a, Object b) {

    if (isNull(a) || isNull(b)) {
      return a == b;
    }

    return Objects.equals(a, b);
  }
}
