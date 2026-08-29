package br.com.ufu.ppgeb.eeg.constant;

import lombok.experimental.UtilityClass;

/**
 * Centralized constants for the application URLs.
 */
@UtilityClass
public class ApiPaths {

  private static final String API = "/api";
  public static final String PATH_SEPARATOR = "/";

  public static final String HOME = PATH_SEPARATOR;
  public static final String CUSTOMERS = "/customers/*";
  public static final String LOGIN = "/login";
  public static final String LOGOUT = "/logout";
  public static final String LOGIN_LOGOUT = LOGIN + "?logout";
  public static final String ANY = "/**";
  public static final String API_ROOT = API + ANY;

  public static final String ACTIVITY = API + "/activity";
  public static final String CONTACT = API + "/contact";
  public static final String EPOCH = API + "/epoch";
  public static final String EXAM = API + "/exam";
  public static final String EXAM_REQUEST = API + "/exam-request";
  public static final String PATIENT = API + "/patient";
  public static final String UNIT = API + "/unit";

  public static final String MEDICAMENT_SUBPATH = "/medicament";
  public static final String MEDICAMENT = API + MEDICAMENT_SUBPATH;

  public static final String EQUIPMENT_SUBPATH = "/equipment";
  public static final String EQUIPMENT = API + EQUIPMENT_SUBPATH;
}
