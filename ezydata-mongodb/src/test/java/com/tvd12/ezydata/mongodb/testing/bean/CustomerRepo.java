package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyQuery;

import java.util.List;
import java.util.Optional;

@EzyAutoImpl
public interface CustomerRepo extends EzyMongoRepository<String, Customer> {

    Optional<Customer> findByName(String name);

    Optional<?> findByHello(String hello);

    @EzyQuery("[{ $match: { name: ?0 }}]")
    Optional<?> fetchByName(String name);

    List<CustomerResult> findByWorld(String world);

    @EzyQuery(value = "[{ $match: { name: ?0 }}]", resultType = CustomerResult.class)
    Optional<Customer> fetchCustomerByName(String name);

    @EzyQuery(value = "[{ $match: { name: ?0 }}]")
    Optional<Customer> fetchCustomerByNameWork(String name);

    @EzyQuery(value = "{word: ?0}", resultType = Customer.class)
    CustomerResult findCustomerByWorld(String world);
}
