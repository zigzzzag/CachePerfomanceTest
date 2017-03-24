package org.zigzzzag;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

class GenerateTestData {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateTestData.class);
    private static final int ENTRY_COUNT = 4_000_000;
    private static final int BATCH_SIZE = 50;

    private GenerateTestData() {
    }

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try {
            final Session session = sessionFactory.openSession();
            try {
                session.beginTransaction();
                generateData(session);
                session.getTransaction().commit();
            } finally {
                session.close();
            }
        } finally {
            sessionFactory.close();
        }

        LOG.info("Total time {}ms", System.currentTimeMillis() - startTime);
    }

    private static void generateData(Session session) {
        for (int i = 0; i < ENTRY_COUNT; i++) {
            final Branch p = new Branch()
                    .name("First Name " + i)
                    .type("First Type " + i)
                    .data1("First data1 " + i)
                    .data2("First data2 " + i)
                    .data3("First data3 " + i)
                    .data4("First data4 " + i)
                    .data5("First data5 " + i)
                    .data6("First data6 " + i)
                    .data7("First data7 " + i)
                    .data8("First data8 " + i)
                    .data9("First data9 " + i)
                    .data10("First data10 " + i)
                    .data11("First data11 " + i)
                    .data12("First data12 " + i)
                    .data13("First data13 " + i)
                    .data14("First data14 " + i)
                    .data15("First data15 " + i)
                    .data16("First data16 " + i)
                    .data17("First data17 " + i)
                    .data18("First data18 " + i)
                    .data19("First data19 " + i)
                    .data20("First data20 " + i)
                    .shelfLife(new Date());
            session.save(p);

            if (i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();
                LOG.info("flushing: {}", i);
            }
        }
    }
}
