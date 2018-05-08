package com.brewer.repository.helper.cidade;

import com.brewer.helper.JPAHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CidadesImplTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        EntityManager entityManager = JPAHibernateTest.getEntityManager();
    }

    @After
    public void tearDown() {
        JPAHibernateTest.roolbackEcloseEntityManager();
    }

    @Test
    public void filtrar() {
//        fail();
    }
}