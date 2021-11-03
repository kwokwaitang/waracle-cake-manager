package com.waracle.cake_manager.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.List;
import java.util.stream.Collectors;

public class GeneralUtils {

    private GeneralUtils() {
    }

    /**
     * Generic conversion of a list of source objects of type S to a list of type T
     * <p>
     * See https://www.baeldung.com/java-modelmapper-lists
     *
     * @param sourceObjects List of source objects
     * @param targetClass   The target class
     * @param <S>           Source objects of type S
     * @param <T>           Target class
     * @return List of objects of type T
     */
    public static <S, T> List<T> mapList(List<S> sourceObjects, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // To allow ModelMapper to compare private fields in the mapping classes (objects)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return sourceObjects
                .stream()
                .map(sourceObject -> modelMapper.map(sourceObject, targetClass))
                .collect(Collectors.toList());
    }
}
