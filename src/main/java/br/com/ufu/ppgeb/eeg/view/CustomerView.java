package br.com.ufu.ppgeb.eeg.view;


import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.model.Customer;


public class CustomerView {

    Customer customer;

    List< Contact > contactList;


    public Customer getCustomer() {

        return customer;
    }


    public void setCustomer( Customer customer ) {

        this.customer = customer;
    }


    public List< Contact > getContactList() {

        return contactList;
    }


    public void setContactList( List< Contact > contactList ) {

        this.contactList = contactList;
    }


    @Override
    public String toString() {

        return "CustomerView{" + "customer=" + customer + ", contactList=" + contactList + '}';
    }
}
