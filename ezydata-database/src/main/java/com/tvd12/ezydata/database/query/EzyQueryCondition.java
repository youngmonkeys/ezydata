package com.tvd12.ezydata.database.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzyQueryCondition {
    
    protected String field;
    protected EzyQueryOperation operation;

    public static EzyQueryCondition parse(String str) {
        String field = str;
        EzyQueryOperation operation = EzyQueryOperation.EQUAL;
        for (EzyQueryOperation it : EzyQueryOperation.notIncludeEqualValues()) {
            if(str.endsWith(it.getTag())) {
                field = str.substring(0, str.length() - it.getTag().length());
                operation = it;
                break;
            }
        }
        field = field.substring(0, 1).toLowerCase() + field.substring(1);
        return new EzyQueryCondition(field, operation);
    }
}
