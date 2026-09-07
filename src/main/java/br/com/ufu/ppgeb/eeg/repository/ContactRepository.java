package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for Contact entities.
 */
public interface ContactRepository
    extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
}