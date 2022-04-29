package com.tvd12.ezydata.jpa.test;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.ezyfox.database.annotation.EzyTransactional;

public class EzyJpaRepositoriesImplementerTest extends BaseJpaTest {

    protected EzyDatabaseContext databaseContext;
    
    protected EzyJpaRepositoriesImplementerTest() {
        EzyAbstractRepositoryImplementer.setDebug(true);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("UsersDB");
        databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryInterfaces(Object.class)
                .repositoryInterfaces(InterfaceA.class)
                .repositoryInterfaces(UserXRepo.class)
                .scan("com.tvd12.ezydata.jpa.test.result")
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Test
    public void test() {
        UserXRepo userRepo = (UserXRepo) databaseContext.getRepository(UserXRepo.class); 
        System.out.println(userRepo.count());
        System.out.println(userRepo.findByEmail("dzung@gmail.com"));
        List<UserIdFullNameResult> list = userRepo.findListOfIdAndFullName();
        System.out.println(list);
        databaseContext.close();
    }
    
    public static interface InterfaceA {
        
    }
    
    public static interface UserXRepo extends EzyDatabaseRepository<String, User> {
        
        @EzyQuery("select e from User e where e.email = ?0")
        User findByEmail(String email);
        
        @EzyQuery(
                value = "select e.id, e.fullName from User e where e.email = ?0",
                resultType = UserIdFullNameResult.class)
        UserIdFullNameResult findByEmail2(String email);
        
        @EzyQuery(
                value = "select e.id, e.fullName from User e",
                resultType = UserIdFullNameResult.class
        )
        List<UserIdFullNameResult> findListOfIdAndFullName();
        
        @EzyQuery(
                value = "delete e from User e where e.id = ?0",
                resultType = UserIdFullNameResult.class
        )
        @EzyTransactional
        int deleteUser(String userId);
        
        @EzyQuery(
                value = "update User e set e.id = ?0",
                resultType = UserIdFullNameResult.class
        )
        @EzyTransactional
        int updateUser(String userId);
        
    }
}
