package com.example.anuragsharma.bonding;

import java.io.Serializable;

/**
 * Created by anuragsharma on 16/2/17.
 */

public class ChildKey implements Serializable {
    private String mKey;

    public ChildKey(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }
}
