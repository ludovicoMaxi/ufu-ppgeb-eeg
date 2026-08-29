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

  public static final String EXAM_ID_PATTERN = "{examId}";

  public static final String ACTIVITIES_SUBPATH = "/activities";
  public static final String EPOCHS_SUBPATH = "/epochs";
  public static final String MEDICAMENTS_SUBPATH = "/medicaments";
  public static final String EQUIPMENTS_SUBPATH = "/equipments";

  public static final String EXAM = API + "/exams";
  public static final String EXAM_REQUEST = API + "/exam-requests";
  public static final String PATIENT = API + "/patients";
  public static final String CONTACT = API + "/contacts";
  public static final String UNIT = API + "/units";
  public static final String MEDICAMENT = API + MEDICAMENTS_SUBPATH;
  public static final String EQUIPMENT = API + EQUIPMENTS_SUBPATH;

  public static final String EXAM_ACTIVITIES = EXAM + PATH_SEPARATOR + EXAM_ID_PATTERN + ACTIVITIES_SUBPATH;
  public static final String EXAM_EPOCHS = EXAM + PATH_SEPARATOR + EXAM_ID_PATTERN + EPOCHS_SUBPATH;
}
