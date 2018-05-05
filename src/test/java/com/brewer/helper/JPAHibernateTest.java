package com.brewer.helper;

import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class JPAHibernateTest {

	private static EntityManagerFactory emf;
    private EntityManager em;

    static {
        emf = Persistence.createEntityManagerFactory("puTest");
    }

    public EntityManager getEntityManager(String dataSQL) {
        em = emf.createEntityManager();
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try {
                    URL url = getClass().getResource("/scriptsTest/"+dataSQL);
                    File script = new File(url.getFile());
                    RunScript.execute(connection, new FileReader(script));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("could not initialize with script");
                }
            }
        });

        return em;
    }

    public EntityManager getEntityManager() {
        if (em == null) {
            em = emf.createEntityManager();
        }
        return em;
    }

    public void closeEntityManager() {
        if (em != null) {
            em.close();
        }
    }
}
