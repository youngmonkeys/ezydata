package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.exception.EzyCreateQueryException;
import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.database.query.EzyQLQueryFactory;
import org.testng.annotations.Test;

public class EzyQLQueryTest {

    public static void main(String[] args) {
        new EzyQLQueryTest().test();
    }

    @Test
    public void test() {
        EzyQLQueryFactory factory = EzyQLQueryFactory.builder()
            .parameterConverter(p -> p != null ? p : "null")
            .build();
        String queryString = "select e from E e where id = ?0 and name = ?1";
        EzyQLQuery query = factory.newQuery(queryString, 1, "dung");
        assert query.getQuery().equals(queryString);
        assert query.getValue().equals("select e from E e where id = 1 and name = dung");
        System.out.println(query);

        EzyQLQuery queryA = EzyQLQuery.builder()
            .parameter(0, true)
            .parameter(1, (byte) 1)
            .parameter(2, 'a')
            .parameter(3, 2D)
            .parameter(4, 3F)
            .parameter(5, 5)
            .parameter(6, 6L)
            .parameter(7, (short) 7)
            .query("select e from E e where e.id = ?0 and e.name = dung")
            .build();
        System.out.println(queryA);
        EzyQLQuery queryB = EzyQLQuery.builder()
            .query("select e from E e")
            .build();
        System.out.println(queryB);

        EzyQLQuery queryC = EzyQLQuery.builder()
            .query("select e from E e where e.id = ?a")
            .build();
        System.out.println(queryC);

        EzyQLQuery queryD = EzyQLQuery.builder()
            .query("select e from E e where e.id = ?a and e.name = ?0")
            .parameter(0, "dung")
            .build();
        System.out.println(queryD);

        EzyQLQuery queryE = EzyQLQuery.builder()
            .query("select e from E e where e.id = ?0 and e.name = ?a")
            .parameter(0, 1)
            .build();
        System.out.println(queryE);

        try {
            EzyQLQuery.builder()
                .query("select e from E e where e.id = ?0 and e.name = ?1")
                .parameter(0, 1)
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            assert e instanceof EzyCreateQueryException;
        }
    }

}
