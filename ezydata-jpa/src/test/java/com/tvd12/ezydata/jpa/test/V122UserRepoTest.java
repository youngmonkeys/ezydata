package com.tvd12.ezydata.jpa.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.bean.EzyJpaRepositoryImplementer;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.repo.V122UserRepo;
import com.tvd12.ezydata.jpa.test.result.UserEmailFullNameResult;
import com.tvd12.test.assertion.Asserts;

public class V122UserRepoTest extends BaseJpaTest {

    @Test
    public void findListByQueryStringTest() throws Exception {
        // given
        EzyJpaRepositoryImplementer.setDebug(true);
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryClass(V122UserRepo.class)
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        V122UserRepo userRepo = databaseContext.getRepository(V122UserRepo.class);
        userRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        User user = new User();
        user.setEmail(email);
        userRepo.save(user);
        
        // when
        List<User> actual = userRepo.findByEmail(email);
        
        // then
        Asserts.assertEquals(actual, Arrays.asList(user), false);
        userRepo.deleteAll();
    }
    
    @Test
    public void fetchListByQueryStringAndParamMapTest() throws Exception {
        // given
        EzyJpaRepositoryImplementer.setDebug(true);
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryClass(V122UserRepo.class)
                .queryResultClass(UserEmailFullNameResult.class)
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        V122UserRepo userRepo = databaseContext.getRepository(V122UserRepo.class);
        userRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        User user = new User();
        user.setEmail(email);
        userRepo.save(user);
        
        // when
        List<UserEmailFullNameResult> actual = userRepo.fetchUserEmailByParamMap(email);
        
        // then
        List<UserEmailFullNameResult> expectation = Collections.singletonList(
            new UserEmailFullNameResult(user.getEmail(), user.getFullName())
        );
        Asserts.assertEquals(actual, expectation, false);
        Asserts.assertEmpty(userRepo.fetchUserEmailByParamMap("nothing"));
        userRepo.deleteAll();
    }
    
    @Test
    public void fetchListByQueryStringAndParamListTest() throws Exception {
        // given
        EzyJpaRepositoryImplementer.setDebug(true);
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryClass(V122UserRepo.class)
                .queryResultClass(UserEmailFullNameResult.class)
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        V122UserRepo userRepo = databaseContext.getRepository(V122UserRepo.class);
        userRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        User user = new User();
        user.setEmail(email);
        userRepo.save(user);
        
        // when
        List<UserEmailFullNameResult> actual = userRepo.fetchUserEmailByParamList(email);
        
        // then
        List<UserEmailFullNameResult> expectation = Collections.singletonList(
            new UserEmailFullNameResult(user.getEmail(), user.getFullName())
        );
        Asserts.assertEquals(actual, expectation, false);
        Asserts.assertEmpty(userRepo.fetchUserEmailByParamList("nothing"));
        userRepo.deleteAll();
    }
    
    @Test
    public void countByQueryStringTest() throws Exception {
        // given
        EzyJpaRepositoryImplementer.setDebug(true);
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryClass(V122UserRepo.class)
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        V122UserRepo userRepo = databaseContext.getRepository(V122UserRepo.class);
        userRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        User user = new User();
        user.setEmail(email);
        userRepo.save(user);
        
        // when
        long actual = userRepo.countByEmail(email);
        
        // then
        Asserts.assertEquals(actual, 1L);
        userRepo.deleteAll();
    }
    
    @Test
    public void countByQueryStringAndArrayParamTest() throws Exception {
        // given
        EzyJpaRepositoryImplementer.setDebug(true);
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryClass(V122UserRepo.class)
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        V122UserRepo userRepo = databaseContext.getRepository(V122UserRepo.class);
        userRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        User user = new User();
        user.setEmail(email);
        userRepo.save(user);
        
        // when
        long actual = userRepo.countByEmailWithArrayParam(email);
        
        // then
        Asserts.assertEquals(actual, 1L);
        userRepo.deleteAll();
    }
}
