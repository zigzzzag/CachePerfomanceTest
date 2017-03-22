package org.zigzzzag;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateEHCacheTest {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateEHCacheTest.class);

    private HibernateEHCacheTest() {
    }

    public static void main(String[] args) {
        LOG.info("Temp Dir: {}", System.getProperty("java.io.tmpdir"));

        //Initialize Sessions
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Statistics stats = sessionFactory.getStatistics();
        LOG.info("Stats enabled={}", stats.isStatisticsEnabled());
        stats.setStatisticsEnabled(true);
        LOG.info("Stats enabled={}", stats.isStatisticsEnabled());


        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        printStats(stats, 0);

        Branch branch = (Branch) session.load(Branch.class, 1L);
        printData(branch, stats, 1);

        branch = (Branch) session.load(Branch.class, 1L);
        printData(branch, stats, 2);

        //clear first level cache, so that second level cache is used
        session.evict(branch);
        branch = (Branch) session.load(Branch.class, 1L);
        printData(branch, stats, 3);

        branch = (Branch) session.load(Branch.class, 3L);
        printData(branch, stats, 4);

        transaction.commit();


        Session otherSession1 = sessionFactory.openSession();
        Transaction otherTransaction1 = otherSession1.beginTransaction();
        branch = (Branch) otherSession1.load(Branch.class, 1L);
        printData(branch, stats, 5);
        otherTransaction1.commit();


        Session otherSession2 = sessionFactory.openSession();
        Transaction otherTransaction2 = otherSession2.beginTransaction();
        branch = (Branch) otherSession2.load(Branch.class, 1L);
        printData(branch, stats, 6);
        otherTransaction2.commit();


        sessionFactory.close();
    }

    private static void printStats(Statistics stats, int i) {
        LOG.info("***** {} *****", i);
        LOG.info("Fetch Count={}", stats.getEntityFetchCount());
        LOG.info("Second Level Hit Count={}", stats.getSecondLevelCacheHitCount());
        LOG.info("Second Level Miss Count={}", stats.getSecondLevelCacheMissCount());
        LOG.info("Second Level Put Count={}", stats.getSecondLevelCachePutCount());
    }

    private static void printData(Branch branch, Statistics stats, int count) {
        LOG.info("{}:: Name={}, Type={}", count, branch.getName(), branch.getType());
        printStats(stats, count);
    }
}
