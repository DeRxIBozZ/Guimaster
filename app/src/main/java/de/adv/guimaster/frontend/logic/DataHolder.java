package de.adv.guimaster.frontend.logic;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DataHolder {
    public Map<String, WeakReference<Object>> data = new HashMap<>();

    public void save(String id, Object object){
        data.put(id, new WeakReference<>(object));
    }

    public Object retrieve(String id){
        WeakReference<Object> objectWeakReference = data.get(id);
        return objectWeakReference.get();
    }

    public static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() { return holder;}
}
