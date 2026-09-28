package br.com.ufu.ppgeb.eeg.model;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ClassUtils;

class AuditableEntityTest {

  private static final String MODEL_PACKAGE = "br.com.ufu.ppgeb.eeg.model";
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";
  private static final List<String> AUDITING_FIELDS = List.of(
      CREATED_AT_FIELD, CREATED_BY_FIELD, UPDATED_AT_FIELD, UPDATED_BY_FIELD);
  private static final Set<String> NON_AUDITED_ENTITIES = Set.of(
      IdempotencyRecord.class.getSimpleName(), ObjectType.class.getSimpleName(),
      Unit.class.getSimpleName());

  @Test
  @DisplayName("Given the auditable base class when inspecting it then be a mapped superclass "
      + "with the auditing listener and the four auditing fields")
  void givenAuditableBaseClass_whenInspectingIt_thenBeMappedSuperclassWithAuditingListener() {
    assertThat(AuditableEntity.class.getAnnotation(MappedSuperclass.class)).isNotNull();
    assertThat(AuditableEntity.class.getAnnotation(EntityListeners.class).value())
        .containsExactly(AuditingEntityListener.class);
    assertThat(declaredFieldNames(AuditableEntity.class))
        .containsExactlyInAnyOrderElementsOf(AUDITING_FIELDS);
  }

  @Test
  @DisplayName("Given every model entity when inspecting the superclass then only the non "
      + "audited entities do not extend the auditable base class")
  void givenEveryModelEntity_whenInspectingTheSuperclass_thenOnlyNonAuditedOnesDoNotExtendIt() {
    assertThat(nonAuditedEntityNames()).containsExactlyInAnyOrderElementsOf(
        NON_AUDITED_ENTITIES);
  }

  @Test
  @DisplayName("Given every audited entity when inspecting the declared fields then none "
      + "redeclares an auditing field")
  void givenEveryAuditedEntity_whenInspectingTheDeclaredFields_thenNoneRedeclaresAuditingField() {
    List<Class<?>> audited = entities().stream()
        .filter(entity -> AuditableEntity.class.equals(entity.getSuperclass()))
        .toList();

    assertThat(audited).isNotEmpty();
    assertThat(audited)
        .allSatisfy(entity -> assertThat(declaredFieldNames(entity))
            .doesNotContainAnyElementsOf(AUDITING_FIELDS));
  }

  private static Set<String> nonAuditedEntityNames() {
    return entities().stream()
        .filter(entity -> !AuditableEntity.class.equals(entity.getSuperclass()))
        .map(Class::getSimpleName)
        .collect(Collectors.toSet());
  }

  private static List<Class<?>> entities() {
    var scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
    return scanner.findCandidateComponents(MODEL_PACKAGE).stream()
        .map(BeanDefinition::getBeanClassName)
        .<Class<?>>map(className -> ClassUtils.resolveClassName(className,
            ClassUtils.getDefaultClassLoader()))
        .toList();
  }

  private static Set<String> declaredFieldNames(Class<?> type) {
    return stream(type.getDeclaredFields())
        .map(Field::getName)
        .collect(Collectors.toSet());
  }
}
