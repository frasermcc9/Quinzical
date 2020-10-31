package quinzical.impl.multiplayer.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class Util {
    public static JSONObject asJson(final Object o) throws NoSuchFieldException, IllegalAccessException, JSONException {
        final Field[] fields = o.getClass().getDeclaredFields();

        final JSONObject object = new JSONObject();

        for (final Field field : fields) {
            final Object extracted = o.getClass().getDeclaredField(field.getName()).get(o);
            final String name = field.getName();
            object.put(name, extracted.toString());
        }

        return object;
    }
}
