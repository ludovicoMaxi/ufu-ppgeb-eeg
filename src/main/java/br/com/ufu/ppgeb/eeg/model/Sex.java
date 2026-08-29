package br.com.ufu.ppgeb.eeg.model;

/**
 * Represents a patient's biological sex.
 */
public enum Sex {

  MALE("Masculino",
      "Sexo biológico caracterizado pelos cromossomos XY, órgãos reprodutores masculinos e "
          + "produção de testosterona."),
  FEMALE("Feminino",
      "Sexo biológico caracterizado pelos cromossomos XX, órgãos reprodutores femininos e "
          + "desenvolvimento de características sexuais femininas.");

  private final String name;

  private final String description;

  Sex(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
