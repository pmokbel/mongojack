/*
 * Copyright 2011 VZ Netzwerke Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mongojack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class TestDBRefHandlingWithPolymorphicType extends MongoDBTestBase {

    @Test
    public void simpleDbRefShouldBeSavedAsDbRef() {
        //assertThat("1", equalTo("1"));
        JacksonDBCollection<BaseObj, String> coll = getCollection(BaseObj.class, String.class);

        BaseObjExtendedWNoRefs noRefObj = new BaseObjExtendedWNoRefs();
        noRefObj.foo = "Test";
        noRefObj.id = "test1";
        coll.save(noRefObj);

        BaseObjExtendedWRefs refObj = new BaseObjExtendedWRefs();
        refObj.id = "test2";
        refObj.listOfRefs = new ArrayList<DBRef<BaseObj, String>>();
        refObj.listOfRefs.add(new DBRef<BaseObj, String>(noRefObj.id, BaseObj.class));

        coll.save(refObj);

        coll.findOneById(refObj.id);

    }


}
@MongoCollection(name="BaseObj")
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="type")
class BaseObj {

    @Id
    public String id;
}

class BaseObjExtendedWNoRefs extends BaseObj {
    public String foo;
}

class BaseObjExtendedWRefs extends BaseObj {
    public List<DBRef<BaseObj, String>> listOfRefs;
}

