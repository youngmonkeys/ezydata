package com.tvd12.ezydata.database.test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.converter.EzyResultDeserializer;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.test.bean.*;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.io.EzyMaps;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import lombok.AllArgsConstructor;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

public class EzyDatabaseContextBuilderTest extends BaseTest {

    @Test
    public void test() {
        EzyQueryEntity queryEntity1 = EzyQueryEntity.builder()
            .name("test1")
            .nativeQuery(false)
            .resultType(String.class)
            .type("find")
            .value("select a from b")
            .build();
        EzyQueryEntity queryEntity2 = EzyQueryEntity.builder()
            .name("test2")
            .nativeQuery(true)
            .resultType(Object.class)
            .type("find")
            .value("select a from b")
            .build();
        EzyAbstractRepositoryImplementer.setDebug(true);
        DbContext dbContext = new Builder()
            .addQuery(queryEntity1)
            .addQueries(Lists.newArrayList(queryEntity1, queryEntity2))
            .scan("com.tvd12.ezydata.database.test.bean")
            .scan("com.tvd12.ezydata.database.test.bean", "com.tvd12.ezydata.database.test.bean")
            .scan(Sets.newHashSet("com.tvd12.ezydata.database.test.bean"))
            .scan(new EzyReflectionProxy("com.tvd12.ezydata.database.test.bean"))
            .repositoryInterface(PersonRepo.class)
            .repositoryInterfaces(PersonRepo.class, PersonRepo2.class)
            .repositoryInterfaces(Collections.singletonList(PersonRepo3.class))
            .repositoryClass(PersonRepo5.class)
            .repositoryClasses(PersonRepo5.class, PersonRepo5.class)
            .repositoryClasses(Sets.newHashSet(PersonRepo5.class))
            .queryResultClass(FindResult.class)
            .queryResultClasses(FindResult.class, FindResult.class)
            .queryResultClasses(Sets.newHashSet(FindResult.class))
            .addResultDeserializer(FindResult.class, new EzyResultDeserializer<FindResult>() {
                @Override
                public FindResult deserialize(Object data) {
                    return new FindResult();
                }
            })
            .addResultDeserializers(EzyMaps.newHashMap(FindResult.class, new EzyResultDeserializer<FindResult>() {
            }))
            .build();
        assert dbContext.getQueryManager().getQuery("test1") == queryEntity1;
        assert dbContext.getQuery("test1") == queryEntity1;
        assert dbContext.deserializeResult(new Object[0], FindResult.class) instanceof FindResult;
        assert dbContext.deserializeResultList(Arrays.asList(new Object[0], new Object[0]), FindResult.class).size() == 2;
        assert dbContext.getRepository(PersonRepo.class) != null;
        assert dbContext.getRepository("personRepo") instanceof PersonRepo;
        assert dbContext.getRepositories().size() > 0;
        assert dbContext.getRepositoriesByName().size() > 0;
        try {
            dbContext.getQuery("no one");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            dbContext.getRepository("no one");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            dbContext.getRepository(getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbContext = new Builder().build();
        System.out.println(dbContext);
    }

    @Test
    public void addBindingContextBuilderTest() {
        EzyBindingContextBuilder bindingContextBuilder = EzyBindingContext.builder()
            .scan("com.tvd12.ezydata.database.test.bean");
        new Builder()
            .bindingContextBuilder(bindingContextBuilder)
            .repositoryInterface(PersonRepo.class)
            .repositoryInterfaces(PersonRepo.class, PersonRepo2.class)
            .repositoryInterfaces(Collections.singletonList(PersonRepo3.class))
            .repositoryClass(PersonRepo5.class)
            .repositoryClasses(PersonRepo5.class, PersonRepo5.class)
            .repositoryClasses(Sets.newHashSet(PersonRepo5.class))
            .queryResultClass(FindResult.class)
            .queryResultClasses(FindResult.class, FindResult.class)
            .queryResultClasses(Sets.newHashSet(FindResult.class))
            .addResultDeserializer(FindResult.class, new EzyResultDeserializer<FindResult>() {
            })
            .addResultDeserializers(EzyMaps.newHashMap(FindResult.class, new EzyResultDeserializer<FindResult>() {
            }))
            .build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void multiQueryResultTypeTest() {
        EzyQueryEntity queryEntity1 = EzyQueryEntity.builder()
            .name("find2")
            .nativeQuery(false)
            .resultType(String.class)
            .type("find")
            .value("select a from b")
            .build();
        new Builder()
            .addQuery(queryEntity1)
            .scan("com.tvd12.ezydata.database.test.bean")
            .build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void emptyQueryNameTest() {
        new Builder()
            .scan("com.tvd12.ezydata.database.test.invalid1")
            .build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void invalidResultDeserializerTest() {
        new Builder()
            .scan("com.tvd12.ezydata.database.test.invalid2")
            .build();
    }

    @Test
    public void addRepositoryFromClassRepoWithoutEzyDatabaseContextAware() {
        // given
        DbContext dbContext = new Builder(false)
            .scan("com.tvd12.ezydata.database.test.bean")
            .build();

        // when
        // then
        Asserts.assertNotNull(dbContext);
    }

    @Test
    public void setDbContextErrorRepo() {
        // given
        // when
        Throwable e = Asserts.assertThrows(() ->
            new Builder()
                .repositoryClass(SetDbContextErrorRepo.class)
                .build()
        );
        // then
        Asserts.assertEqualsType(e, IllegalArgumentException.class);
    }

    private static class DbContext extends EzySimpleDatabaseContext {}

    @AllArgsConstructor
    private static class Builder extends EzyDatabaseContextBuilder<Builder> {

        private final boolean awareDBContextRepo;

        public Builder() {
            this(true);
        }

        @Override
        public DbContext build() {
            return (DbContext) super.build();
        }

        @Override
        protected EzySimpleDatabaseContext newDatabaseContext() {
            return new DbContext();
        }

        @Override
        protected EzyAbstractRepositoriesImplementer newRepositoriesImplementer() {
            return awareDBContextRepo
                ? new RepositoriesImplementer()
                : new RepositoriesImplementerWithoutDbContextAware();
        }

        @Override
        public void postCreateRepositoryFromClass(
            EzyDatabaseContext context,
            Object repo
        ) {
            super.postCreateRepositoryFromClass(context, repo);
        }
    }

    private static class RepositoriesImplementer
        extends EzyAbstractRepositoriesImplementer {

        @Override
        protected EzyAbstractRepositoryImplementer newRepoImplementer(Class<?> itf) {
            return new RepositoryImplementer(itf);
        }
    }

    private static class RepositoryImplementer
        extends EzyAbstractRepositoryImplementer {

        public RepositoryImplementer(Class<?> itf) {
            super(itf);
        }

        @Override
        protected Class<?> getSuperClass() {
            return DbRepository.class;
        }
    }

    private static class RepositoriesImplementerWithoutDbContextAware
        extends EzyAbstractRepositoriesImplementer {

        @Override
        protected EzyAbstractRepositoryImplementer newRepoImplementer(Class<?> itf) {
            return new RepositoryImplementerWithoutDbContextAware(itf);
        }
    }

    private static class RepositoryImplementerWithoutDbContextAware
        extends RepositoryImplementer {

        public RepositoryImplementerWithoutDbContextAware(Class<?> itf) {
            super(itf);
        }

        @Override
        protected Class<?> getSuperClass() {
            return BaseDbRepository.class;
        }
    }

    public static class SetDbContextErrorRepo extends DbRepository<Long, Person> {
        @Override
        public void setDatabaseContext(EzyDatabaseContext context) {
            throw new IllegalStateException("just test");
        }
    }
}
