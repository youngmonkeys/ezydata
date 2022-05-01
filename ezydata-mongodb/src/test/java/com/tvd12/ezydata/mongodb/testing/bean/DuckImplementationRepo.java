package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.repository.EzySimpleMongoRepository;
import com.tvd12.ezydata.mongodb.testing.result.DuckResult;
import com.tvd12.ezydata.mongodb.testing.result.DuckResult2;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.ezyfox.util.Next;

import java.util.List;

@EzyRepository
public class DuckImplementationRepo
    extends EzySimpleMongoRepository<DuckId, Duck> {

    @Override
    public Duck findOneWithQuery(EzyQLQuery query) {
        return super.findOneWithQuery(query);
    }

    @Override
    public List<Duck> findListWithQuery(EzyQLQuery query) {
        return super.findListWithQuery(query);
    }

    @Override
    public List<Duck> findListWithQuery(EzyQLQuery query, Next next) {
        return super.findListWithQuery(query, next);
    }

    @Override
    public long countWithQuery(EzyQLQuery query) {
        return super.countWithQuery(query);
    }

    @Override
    public long countWithQuery(EzyQLQuery query, Next next) {
        return super.countWithQuery(query, next);
    }

    public DuckResult aggregateOneWithQuery(EzyQLQuery query) {
        return super.aggregateOneWithQuery(
            query,
            DuckResult.class
        );
    }

    public DuckResult2 aggregateOneWithQuery2(EzyQLQuery query) {
        return super.aggregateOneWithQuery(
            query,
            DuckResult2.class
        );
    }

    public List<DuckResult> aggregateListWithQuery(EzyQLQuery query) {
        return super.aggregateListWithQuery(
            query,
            DuckResult.class
        );
    }

    public List<DuckResult2> aggregateListWithQuery2(EzyQLQuery query) {
        return super.aggregateListWithQuery(
            query,
            DuckResult2.class
        );
    }

    @Override
    public int updateWithQuery(EzyQLQuery query) {
        return super.updateWithQuery(query);
    }

    @Override
    public int deleteWithQuery(EzyQLQuery query) {
        return super.deleteWithQuery(query);
    }

    @Override
    public EzyQLQuery.Builder newQueryBuilder() {
        return super.newQueryBuilder();
    }
}

