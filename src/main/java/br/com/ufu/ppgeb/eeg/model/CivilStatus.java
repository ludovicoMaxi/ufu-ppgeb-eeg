package br.com.ufu.ppgeb.eeg.model;

/**
 * Represents a patient's marital status.
 */
public enum CivilStatus {

  SINGLE("Solteiro",
      "Solteiro oferece liberdade e independência, mas pode enfrentar pressão social e estigma, "
          + "com perguntas constantes sobre o status de relacionamento."),
  MARRIED("Casado",
      "Casado é um marco que envolve compromisso emocional e implicações legais e financeiras, "
          + "com direitos como decisões médicas pelo parceiro e desafios como compromissos compartilhados."),
  DIVORCED("Divorciado",
      "Divorciado passa por uma transição emocional difícil, com questões legais como divisão de bens e "
          + "custódia, mas muitos encontram liberdade e autodescoberta após o divórcio."),
  WIDOWED("Viúvo",
      "Viúvo vivencia luto e solidão, sendo essencial oferecer suporte emocional e prático, além de "
          + "buscar grupos de apoio para lidar com a perda."),
  COMMON_LAW_MARRIAGE("União Estável",
      "União estável é uma convivência duradoura e pública com objetivo de constituir família, "
          + "conferindo direitos e deveres semelhantes ao casamento, como compartilhamento de bens.");

  private final String name;

  private final String description;

  CivilStatus(String name, String description) {
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
