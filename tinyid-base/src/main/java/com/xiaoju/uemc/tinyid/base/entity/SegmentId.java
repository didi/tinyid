package com.xiaoju.uemc.tinyid.base.entity;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author du_imba
 * nextId: (currentId, maxId]
 */
public class SegmentId {
    private long maxId;
    private long loadingId;
    private AtomicLong currentId;
    /**
     * increment by
     */
    private int delta;
    /**
     * mod num
     */
    private int remainder;

    private volatile boolean isInit;

    public void init() {
        if (isInit) {
            return;
        }
        synchronized (this) {
            if (isInit) {
                return;
            }
            long id = currentId.get();
            if (id % delta == remainder) {
                isInit = true;
                return;
            }
            for (int i = 0; i <= delta; i++) {
                id = currentId.incrementAndGet();
                if (id % delta == remainder) {
                    currentId.addAndGet(0 - delta);
                    isInit = true;
                    return;
                }
            }
        }
    }

    public Result nextId() {
        init();
        long id = currentId.addAndGet(delta);
        if (id > maxId) {
            return new Result(ResultCode.OVER, id);
        }
        if (id >= loadingId) {
            return new Result(ResultCode.LOADING, id);
        }
        return new Result(ResultCode.NORMAL, id);
    }

    public boolean useful() {
        return currentId.get() <= maxId;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public long getLoadingId() {
        return loadingId;
    }

    public void setLoadingId(long loadingId) {
        this.loadingId = loadingId;
    }

    public AtomicLong getCurrentId() {
        return currentId;
    }

    public void setCurrentId(AtomicLong currentId) {
        this.currentId = currentId;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }

    @Override
    public String toString() {
        return "[maxId=" + maxId + ",loadingId=" + loadingId + ",currentId=" + currentId + ",delta=" + delta + ",remainder=" + remainder + "]";
    }
}
