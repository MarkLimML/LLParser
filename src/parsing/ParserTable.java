package parsing;

import java.util.HashMap;

public class ParserTable<K1, K2, V> {

    private final HashMap<K1, HashMap<K2, V>> lltable;

    public ParserTable() {
        this.lltable = new HashMap<K1, HashMap<K2, V>>();
    }

    public V put(K1 key1, K2 key2, V value) {
        HashMap<K2, V> map;
        if (this.lltable.containsKey(key1)) {
            map = this.lltable.get(key1);
        } else {
            map = new HashMap<K2, V>();
            this.lltable.put(key1, map);
        }

        return map.put(key2, value);
    }

    public V get(K1 key1, K2 key2) {
        if (this.lltable.containsKey(key1)) {
            return this.lltable.get(key1).get(key2);
        } else {
            return null;
        }
    }

    public boolean containsKeys(K1 key1, K2 key2) {
        return this.lltable.containsKey(key1) && this.lltable.get(key1).containsKey(key2);
    }

    public void clear() {
        this.lltable.clear();
    }

}