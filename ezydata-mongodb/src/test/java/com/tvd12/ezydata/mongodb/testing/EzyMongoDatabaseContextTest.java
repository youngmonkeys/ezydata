package com.tvd12.ezydata.mongodb.testing;

import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.testing.bean.*;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EzyMongoDatabaseContextTest extends MongodbTest {

    @Test
    public void test() {
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .maxIdCollectionName("___max_id___")
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();
        assert databaseContext.getClient() == mongoClient;
        assert databaseContext.getDatabase().getName().equals(databaseName);
        CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
        Customer customer = new Customer();
        customer.setName("dzung");
        customerRepo.save(customer);
        System.out.println(customer);
        customerRepo.deleteAll();

        CategoryRepo categoryRepo = databaseContext.getRepository(CategoryRepo.class);
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");
        assert category1.getName() == null;
        assert category2.getName() == null;
        categoryRepo.save(Arrays.asList(category1, category2));
        assert category1.getName() != null;
        assert category2.getName() != null;
        System.out.println("category is: " + categoryRepo.findByField("value", "category1"));

        EzyIdCollectionRepo1 idCollectionRepo1 = databaseContext.getRepository(EzyIdCollectionRepo1.class);
        idCollectionRepo1.save(new EzyIdCollection1(new EzyIdCompositeId1("Hello"), "World"));
        Asserts.assertEquals(
            idCollectionRepo1.findById(new EzyIdCompositeId1("Hello")),
            new EzyIdCollection1(new EzyIdCompositeId1("Hello"), "World")
        );

        EzyIdCollectionRepo2 idCollectionRepo2 = databaseContext.getRepository(EzyIdCollectionRepo2.class);
        idCollectionRepo2.save(new EzyIdCollection2(new EzyIdCompositeId2("Hello"), "World"));
        Asserts.assertEquals(
            idCollectionRepo2.findById(new EzyIdCompositeId2("Hello")),
            new EzyIdCollection2(new EzyIdCompositeId2("Hello"), "World")
        );

        EzyCollectionIdCollectionRepo1 collectionIdCollectionRepo1 =
            databaseContext.getRepository(EzyCollectionIdCollectionRepo1.class);
        collectionIdCollectionRepo1.save(
            new EzyCollectionIdCollection1(new EzyCollectionIdCompositeId1("Hello"), "World"));
        Asserts.assertEquals(
            collectionIdCollectionRepo1.findById(new EzyCollectionIdCompositeId1("Hello")),
            new EzyCollectionIdCollection1(new EzyCollectionIdCompositeId1("Hello"), "World")
        );

        EzyCollectionIdCollectionRepo2 collectionIdCollectionRepo2 =
            databaseContext.getRepository(EzyCollectionIdCollectionRepo2.class);
        collectionIdCollectionRepo2.save(
            new EzyCollectionIdCollection2(new EzyCollectionIdCompositeId2("Hello"), "World"));
        Asserts.assertEquals(
            collectionIdCollectionRepo2.findById(new EzyCollectionIdCompositeId2("Hello")),
            new EzyCollectionIdCollection2(new EzyCollectionIdCompositeId2("Hello"), "World")
        );

        try {
            new EzyMongoDatabaseContextBuilder()
                .repositoryInterface(RepoA.class)
                .mongoClient(mongoClient)
                .databaseName(databaseName)
                .build();
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }

        try {
            new EzyMongoDatabaseContextBuilder()
                .repositoryInterface(RepoB.class)
                .mongoClient(mongoClient)
                .databaseName(databaseName)
                .build();
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }

        try {
            new EzyMongoDatabaseContextBuilder()
                .repositoryInterface(RepoC.class)
                .mongoClient(mongoClient)
                .databaseName(databaseName)
                .build();
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }

        databaseContext = new EzyMongoDatabaseContextBuilder()
            .repositoryInterface(RepoD.class)
            .mongoClient(mongoClientLoader().load())
            .databaseName(databaseName)
            .dataConverter(EzyMongoDataConverter.builder().build())
            .build();
        databaseContext.close();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void findListWithQueryTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .maxIdCollectionName("___max_id___")
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();
        CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
        customerRepo.deleteAll();
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("dzung");
        customerRepo.save(customer);

        EzyQLQuery query = EzyQLQuery.builder()
            .query("{name: ?0}")
            .parameter(0, "'dzung'")
            .build();

        // when
        List actual = MethodInvoker.create()
            .object(customerRepo)
            .method("findListWithQuery")
            .param(query)
            .call();

        // then
        Asserts.assertEquals(actual.size(), 1);
        customerRepo.deleteAll();
    }

    @Test
    public void countWithQueryTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .maxIdCollectionName("___max_id___")
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();
        CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
        customerRepo.deleteAll();
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("dzung");
        customerRepo.save(customer);

        EzyQLQuery query = EzyQLQuery.builder()
            .query("{name: ?0}")
            .parameter(0, "'dzung'")
            .build();

        // when
        Long actual = MethodInvoker.create()
            .object(customerRepo)
            .method("countWithQuery")
            .param(query)
            .call();

        // then
        Asserts.assertEquals(actual, 1L);
        customerRepo.deleteAll();
    }

    @Test
    public void aggregateOneWithQueryTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .maxIdCollectionName("___max_id___")
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();
        CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
        customerRepo.deleteAll();
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("dzung");
        customerRepo.save(customer);

        EzyQLQuery query = EzyQLQuery.builder()
            .query(
                "[{ $match: { name: ?0 }}]"
            )
            .parameter(0, "'dzung'")
            .build();

        // when
        Customer actual = MethodInvoker.create()
            .object(customerRepo)
            .method("aggregateOneWithQuery")
            .param(query)
            .param(Customer.class)
            .call();

        // then
        Asserts.assertNotNull(actual);
        customerRepo.deleteAll();
    }

    @Test
    public void aggregateOneWithQueryAndOptionalTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .maxIdCollectionName("___max_id___")
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();
        CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
        customerRepo.deleteAll();
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("dzung");
        customerRepo.save(customer);

        EzyQLQuery query = EzyQLQuery.builder()
            .query(
                "[{ $match: { name: ?0 }}]"
            )
            .parameter(0, "'dzung'")
            .build();

        // when
        Customer actual = MethodInvoker.create()
            .object(customerRepo)
            .method("aggregateOneWithQuery")
            .param(query)
            .param(Customer.class)
            .call();

        // then
        Customer expectation = customerRepo.fetchCustomerByNameWork("dzung").get();
        Asserts.assertEquals(actual, expectation);
        customerRepo.deleteAll();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void aggregateListWithQueryTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .maxIdCollectionName("___max_id___")
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();
        CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
        customerRepo.deleteAll();
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("dzung");
        customerRepo.save(customer);

        EzyQLQuery query = EzyQLQuery.builder()
            .query(
                "[{ $match: { name: ?0 }}]"
            )
            .parameter(0, "'dzung'")
            .build();

        // when
        List actual = MethodInvoker.create()
            .object(customerRepo)
            .method("aggregateListWithQuery")
            .param(query)
            .param(Customer.class)
            .call();

        // then
        Asserts.assertEquals(actual.size(), 1);
        customerRepo.deleteAll();
    }

    @Test
    public void findOptionalTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .maxIdCollectionName("___max_id___")
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();
        CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
        customerRepo.deleteAll();
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("dzung");
        customerRepo.save(customer);

        // when
        Optional<Customer> actual = customerRepo.findByName("dzung");

        // then
        Asserts.assertEquals(actual.get(), new Customer("1", "dzung", "hello", "world"));
        customerRepo.deleteAll();
    }

    public static interface RepoA extends EzyMongoRepository<Integer, Person> {

        @EzyQuery("{}")
        void countInvalid();

    }

    public static interface RepoB extends EzyMongoRepository<Integer, Person> {

        @EzyQuery("{}")
        Class<?> deleteInvalid();

    }

    public static interface RepoC extends EzyMongoRepository<Integer, Person> {

        @EzyQuery("{}")
        Class<?> invalid();

    }

    public static interface RepoD extends EzyMongoRepository<Integer, Person> {

        @EzyQuery(resultType = Person.class, value = "{}")
        Person findByName();

        @EzyQuery("{}")
        int countByName1();

        @EzyQuery("{}")
        long countByName2();

        @EzyQuery("{}")
        List<Person> findListByName();

        @EzyQuery(resultType = Person.class, value = "{}")
        Person fetchByName();

        @EzyQuery("{}")
        Person fetchListByName();
    }

}
