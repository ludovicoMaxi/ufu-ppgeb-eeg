package br.com.ufu.ppgeb.eeg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point.
 */
@SpringBootApplication
public class UfuPpgebEegApplication {

  /**
   * Main method to start the application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(UfuPpgebEegApplication.class, args);
  }
}
