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
import java.util.concurrent.atomic.AtomicLong;

public class BranchLoader implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(BranchLoader.class);
    private static final int ID_OFFSET = 50;
    private static final int ITERATE_TIME = 10_000;
    private static final AtomicLong TOTAL_TIME = new AtomicLong(0);
    private static final AtomicLong TOTAL_ITERATE = new AtomicLong(0);

    private final AtomicBoolean stop = new AtomicBoolean(false);
    private final int startIndexBranch;
    private final int finishIndexBranch;

    public BranchLoader(final int startIndexBranch, final int finishIndexBranch) {
        this.startIndexBranch = startIndexBranch;
        this.finishIndexBranch = finishIndexBranch;
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
        }
    }

    private void loadBranches(final SessionFactory sessionFactory, final Statistics stats) {
        final Session session = sessionFactory.openSession();
        try {
            final long start = System.currentTimeMillis();
            for (int i = startIndexBranch + ID_OFFSET; i < finishIndexBranch + ID_OFFSET; i++) {
                loadBranchById(session, i);
            }

            final long timePassed = System.currentTimeMillis() - start;
            if (timePassed < ITERATE_TIME) {
                LOG.info("########################################  Thread '{}' sleep", Thread.currentThread().getId());
                Thread.sleep(ITERATE_TIME - timePassed);
            } else {
                LOG.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  TimeSpent: {}ms", timePassed);
            }

            long averageTotalTime = TOTAL_TIME.addAndGet(timePassed) / TOTAL_ITERATE.incrementAndGet();
            LOG.info("*************************************************************** AVARAGE TIME: {}ms", averageTotalTime);
            LOG.info("*************************************************************** TOTAL TIME:   {}sec", (System.currentTimeMillis() - PerformanceTest.START_TIME) / 1000);

            HibernateUtil.printStatistics(LOG, stats);
        } catch (Exception ex) {
            LOG.error("Unexpected exception", ex);
        } finally {
            session.flush();
            session.clear();
            session.close();
        }
    }

    private void loadBranchById(final Session session, final int i) {
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
