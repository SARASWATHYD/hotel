
package com.hotel.services.datastore;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.QueryKeys;
import com.hotel.commons.utils.FunctionalInterfaceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *  Created by saraswathy
on 2020-08-28 19:47 */

public class ObjectifyHelper  extends ObjectifyService {


        public static final Logger log = Logger.getLogger(ObjectifyHelper.class.getName());

        private String cursor_string;

        protected boolean cursor;

        public String getCursor_string() {
            return this.cursor_string;
        }

        public void setCursor_string(final String cursor_string) {
            this.cursor_string = cursor_string;
        }


        /**
         * Get by id
         *
         * @param claz
         * @param id
         * @return claz type Entity
         */
        public <T> T getById(final Class<T> claz, final long id) {
            return ofy().load().type(claz).id(id).now();
        }

        /**
         * @param claz
         * @param id
         * @param <T>
         * @return claz type Entity
         */
        public <T> T getById(final Class<T> claz, final String id) {
            if (!FunctionalInterfaceUtils.StringUtils.isNotNullOrEmpty(id))
                throw new IllegalArgumentException("id can't be null or empty :: " + id);

            return ofy().load().type(claz).id(id).now();
        }


        /**
         * @param t
         * @param <T>
         * @return the list of entities saved
         */
        public <T> List<T> save(final List<T> t) {

            ofy().save().entities(t).now();
            return t;
        }


        /**
         * @param t
         * @param <T>
         * @return
         */
        public <T> T save(final T t) {
            ofy().save().entity(t).now();
            return t;
        }

        public void flushCache(){
            ofy().cache(true).clear();
        }

        /**
         * @param ids
         * @param clazz
         * @param <T>
         * @return
         */
        public <T> Collection getByIds(final Collection<T> ids, final Class clazz) {
            return ofy().load().type(clazz).ids(ids).values();
        }

        /**
         * @param ids
         * @param clazz
         * @param <T>
         * @return
         */
        public <T> Map getByIdsAndReturnMap(final List<T> ids, final Class clazz) {
            return ofy().load().type(clazz).ids(ids);
        }


