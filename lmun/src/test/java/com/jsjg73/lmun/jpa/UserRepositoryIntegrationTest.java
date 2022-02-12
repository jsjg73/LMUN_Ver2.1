package com.jsjg73.lmun.jpa;

import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.repositories.UserTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserTestRepository userRepository;

    @Test
    public void givenUser_whenFindByNick_thenReturnUser(){
        User jack = User.builder()
                    .username("jack")
                .nick("jsjg73")
                .build();
        entityManager.persist(jack);
        entityManager.flush();

        User found = userRepository.findByNick(jack.getNick());

        assertEquals(found.getUsername(), jack.getUsername());
    }
}
