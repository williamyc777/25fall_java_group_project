package org.example.backend.jedis;

import java.util.Set;

public interface JedisService {
    boolean setString(String key, String value);

    boolean setString(String key, String value, long seconds);

    boolean setString(int index, String key, String value);

    boolean setString(int index, String key, String value, long seconds);

    <V> boolean setHash(String key, V value);

    <V> boolean setHash(String key, V value, long seconds);

    <V> boolean setHash(int index, String key, V value);

    <V> boolean setHash(int index, String key, V value, long seconds);

    String getString(String key);

    String getString(int index, String key);

    <V> V getHash(String key, Class<V> clazz);

    <V> V getHash(int index, String key, Class<V> clazz);

    boolean delete(String key);

    boolean delete(int index, String key);

    boolean exists(String key);

    boolean exists(int index, String key);

    boolean expire(String key, int seconds);

    boolean expire(int index, String key, int seconds);

    Set<String> getKeys(String pattern);

    Set<String> getKeys(int index, String pattern);
}
