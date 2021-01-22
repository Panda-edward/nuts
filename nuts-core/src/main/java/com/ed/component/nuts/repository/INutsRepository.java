package com.ed.component.nuts.repository;

import java.util.List;

/**
 * @author : Edward
 * @date : 2021/1/19 下午5:49
 */
public interface INutsRepository {

    /**
     * insert
     *
     * @param po
     * @return
     */
    boolean save(NutsRetryRecord po);

    /**
     * update
     */
    boolean updateStatus(Long id, Long nextRetryTime, Integer retryStatus);

    /**
     * query
     *
     * @param count
     * @return
     */
    List<NutsRetryRecord> queryRetries(int count);
}
