package br.com.ufu.ppgeb.eeg.repository.impl;


import br.com.ufu.ppgeb.eeg.repository.CarRepositoryCustom;
import br.com.ufu.ppgeb.eeg.model.Car;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * Created by joaol on 15/04/18.
 */
@Repository
public class CarRepositoryImpl implements CarRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public List<Car> findByFilter(String brand, String model, String color, Integer maxYear, Integer minYear, Float maxPrice, Float minPrice, Boolean isNew) {

        Session session = em.unwrap(Session.class);

        Criteria criteria = session.createCriteria(Car.class);

        if (brand != null) {
            criteria.add( Restrictions.eq( "brand", brand ) );
        }
        if (model != null) {
            criteria.add( Restrictions.eq( "model", model ) );
        }
        if (color != null) {
            criteria.add( Restrictions.eq( "color", color ) );
        }
        if (maxYear != null) {
            criteria.add( Restrictions.le( "year", maxYear ) );
        }
        if (minYear != null) {
            criteria.add( Restrictions.ge( "year", minYear ) );
        }
        if (maxPrice != null) {
            criteria.add( Restrictions.le( "price", maxPrice ) );
        }
        if (minPrice != null) {
            criteria.add( Restrictions.ge( "price", minPrice ) );
        }
        if (isNew != null) {
            criteria.add( Restrictions.eq( "isNew", isNew ) );
        }


        List<Car> list = criteria.list();

        return list;
    }
}
