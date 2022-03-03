package com.lsyf.pay.logger.common.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * bean工具类
 */
public class BeanUtils {
    private static Mapper dozer = DozerBeanMapperBuilder.buildDefault();


    /**
     * 创建targetClass实例，将对象source属性映射到targetClass实例中
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convertObj(Object source, Class<T> targetClass) {
        if (source == null || targetClass == null) {
            return null;
        }
        return dozer.map(source, targetClass);
    }

    /**
     * 将对象source的所有属性值拷贝到对象target中.
     *
     * @param source
     * @param target
     */
    public static void copy(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        dozer.map(source, target);
    }

    /**
     * 创建targetClass实例列表，将对象sourceList各对象映射到targetClass列表中(属性映射)
     *
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> convertList(Collection<?> sourceList, Class<T> targetClass) {
        if (sourceList == null || targetClass == null) {
            return null;
        }

        List<T> result = new ArrayList<>();
        for (Object o : sourceList) {
            result.add(dozer.map(o, targetClass));
        }
        return result;
    }


    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMapObject(T bean) {
        if (bean == null) {
            return new HashMap<>();
        }
        return dozer.map(bean, HashMap.class);
    }

    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = beanToMapObject(bean);
        return map.entrySet().stream()
                .filter(a -> a.getValue() != null)
                .collect(Collectors.toMap(a -> a.getKey(), a -> a.getValue().toString()));
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param targetClass
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> targetClass) {
        return dozer.map(map, targetClass);
    }


}

