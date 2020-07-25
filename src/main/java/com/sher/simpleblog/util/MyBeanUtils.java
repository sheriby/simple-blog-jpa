package com.sher.simpleblog.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title MyBeanUtils
 * @Package com.sher.simpleblog.util
 * @Description A custom bean copy util
 * @Author sher
 * @Date 2020/07/25 11:57 AM
 */
public class MyBeanUtils {

    public static String[] getNullPropertyName(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> names = new ArrayList<>();

        for (PropertyDescriptor pd : pds) {
            String name = pd.getName();
            if (beanWrapper.getPropertyValue(name) == null) {
                names.add(name);
            }
        }

        return names.toArray(new String[0]);
    }
}
