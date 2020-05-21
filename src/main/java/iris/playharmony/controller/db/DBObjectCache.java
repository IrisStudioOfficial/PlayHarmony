package iris.playharmony.controller.db;

import java.util.Map;
import java.util.WeakHashMap;

public class DBObjectCache {

    private final Map<String, Integer> nameToIndexMap;
    private final Map<Integer, String> indexToNameMap;

    public DBObjectCache() {
        nameToIndexMap = new WeakHashMap<>();
        indexToNameMap = new WeakHashMap<>();
    }

    public void put(String name, int index) {
        nameToIndexMap.put(name, index);
        indexToNameMap.put(index, name);
    }

    public void put(int index, String name) {
        put(name, index);
    }

    public String getName(int index) {
        return indexToNameMap.get(index);
    }

    public Integer getIndex(String name) {
        return nameToIndexMap.get(name);
    }
}
