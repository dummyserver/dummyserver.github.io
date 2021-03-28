package com.github.ahenteti.dummyserver.service.impl.utils;

import java.lang.reflect.Array;
import java.util.List;

public class ArrayUtils {

    public static <T> T[] toArray(List<T> list, Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, list.size());
        return list.toArray(array);
    }
}
