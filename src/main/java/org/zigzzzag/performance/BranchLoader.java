package org.zigzzzag.performance;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zigzzzag.Branch;
import org.zigzzzag.HibernateUtil;

import java.util.concurrent.atomic.AtomicBoolean;

public class BranchLoader implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(BranchLoader.class);
    private final int maxBranchCount;
    private final AtomicBoolean stop = new AtomicBoolean(false);
    private static final int ID_OFFSET = 50;

    public BranchLoader(final int maxBranchCount) {
        this.maxBranchCount = maxBranchCount;
    }

    @Override
    public void run() {
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        final Statistics stats = sessionFactory.getStatistics();
        LOG.info("Stats enabled={}", stats.isStatisticsEnabled());
        stats.setStatisticsEnabled(true);
        LOG.info("Stats enabled={}", stats.isStatisticsEnabled());

        while (!stop.get()) {
            loadBranches(sessionFactory, stats);

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private void loadBranches(SessionFactory sessionFactory, Statistics stats) {
        final Session session = sessionFactory.openSession();
        try {
            for (int i = ID_OFFSET; i < maxBranchCount + ID_OFFSET; i++) {
                loadBranchById(session, i);
            }

            HibernateUtil.printStatistics(LOG, stats);
        } catch (Exception ex) {
            LOG.error("Unexpected exception", ex);
        } finally {
            session.flush();
            session.clear();
            session.close();
        }
    }

    private void loadBranchById(Session session, int i) {
        try {
            final Branch branch = (Branch) session.load(Branch.class, (long) i);
            if (branch.getName() == null) {
                LOG.info("Branch (id={}) has null name", i);
            }
        } catch (ObjectNotFoundException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public void stop() {
        this.stop.set(false);
    }
}
