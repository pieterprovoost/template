package be.pieterprovoost.demo.repository;

import be.pieterprovoost.demo.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository {

@PersistenceContext
private EntityManager em;

    public User findByUsername(String username) {
        return em.createQuery("select u from User u where username=?1", User.class).setParameter(1, username).getSingleResult();
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

}
