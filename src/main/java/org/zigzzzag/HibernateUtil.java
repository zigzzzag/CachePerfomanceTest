package org.zigzzzag;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            LOG.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void printStatistics(final Logger logger, final Statistics stats) {
        logger.info("{}:Fetch Count={}", stats.hashCode(), stats.getEntityFetchCount());
        logger.info("{}:Second Level Hit Count={}", stats.hashCode(), stats.getSecondLevelCacheHitCount());
        logger.info("{}:Second Level Miss Count={}", stats.hashCode(), stats.getSecondLevelCacheMissCount());
        logger.info("{}:Second Level Put Count={}", stats.hashCode(), stats.getSecondLevelCachePutCount());
    }
}
