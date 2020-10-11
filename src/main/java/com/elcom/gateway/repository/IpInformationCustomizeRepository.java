/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.repository;

import com.elcom.gateway.model.dto.IpInformation;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class IpInformationCustomizeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpInformationCustomizeRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public IpInformationCustomizeRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public IpInformation findById(Long id) {
        Session session = openSession();
        try {
            IpInformation ipInfor = session.load(IpInformation.class, id);
            return ipInfor;
        } catch (EntityNotFoundException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }

    private Session openSession() {
        Session session = this.sessionFactory.openSession();
        return session;
    }

    private void closeSession(Session session) {
        if (session.isOpen()) {
            session.disconnect();
            session.close();
        }
    }
}
