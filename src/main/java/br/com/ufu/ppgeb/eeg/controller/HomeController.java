package br.com.ufu.ppgeb.eeg.controller;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for home and login pages.
 */
@Controller
public class HomeController {

  private static final String CACHE_VERSION_ATTRIBUTE = "cacheVersion";

  private final String cacheVersion = Long.toString(System.currentTimeMillis());

  /**
   * Renders the index page.
   *
   * @param model the model
   * @return the view name
   */
  @GetMapping(value = { ApiPaths.HOME, ApiPaths.CUSTOMERS })
  public String index(Model model) {

    model.addAttribute(CACHE_VERSION_ATTRIBUTE, cacheVersion);
    return "index";
  }

  /**
   * Renders the login page.
   *
   * @param model the model
   * @return the view name
   */
  @GetMapping(ApiPaths.LOGIN)
  public String login(Model model) {

    model.addAttribute(CACHE_VERSION_ATTRIBUTE, cacheVersion);
    return "login";
  }
}
