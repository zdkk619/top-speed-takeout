package com.zdkk.speed.service;

import com.zdkk.speed.annotation.AutoFill;
import com.zdkk.speed.enumeration.OperationType;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class AutoFillService {

    @AutoFill(value = OperationType.INSERT)
    public <T> void insert(T object, Consumer<T> mapperAction) {
        mapperAction.accept(object);
    }


    @AutoFill(value = OperationType.UPDATE)
    public <T> void update(T object, Consumer<T> mapperAction) {
        mapperAction.accept(object);
    }
}
