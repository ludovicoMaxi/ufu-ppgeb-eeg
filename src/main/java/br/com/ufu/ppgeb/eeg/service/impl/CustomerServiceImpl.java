package br.com.ufu.ppgeb.eeg.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ufu.ppgeb.eeg.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.model.Customer;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import br.com.ufu.ppgeb.eeg.service.ContactService;
import br.com.ufu.ppgeb.eeg.service.CustomerService;
import br.com.ufu.ppgeb.eeg.view.CustomerView;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContactService contactService;


    public Customer save( CustomerView customerView ) {

        Assert.notNull( customerView, "customerView cannot be null." );

        Customer customer = customerView.getCustomer();
        validateCustomer( customer );

        customer.setCreatedAt( new Date() );
        // TODO: pegar o usuario logado
        customer.setCreatedBy( "system" );

        customer.setUpdatedAt( null );
        customer.setUpdatedBy( null );

        contactService.validateContactList( customerView.getContactList() );

        Customer customerSaved = customerRepository.save( customer );

        contactService.saveContactList( customerView.getContactList(), ObjectType.CUSTOMER, customerSaved.getId() );
        return customerSaved;

    }


    private void validateCustomer( Customer customer ) {

        Assert.notNull( customer, "Customer cannot be null." );
        Assert.hasText( customer.getName(), "name cannot be empty." );
        Assert.hasText( customer.getDocumentNumber(), "documentNumber cannot be empty." );
        Assert.notNull( customer.getBirthDate(), "birthDate cannot be empty." );
        Assert.notNull( customer.getNacionality(), "nacionality cannot be null." );
    }


    @Override
    public List< Customer > findAll() {

        return customerRepository.findAll();
    }


    @Override
    public Customer findById( Long id ) {

        return customerRepository.findOne( id );
    }


    @Override
    @Transactional( readOnly = true )
    public List< Customer > findByFilter(
        String brand,
        String model,
        String color,
        Integer maxYear,
        Integer minYear,
        Float maxPrice,
        Float minPrice,
        Boolean isNew ) {

        List< Customer > list = null;
        if ( brand == null && model == null && color == null && maxYear == null && minYear == null && maxPrice == null && minPrice == null && isNew == null ) {
            list = findAll();

        } else {

            // list = customerRepository.findByFilter(brand, model, color, maxYear, minYear,
            // maxPrice, minPrice, isNew);
        }
        return list;
    }


    @Override
    public void delete( Long id ) {

        Assert.notNull( id, "id cannot be null." );
        customerRepository.delete( id );
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public Customer update( CustomerView customerView ) {

        Assert.notNull( customerView, "customerView cannot be null." );

        Customer customer = customerView.getCustomer();
        validateCustomer( customer );
        Assert.notNull( customer.getId(), "customer ID cannot be null." );
        contactService.validateContactList( customerView.getContactList() );

        Customer oldCustomer = customerRepository.getOne( customer.getId() );

        if ( oldCustomer == null ) {
            throw new IllegalArgumentException( "Not exist customer with this Id=" + customer.getId() );
        }

        oldCustomer.setName( customer.getName() );
        oldCustomer.setDocumentNumber( customer.getDocumentNumber() );
        oldCustomer.setSecondurayDocumentNumber( customer.getSecondurayDocumentNumber() );
        oldCustomer.setSecondurayDocumentType( customer.getSecondurayDocumentType() );
        oldCustomer.setBirthDate( customer.getBirthDate() );
        oldCustomer.setNacionality( customer.getNacionality() );
        oldCustomer.setCivilStatus( customer.getCivilStatus() );
        oldCustomer.setJob( customer.getJob() );

        oldCustomer.setUpdatedAt( new Date() );
        // TODO: pegar o usuario logado
        oldCustomer.setUpdatedBy( "system" );

        customer = customerRepository.save( oldCustomer );

        List< Contact > oldContactList = contactService.findByFilter( ObjectType.CUSTOMER.getId(), customer.getId() );

        List< Contact > oldContactUpdated = new ArrayList<>();
        List< Contact > contactUpdated = new ArrayList<>();
        for ( Contact contact : customerView.getContactList() ) {

            if ( contact.getId() != null ) {
                Boolean existContact = false;
                for ( Contact oldContact : oldContactList ) {
                    if ( contact.getId().equals( oldContact.getId() ) ) {
                        existContact = true;
                        contactService.update( contact );
                        oldContactUpdated.add( oldContact );
                        contactUpdated.add( contact );
                    }
                }

                if ( existContact == false ) {
                    throw new IllegalArgumentException( "Contact with id=" + contact.getId() + " not exist by customerID=" + customer.getId() );
                }
            }
        }

        customerView.getContactList().removeAll( contactUpdated );
        contactService.saveContactList( customerView.getContactList(), ObjectType.CUSTOMER, customer.getId() );

        oldContactList.removeAll( oldContactUpdated );
        for ( Contact oldContact : oldContactList ) {
            oldContact.setActive( Boolean.FALSE );
            contactService.update( oldContact );
        }

        return customer;

    }

}
