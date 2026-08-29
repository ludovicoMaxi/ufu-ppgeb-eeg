package br.com.ufu.ppgeb.eeg.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

class HomeControllerTest {

  private static final String INDEX_VIEW = "index";
  private static final String LOGIN_VIEW = "login";
  private static final String CACHE_VERSION_ATTRIBUTE = "cacheVersion";

  private final HomeController homeController = new HomeController();

  @Test
  @DisplayName("Given home request when rendering index then return index view with cache version")
  void givenHomeRequest_whenRenderingIndex_thenReturnIndexView() {
    Model model = new ExtendedModelMap();

    String view = homeController.index(model);

    assertThat(view).isEqualTo(INDEX_VIEW);
    assertThat(model.asMap()).containsKey(CACHE_VERSION_ATTRIBUTE);
  }

  @Test
  @DisplayName("Given login request when rendering login then return login view")
  void givenLoginRequest_whenRenderingLogin_thenReturnLoginView() {

    String view = homeController.login();

    assertThat(view).isEqualTo(LOGIN_VIEW);
  }
}
