package com.tvd12.ezydata.database.test.query;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.query.EzyQueryConditionChain;
import com.tvd12.ezydata.database.query.EzyQueryConditionGroup;
import com.tvd12.ezydata.database.query.EzyQueryMethod;
import com.tvd12.ezydata.database.query.EzyQueryMethodType;
import com.tvd12.ezydata.database.query.EzyQueryOperation;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.util.Next;
import com.tvd12.test.assertion.Asserts;

public class EzyQueryMethodTest {

    @Test
    public void methodCount() {
        // given
        EzyMethod method = getMethod("countByNameAndValue", String.class, String.class);
        
        // when
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);
        
        // then
        Asserts.assertEquals(queryMethod.getMethod(), method);
        Asserts.assertEquals(queryMethod.getType(), EzyQueryMethodType.COUNT);
        
        EzyQueryConditionChain conditionChain = queryMethod.getConditionChain();
        Asserts.assertEquals(conditionChain.getParameterCount(), 2);
        
        List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
        Asserts.assertEquals(conditionGroups.size(), 1);
        
        EzyQueryConditionGroup conditionGroup = conditionGroups.get(0);
        Asserts.assertEquals(conditionGroup.size(), 2);
        Asserts.assertEquals(conditionGroup.getConditions().get(0).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup.getConditions().get(0).getField(), "name");
        Asserts.assertEquals(conditionGroup.getConditions().get(1).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup.getConditions().get(1).getField(), "value");
    }
    
    @Test
    public void methodFind() {
        // given
        EzyMethod method = getMethod("findByNameAndValue", String.class, String.class, Next.class);
        
        // when
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);
        
        // then
        Asserts.assertEquals(queryMethod.getMethod(), method);
        Asserts.assertEquals(queryMethod.getType(), EzyQueryMethodType.FIND);
        
        EzyQueryConditionChain conditionChain = queryMethod.getConditionChain();
        Asserts.assertEquals(conditionChain.getParameterCount(), 2);
        
        List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
        Asserts.assertEquals(conditionGroups.size(), 1);
        
        EzyQueryConditionGroup conditionGroup = conditionGroups.get(0);
        Asserts.assertEquals(conditionGroup.size(), 2);
        Asserts.assertEquals(conditionGroup.getConditions().get(0).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup.getConditions().get(0).getField(), "name");
        Asserts.assertEquals(conditionGroup.getConditions().get(1).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup.getConditions().get(1).getField(), "value");
    }
    
    @Test
    public void methodDelete() {
        // given
        EzyMethod method = getMethod("deleteByNameAndValue", String.class, String.class);
        
        // when
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);
        
        // then
        Asserts.assertEquals(queryMethod.getMethod(), method);
        Asserts.assertEquals(queryMethod.getType(), EzyQueryMethodType.DELETE);
        
        EzyQueryConditionChain conditionChain = queryMethod.getConditionChain();
        Asserts.assertEquals(conditionChain.getParameterCount(), 2);
        
        List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
        Asserts.assertEquals(conditionGroups.size(), 1);
        
        EzyQueryConditionGroup conditionGroup = conditionGroups.get(0);
        Asserts.assertEquals(conditionGroup.size(), 2);
        Asserts.assertEquals(conditionGroup.getConditions().get(0).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup.getConditions().get(0).getField(), "name");
        Asserts.assertEquals(conditionGroup.getConditions().get(1).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup.getConditions().get(1).getField(), "value");
    }
    
    @Test
    public void emptyParamsMethod() {
        // given
        EzyMethod method = getMethod("findAll");
        
        // when
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);
        
        // then
        Asserts.assertEquals(queryMethod.getMethod(), method);
        Asserts.assertEquals(queryMethod.getType(), EzyQueryMethodType.FIND);
        
        EzyQueryConditionChain conditionChain = queryMethod.getConditionChain();
        Asserts.assertEquals(conditionChain.getParameterCount(), 0);
        
        List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
        Asserts.assertEquals(conditionGroups.size(), 0);
    }
    
    @Test
    public void notEnoughParameters() {
        // given
        EzyMethod method = getMethod("findByName");
        
        // when
        Throwable e = Asserts.assertThrows(() -> new EzyQueryMethod(method));
        
        // then
        Asserts.assertEqualsType(e, IllegalArgumentException.class);
    }
    
    private EzyMethod getMethod(String name, Class<?>... parameters) {
        try {
            return new EzyMethod(Repo.class.getDeclaredMethod(name, parameters));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static class Data {}
    
    private static interface Repo extends EzyDatabaseRepository<Long, Data> {
        
        int countByNameAndValue(String name, String value);
        
        List<Data> findByNameAndValue(String name, String value, Next next);
        
        int deleteByNameAndValue(String name, String value);
        
        @Override
        List<Data> findAll();
        
        Data findByName();
    }
}
