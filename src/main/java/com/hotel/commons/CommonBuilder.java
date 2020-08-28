package com.hotel.commons;

import com.hotel.commons.utils.FunctionalInterfaceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *  Created by saraswathy
on 2020-08-28 20:09
 */

public class CommonBuilder<T> {

    private final Supplier<T> instantiator;

    private final T t;

    private List<Consumer<T>> native_instance_modifiers = new ArrayList<>();

    private List<FunctionalInterfaceUtils.Consumer_WithExceptions<T,Exception>> instance_modifiers = new ArrayList<>();

    public CommonBuilder(Supplier<T> instantiator) {

        this.instantiator = instantiator;
        this.t =instantiator.get();
    }

    public static <T> CommonBuilder<T> of(Supplier<T> instantiator) {

        return new CommonBuilder<T>(instantiator);
    }

    public CommonBuilder(T t) {
        this.t = t;
        instantiator = null;
    }

    public static <T> CommonBuilder<T> of(T t) {

        return new CommonBuilder<T>(t);
    }

    public <U> CommonBuilder<T> native_with(BiConsumer<T, U> consumer, U value){
        Consumer<T> c = instance ->{
            consumer.accept(instance, value);
        };
        native_instance_modifiers.add(c);
        return this;
    }

    public <U> CommonBuilder<T> with(FunctionalInterfaceUtils.BiConsumer_WithExceptions<T, U,Exception> consumer, U value){
        FunctionalInterfaceUtils.Consumer_WithExceptions<T,Exception> c = instance ->{
            consumer.accept(instance, value);
        };
        instance_modifiers.add(c);
        return this;
    }
    public  CommonBuilder<T> with(FunctionalInterfaceUtils.Consumer_WithExceptions<T, Exception> consumer){
        FunctionalInterfaceUtils.Consumer_WithExceptions<T,Exception> c = instance ->{
            consumer.accept(instance);
        };
        instance_modifiers.add(c);
        return this;
    }


    public T build() throws Exception{
        T value = t;
        instance_modifiers.forEach(FunctionalInterfaceUtils.rethrowConsumer(modifier -> modifier.accept(value)));
        instance_modifiers.clear();
        return value;
    }

    public T native_build(){
        T value = instantiator.get();
        native_instance_modifiers.forEach(modifier -> modifier.accept(value));
        native_instance_modifiers.clear();
        return value;
    }
}
