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

  private final String cacheVersion = Long.toString(System.currentTimeMillis());

  /**
   * Renders the index page.
   *
   * @param model the model
   * @return the view name
   */
  @GetMapping(value = { ApiPaths.HOME, ApiPaths.CUSTOMERS })
  public String index(Model model) {

    model.addAttribute("cacheVersion", cacheVersion);
    return "index";
  }

  /**
   * Renders the login page.
   *
   * @return the view name
   */
  @GetMapping(ApiPaths.LOGIN)
  public String login() {

    return "login";
  }
}
