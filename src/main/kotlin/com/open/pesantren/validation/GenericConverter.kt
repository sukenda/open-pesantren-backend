package com.open.pesantren.validation

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import java.util.stream.Collectors

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
class GenericConverter {

    companion object {

        fun <T, E> mapper(source: T, targetClass: Class<E>): E {
            val modelMapper = ModelMapper()
            modelMapper.configuration.matchingStrategy = MatchingStrategies.STRICT
            return modelMapper.map(source, targetClass)
        }

        fun <S, T> mapperList(sources: List<S>, targetClass: Class<T>): MutableList<Any> {
            val modelMapper = ModelMapper()
            modelMapper.configuration.matchingStrategy = MatchingStrategies.STRICT
            return sources
                    .stream()
                    .map<Any> { element: S -> modelMapper.map(element, targetClass) }
                    .collect(Collectors.toList())
        }
    }

}