package com.hive.hive.association.request;

import com.hive.hive.model.association.Request;
import com.hive.hive.model.association.RequestCategory;
import java.util.HashMap;

/**
 * Created by vplentz on 03/02/18.
 */

public class DUMMYREQUESTS {
    public static HashMap<String,Request> requests = new HashMap<>();
    static HashMap<String, RequestCategory> categories = new HashMap<>();

    public static void createArray(){
        categories.put("cat1", new RequestCategory("cat1", "categoria 1"));

        Request request = new Request("0", 0, 0, "uuid0",
                "0", "0", "titulo 0",
                "desc 0 " , 0, categories, null, null);
        DUMMYREQUESTS.requests.put("0", request);

        request = new Request("1", 0, 0, "uuid1",
                "0", "0", "titulo 1",
                "desc 1 " , 0, categories, null, null);
        DUMMYREQUESTS.requests.put("1", request);
    }
}
