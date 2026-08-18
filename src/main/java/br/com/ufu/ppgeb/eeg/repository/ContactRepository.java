package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Contact entities.
 */
public interface ContactRepository
    extends JpaRepository<Contact, Long>, ContactRepositoryCustom {
}
