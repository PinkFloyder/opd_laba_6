package com.brawl.stars.dao;

import com.brawl.stars.entity.AbstractEntity;
import com.brawl.stars.entity.Personage;
import lombok.AllArgsConstructor;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatformException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@AllArgsConstructor
public class BrawlStarsDao {

    private final EntityManager em;

    public <T extends AbstractEntity<?>> List<T> getAllPersonage(final Class<T> tClass) {
        try {
            return em.createQuery("SELECT e FROM Personage e", tClass)
                    .getResultList();
        } catch (Exception e) {
            throw new JtaPlatformException("Some exception");
        }
    }

    public <T extends AbstractEntity<?>> T getPersonageByName(final String name, final Class<T> tClass) {
        try {
            return em.createQuery("SELECT e FROM Personage e " +
                    "WHERE e.name = :NAME", tClass)
                    .setParameter("NAME", name.toUpperCase())
                    .getSingleResult();
        } catch (Exception e) {
            throw new JtaPlatformException("Not found");
        }
    }

    public void savePersonage(Personage personage) {
        em.getTransaction().begin();
        em.persist(personage);
        em.persist(personage.getSpecification());
        em.getTransaction().commit();

    }

    @Transactional(Transactional.TxType.MANDATORY)
    public void updatePersonage(Personage personage) {
        try {
            em.getTransaction().begin();
            em.merge(personage);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new JtaPlatformException("This personage is not exist");
        }
    }

}
