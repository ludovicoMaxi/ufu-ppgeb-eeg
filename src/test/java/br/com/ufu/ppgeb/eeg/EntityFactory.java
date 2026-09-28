package br.com.ufu.ppgeb.eeg;

import static org.instancio.Select.field;

import br.com.ufu.ppgeb.eeg.model.AuditableEntity;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

/**
 * Fabrica de entidades para teste que ja deixa o id e os campos de auditoria para a
 * camada de persistencia preencher.
 *
 * <p>O {@link org.instancio.settings.Settings} do Instancio so aceita chaves tipadas
 * (tamanhos, nullability, formato), entao nao permite declarar ignores globais. A unica
 * forma de nao repetir o bloco de auditoria em cada factory e encapsular o Instancio
 * neste helper.
 *
 * <p>Ha duas entradas de proposito: {@link #audited(Class)} ignora tambem os campos de
 * {@link AuditableEntity}, enquanto {@link #unaudited(Class)} nao os ignora. Uma unica
 * entrada padrao que ignorasse a auditoria quebraria com {@code UnusedSelector} nas
 * entidades que nao sao auditadas, como {@code Unit} e {@code IdempotencyRecord}.
 */
public final class EntityFactory {

  private static final String ID_FIELD = "id";
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  private EntityFactory() {
  }

  /**
   * Instancia de entidade auditada, com id e campos de auditoria ignorados para que a
   * persistencia preencha a auditoria.
   */
  public static <T> InstancioApi<T> audited(Class<T> type) {
    return Instancio.of(type)
        .ignore(field(ID_FIELD))
        .ignore(field(AuditableEntity.class, CREATED_AT_FIELD))
        .ignore(field(AuditableEntity.class, CREATED_BY_FIELD))
        .ignore(field(AuditableEntity.class, UPDATED_AT_FIELD))
        .ignore(field(AuditableEntity.class, UPDATED_BY_FIELD));
  }

  /**
   * Instancia de entidade sem auditoria, com apenas o id ignorado.
   */
  public static <T> InstancioApi<T> unaudited(Class<T> type) {
    return Instancio.of(type)
        .ignore(field(ID_FIELD));
  }
}
