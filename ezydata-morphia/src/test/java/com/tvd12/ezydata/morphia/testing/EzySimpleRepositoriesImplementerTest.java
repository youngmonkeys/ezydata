package com.tvd12.ezydata.morphia.testing;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.morphia.bean.EzyMorphiaRepositoriesImplementer;
import com.tvd12.ezydata.morphia.testing.data.Cat;
import org.testng.annotations.Test;

import java.util.Map;

public class EzySimpleRepositoriesImplementerTest extends BaseMongoDBTest {

    @Test
    public void test() {
        EzyMorphiaRepositoriesImplementer implementer = new EzyMorphiaRepositoriesImplementer();
        implementer.repositoryInterfaces(Object.class);
        implementer.repositoryInterfaces(InterfaceA.class);
        implementer.repositoryInterfaces(CatXRepo.class);
        Map<Class<?>, Object> map = implementer.implement(DATASTORE);
        assert map.containsKey(CatXRepo.class);
    }

    public interface InterfaceA {}

    public interface CatXRepo extends EzyMongoRepository<String, Cat> {}
}
