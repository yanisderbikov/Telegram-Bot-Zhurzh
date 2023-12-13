package com.zhurzh.commonutils.utils;

import org.hashids.Hashids;

public class CryptoTool { // генерация хешей. Соль хранится в application.properties
    private final Hashids hashids;

    public CryptoTool(String salt) {
        var minHashLength = 10; // минимальная длина хеша
	this.hashids = new Hashids(salt, minHashLength);
    }

    public String hashOf(Long value) {
        // создает хеш
        return hashids.encode(value);
    }

    public Long idOf(String value) {
        // создает из хеша в значение
        long[] res = hashids.decode(value);
        if (res != null && res.length > 0) {
            return res[0];
	}
        return null;
    }
}
