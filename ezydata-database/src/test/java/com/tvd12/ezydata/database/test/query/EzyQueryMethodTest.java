package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.query.*;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.util.Next;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void methodDeleteAndOr() {
        // given
        EzyMethod method = getMethod(
            "deleteByNameAndValueOrEmailAndPhoneIn",
            String.class,
            String.class,
            String.class,
            String.class
        );

        // when
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);

        // then
        Asserts.assertEquals(queryMethod.getMethod(), method);
        Asserts.assertEquals(queryMethod.getType(), EzyQueryMethodType.DELETE);

        EzyQueryConditionChain conditionChain = queryMethod.getConditionChain();
        Asserts.assertEquals(conditionChain.getParameterCount(), 4);

        List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
        Asserts.assertEquals(conditionGroups.size(), 2);

        EzyQueryConditionGroup conditionGroup1 = conditionGroups.get(0);
        Asserts.assertEquals(conditionGroup1.size(), 2);
        Asserts.assertEquals(conditionGroup1.getConditions().get(0).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup1.getConditions().get(0).getField(), "name");
        Asserts.assertEquals(conditionGroup1.getConditions().get(1).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup1.getConditions().get(1).getField(), "value");

        EzyQueryConditionGroup conditionGroup2 = conditionGroups.get(1);
        Asserts.assertEquals(conditionGroup2.size(), 2);
        Asserts.assertEquals(conditionGroup2.getConditions().get(0).getOperation(), EzyQueryOperation.EQUAL);
        Asserts.assertEquals(conditionGroup2.getConditions().get(0).getField(), "email");
        Asserts.assertEquals(conditionGroup2.getConditions().get(1).getOperation(), EzyQueryOperation.IN);
        Asserts.assertEquals(conditionGroup2.getConditions().get(1).getField(), "phone");
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

    @Test
    public void splitConditionsTest() {
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "",
                "And"
            ),
            Collections.singletonList(""),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "And",
                "And"
            ),
            Collections.singletonList("And"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "Anda",
                "And"
            ),
            Collections.singletonList("Anda"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "",
                "Or"
            ),
            Collections.singletonList(""),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "Or",
                "Or"
            ),
            Collections.singletonList("Or"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "Ora",
                "Or"
            ),
            Collections.singletonList("Ora"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "Name",
                "And"
            ),
            Collections.singletonList("Name"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "NameAndAnd",
                "And"
            ),
            Arrays.asList("Name", "And"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "NameOrOr",
                "Or"
            ),
            Arrays.asList("Name", "Or"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "NameAndId",
                "And"
            ),
            Arrays.asList("Name", "Id"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "NameAndIdOrEmail",
                "And"
            ),
            Arrays.asList("Name", "IdOrEmail"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "IdOrEmail",
                "Or"
            ),
            Arrays.asList("Id", "Email"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "DisplayOrderInOrName",
                "Or"
            ),
            Arrays.asList("DisplayOrderIn", "Name"),
            false
        );
        Asserts.assertEquals(
            EzyQueryMethod.splitConditions(
                "DataTypeAndDataIdAndMetaKeyAndMetaValue",
                "And"
            ),
            Arrays.asList("DataType", "DataId", "MetaKey", "MetaValue"),
            false
        );
    }

    private EzyMethod getMethod(String name, Class<?>... parameters) {
        try {
            return new EzyMethod(Repo.class.getDeclaredMethod(name, parameters));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unused")
    private interface Repo extends EzyDatabaseRepository<Long, Data> {

        int countByNameAndValue(String name, String value);

        List<Data> findByNameAndValue(String name, String value, Next next);

        int deleteByNameAndValue(String name, String value);

        int deleteByNameAndValueOrEmailAndPhoneIn(
            String name,
            String value,
            String email,
            String phone
        );

        @Override
        List<Data> findAll();

        Data findByName();
    }

    private static class Data {}
}
