package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager em;

    @Autowired
    public UserDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public Optional<User> findById(long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }
    @Override
    public User findByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT user FROM User user JOIN FETCH  user.roles WHERE user.email =:email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u " +
                        "JOIN FETCH u.roles " +
                        "ORDER BY u.id ASC", User.class)
                .getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> user = findById(id);
        user.ifPresent(em::remove);
    }
}
