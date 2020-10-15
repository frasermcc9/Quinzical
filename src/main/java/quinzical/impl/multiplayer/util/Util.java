package quinzical.impl.multiplayer.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class Util {
    public static JSONObject asJson(Object o) throws NoSuchFieldException, IllegalAccessException, JSONException {
        Field[] fields = o.getClass().getDeclaredFields();

        JSONObject object = new JSONObject();

        for (Field field : fields) {
            Object extracted = o.getClass().getDeclaredField(field.getName()).get(o);
            String name = field.getName();
            object.put(name, extracted.toString());
        }

        return object;
    }
}
