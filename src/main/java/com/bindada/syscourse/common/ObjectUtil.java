package com.bindada.syscourse.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @desc 是他转换工具类
 */
@Slf4j
public class ObjectUtil {

    /**
     * 包装equales 方法
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return
     */
    public static boolean thisEquals(Object o1, Object o2) {
        return (o1 == null) ? (o2 == null) : (o2 != null && o1.equals(o2));
    }

    /**
     * 泛型方法，将一个对象转化为另一个对象
     *
     * @param <S>              原始对象
     * @param <T>              目标对象
     * @param targetClass      目标对象的类型
     * @param ignoreProperties 不进行复制的属性
     * @return
     */
    public static <S, T> T source2Target(S source, Class<T> targetClass, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        } catch (Exception e) {
            log.error("将一个对象转化为另一个对象出现异常", e);
            return null;
        }
    }

    /**
     * 将对象转成Map
     *
     * @param object
     * @return
     */
    public static Map<String, String> convertBeanToMap(Object object) {
        if (object == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    String value = null;
                    if (getter.invoke(object) != null) {
                        value = getter.invoke(object).toString();
                    }
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 泛型方法，将一个对象集合转化为另一个对象集合
     *
     * @param <S>              原始对象
     * @param <T>              目标对象
     * @param sourceList       原始对象集合
     * @param targetClass      目标对象的类型
     * @param ignoreProperties 不进行复制的属性
     * @return
     */

    public static <S, T> List<T> sourceList2TargetList(List<S> sourceList, Class<T> targetClass,
                                                       String... ignoreProperties) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<T> targetList = new ArrayList<>();
        try {
            for (S each : sourceList) {
                T target = targetClass.newInstance();
                BeanUtils.copyProperties(each, target, ignoreProperties);
                targetList.add(target);
            }
        } catch (Exception e) {
            log.error("将一个对象集合转化为另一个对象集合出现异常", e);
        }
        return targetList;
    }


}
