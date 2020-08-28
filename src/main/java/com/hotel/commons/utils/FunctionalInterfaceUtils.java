package com.hotel.commons.utils;

import com.hotel.commons.exception.InvalidPayloadException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  Created by saraswathy
on 2020-08-28 20:10 */

public final class FunctionalInterfaceUtils {



    private FunctionalInterfaceUtils(){}

    @FunctionalInterface
    public interface Consumer_WithExceptions<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface Consumer_WithReturn<T, R, E extends Exception> {
        R accept(T t) throws E;
    }

    @FunctionalInterface
    public interface BiConsumer_WithExceptions<T, U, E extends Exception> {
        void accept(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface BiConsumer_WithReturn<T, U, R, E extends Exception> {
        R accept(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface Function_WithExceptions<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface Supplier_WithExceptions<R, E extends Exception> {
        R get() throws E;
    }

    @FunctionalInterface
    public interface Runnable_WithExceptions<E extends Exception> {
        void run() throws E;
    }


    /**
     * .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or .forEach(rethrowConsumer(ClassNameUtil::println));
     */
    public static <T, E extends Exception> Consumer<T> rethrowConsumer(Consumer_WithExceptions<T, E> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    public static <T, U, E extends Exception> BiConsumer<T, U> rethrowBiConsumer(BiConsumer_WithExceptions<T, U, E> biConsumer) throws E {
        return (t, u) -> {
            try {
                biConsumer.accept(t, u);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    /**
     * .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName))
     */
    public static <T, R, E extends Exception> Function<T, R> rethrowFunction(Function_WithExceptions<T, R, E> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * rethrowSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),
     */
    public static <T, E extends Exception> Supplier<T> rethrowSupplier(Supplier_WithExceptions<T, E> function) throws E {
        return () -> {
            try {
                return function.get();
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }


    @FunctionalInterface
    public interface TriConsumer_WithReturn<T, U, V, R, E extends Exception> {
        R accept(T t, U u, V v) throws E;
    }

    @FunctionalInterface
    public interface QuadConsumer_WithReturn<T, U, V, W, R, E extends Exception> {
        R accept(T t, U u, V v, W w) throws E;
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     */
    public static void uncheck(Runnable_WithExceptions t) {
        try {
            t.run();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
        }
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     */
    public static <R, E extends Exception> R uncheck(Supplier_WithExceptions<R, E> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    /**
     * uncheck(Class::forName, "xxx");
     */
    public static <T, R, E extends Exception> R uncheck(Function_WithExceptions<T, R, E> function, T t) {
        try {
            return function.apply(t);
        } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
        throw (E) exception;
    }


    /**
     *  Created by saraswathy
    on 2020-08-28 19:49 */

    public static class StringUtils {


        private StringUtils() {
        }

        public static final int MAX_STRING_BYTES = 250;

        public static final int MAX_TEXT_BYTES = 50000;

        public static final int MAX_NOTES_BYTES = 50000;


        /**
         * @param str
         * @return
         */
        public static boolean isNotNullOrEmpty(String str) {
            return !PreConditions.checkNull(str) && !str.isEmpty() && str.trim().length() > 0 && !str.equals("null");
        }


        /**
         * @param inputStream
         * @return
         * @throws IOException
         */
        public static String readInputStream(InputStream inputStream) throws IOException {
            if (PreConditions.checkNull(inputStream))
                throw new RuntimeException("Invalid data, Input stream can't be null");
            try {
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1)
                    result.write(buffer, 0, length);

                return String.valueOf(result);
            } finally {
                if (!PreConditions.checkNull(inputStream))
                    inputStream.close();
            }
        }


        /**
         * @param str
         * @param arr_str
         * @return
         */
        public static boolean contains(String str, String[] arr_str) {
            List<String> str_list = Arrays.asList(arr_str);
            return str_list.contains(str);
        }

        public static boolean checkBytesLength(String value, String message, int bytesSize) {

            if (!StringUtils.isNotNullOrEmpty(value))
                return false;

            final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

            if (bytes.length > bytesSize)
                throw new InvalidPayloadException(message);

            return true;
        }

        public static String getAlphaNumericString(int n)
        {

            // chose a Character random from this String
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(n);

            for (int i = 0; i < n; i++) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int)(AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }

            return sb.toString();
        }

    }
}