        /**
         * @param claz
         * @param key
         * @param consumer
         * @param <T>
         * @return
         */
        public <T, R> R saveTx(final Class<T> claz, final String key, final FunctionalInterfaceUtils.Consumer_WithReturn<T, R, Exception> consumer) {
            return ofy().transactNew(3, () -> {
                try {
                    final T t = this.getById(claz, key);
                    return this.save(consumer.accept(t));
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }


        /**
         * @param claz
         * @param key
         * @param t1
         * @param consumer
         * @param <T>
         * @param <U>
         * @return
         */
        public <T, U> T saveTx(final Class<T> claz, final String key, final U t1, final FunctionalInterfaceUtils.BiConsumer_WithReturn<U, T, T, Exception> consumer) {
            return ofy().transact(() -> {
                try {
                    final T t = this.getById(claz, key);
                    return this.save(consumer.accept(t1, t));
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }


        /**
         * @param claz
         * @param t1
         * @param <T>
         * @return
         */
        public <T> T saveTx(final Class<T> claz, final T t1) {
            return ofy().transact(() -> {
                this.save(t1);
                return t1;
            });
        }

        /**
         * @param <T>
         * @return
         */
        public <T, R> R processDataInTransaction(final T obj, final FunctionalInterfaceUtils.Consumer_WithReturn<T, R, Exception> consumer) {
            return ofy().transactNew(3, () -> {
                try {

                    return consumer.accept(obj);
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }

        /**

         * @return
         */
        public <R> R processWithTXN(FunctionalInterfaceUtils.Supplier_WithExceptions<R,Exception> supplier_withExceptions){
            return ofy().transactNew(3, () -> {
                try {
                    return supplier_withExceptions.get();
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }


        /**
         * @param <T>
         * @return
         */
        public <T> void  processWithTXN(final T obj, final FunctionalInterfaceUtils.Consumer_WithExceptions<T,Exception> consumer) {
            ofy().transactNew(3, () -> {
                try {
                    consumer.accept(obj);
                }
                catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                return null;
            });
        }


        public <T> T processWithTXN(T accept) {
            ofy().transactNew(3, () -> {
                try {
                    return accept;
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
            return null;
        }

        public <T,U,V,R> R  processWithTXN(final T t, U u, V v,  final FunctionalInterfaceUtils.TriConsumer_WithReturn<T, U, V, R, Exception> consumer) {
            return ofy().transactNew(3, () -> {
                try {
                    return consumer.accept(t,u,v);
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }

        public <T,U> void processWithTXN(final T t, U u,  final FunctionalInterfaceUtils.BiConsumer_WithExceptions<T, U, Exception> consumer) {
            ofy().transactNew(3, () -> {
                try {
                    consumer.accept(t,u);
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                return t;
            });
        }

        /**
         * @param <T>
         * @return
         */
        public <T, U, R> R biConsumerThroughTransaction(final T t1,final U u1,  final FunctionalInterfaceUtils.BiConsumer_WithReturn<T,U, R, Exception> consumer) {
            return ofy().transactNew(3, () -> {
                try {

                    return consumer.accept(t1,u1);
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }

        /**
         * @param <T>
         * @return
         */
        public <T, U> void biConsumerThroughTransaction1(final T t1,final U u1,  final FunctionalInterfaceUtils.BiConsumer_WithExceptions<T,U, Exception> consumer) {
            ofy().transactNew(3, () -> {
                try {

                    consumer.accept(t1,u1);
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                return "Success";
            });
        }


        /**
         * @param <T>
         * @return
         */
        public <T> void  consumerThroughTransaction(final T t1,  final FunctionalInterfaceUtils.Consumer_WithExceptions<T, Exception> consumer) {
            ofy().transactNew(3, () -> {
                try {
                    consumer.accept(t1);
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                return "Success";
            });
        }

        /**
         * @param t
         * @param <T>
         */
        public <T> void saveTx(final T t) {

            ofy().transactNew(3, () -> {
                this.save(t);
                return t;
            });
        }


        /**
         * @param claz
         * @param key
         * @param t1
         * @param brand_id
         * @param consumer
         * @param <T>
         * @param <U>
         * @return
         */
        public <T, U> T saveTx(final Class<T> claz, final String key, final U t1, final String brand_id, final FunctionalInterfaceUtils.TriConsumer_WithReturn<U, String, T, T, Exception> consumer) {

            return ofy().transactNew(3, () -> {
                try {
                    final T t = this.getById(claz, key);
                    return this.save(consumer.accept(t1, brand_id, t));
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            });

        }


        public <T> void delete(final Class<T> tClass, final String key) {

            ofy().delete().type(tClass).id(key);

        }

        public <T,U> void delete(final Class<T> tClass, final List<U> keys) {

            ofy().delete().type(tClass).ids(keys);

        }

        public <T> void delete(final List<T> t) {
            ofy().delete().entities(t).now();
        }

        public <T> void delete(final T t) {
            ofy().delete().entity(t).now();
        }


        /**
         * @param query
         * @param <T>
         * @return
         */
        protected <T> List<T> getList(final Query<T> query) {



            if (this.cursor)
                return this.getListWithCursor(query.iterator());

            return this.getListWithOutCursor(query);
        }


        /**
         * @param queryResultIterator
         * @param <T>
         * @return
         */
        private <T> List getListWithCursor(final QueryResultIterator<T> queryResultIterator) {
            final List<T> dataList = new ArrayList<>();
            while (queryResultIterator.hasNext())
                dataList.add(queryResultIterator.next());

            this.setCursor_string(queryResultIterator.getCursor().toWebSafeString());
            return dataList;
        }


        /**
         * @param query
         * @param <T>
         * @return
         */
        private <T> List<T> getListWithOutCursor(final Query<T> query) {
            return query.list();

        }


        /**
         * @param query
         * @param <T>
         * @return
         */
        public <T> List<String> getKeysOnly(final Query<T> query) {
            if (this.cursor)
                return this.getKeysWithCursor(query);

            return this.getKeysWithOutCursor(query);
        }

        /**
         * @param query
         * @param <T>
         * @return
         */
        public <T> List<Long> getIdsOnly(final Query<T> query) {
            log.info("query :: "+query);
            if (this.cursor)
                return this.getIdsWithCursor(query);

            return this.getIdsWithOutCursor(query);
        }



        /**
         * @param query
         * @param <T>
         * @return
         */
        private <T> List<String> getKeysWithCursor(final Query<T> query) {

            log.info("query :: "+query);
            log.info("before keys fetch");
            final QueryKeys<T> queryKeys = query.keys();
            final QueryResultIterator<Key<T>> queryResultIterator = queryKeys.iterator();
            final List<String> dataList = new ArrayList<>();

            while (queryResultIterator.hasNext())
                dataList.add(queryResultIterator.next().getName());
            log.info("after keys fetch :: ");
            this.setCursor_string(queryResultIterator.getCursor().toWebSafeString());
            return dataList;
        }

        /**
         * @param query
         * @param <T>
         * @return
         */
        private <T> List<Long> getIdsWithCursor(final Query<T> query) {

            final QueryKeys<T> queryKeys = query.keys();
            final QueryResultIterator<Key<T>> queryResultIterator = queryKeys.iterator();
            final List<Long> dataList = new ArrayList<>();

            while (queryResultIterator.hasNext())
                dataList.add(queryResultIterator.next().getId());

            this.setCursor_string(queryResultIterator.getCursor().toWebSafeString());
            return dataList;
        }



        /**
         * @param query
         * @param <T>
         * @return
         */
        private <T> List<String> getKeysWithOutCursor(final Query<T> query) {
            final QueryKeys<T> queryKeys = query.keys();
            final List<String> dataList = new ArrayList<>();
            queryKeys.forEach(k -> {
                dataList.add(k.getName());
            });

            return dataList;

        }

        /**
         * @param query
         * @param <T>
         * @return
         */
        private <T> List<Long> getIdsWithOutCursor(final Query<T> query) {
            final QueryKeys<T> queryKeys = query.keys();
            final List<Long> dataList = new ArrayList<>();
            queryKeys.forEach(k -> {
                dataList.add(k.getId());
            });

            return dataList;

        }


        /**
         * @param query
         * @param <T>
         * @return
         */
        protected <T> T asSingle(final Query<T> query) {
            return query.first().now();
        }


        public <T> void deleteTx(Class<T>  tClass, String key){
            ofy().transact(() -> {
                try{
                    delete(tClass, key);
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
            });
        }

    }


