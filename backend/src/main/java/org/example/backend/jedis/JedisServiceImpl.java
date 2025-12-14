package org.example.backend.jedis;//package com.example.dormitoryselection.jedis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//
//import java.lang.reflect.Field;
//import java.util.Map;
//import java.util.Set;
//import java.util.logging.Logger;
//
//@Service
//public class JedisServiceImpl implements JedisService {
//    @Autowired
//    private JedisUtil jedisUtil;
//
//    private static final Logger logger = Logger.getLogger(JedisServiceImpl.class.getName());
//
//    public boolean setString(String key, String value) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            jedis.set(key, value);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    public boolean setString(String key, String value, long seconds) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            jedis.set(key, value);
//            jedis.expire(key, seconds);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public boolean setString(int index, String key, String value) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            jedis.set(key, value);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    public boolean setString(int index, String key, String value, long seconds) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            jedis.set(key, value);
//            jedis.expire(key, seconds);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public <V> boolean setHash(String key, V value) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            Field[] fields = value.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                jedis.hset(key, field.getName(), field.get(value).toString());
//            }
//            return true;
//        } catch (IllegalAccessException e) {
//            logger.warning("setHash反射获取属性值失败");
//            return false;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public <V> boolean setHash(String key, V value, long seconds) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            Field[] fields = value.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                jedis.hset(key, field.getName(), field.get(value).toString());
//                jedis.expire(key, seconds);
//            }
//            return true;
//        } catch (IllegalAccessException e) {
//            logger.warning("setHash反射获取属性值失败");
//            return false;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public <V> boolean setHash(int index, String key, V value) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            Field[] fields = value.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                jedis.hset(key, field.getName(), field.get(value).toString());
//            }
//            return true;
//        } catch (IllegalAccessException e) {
//            logger.warning("setHash反射获取属性值失败");
//            return false;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public <V> boolean setHash(int index, String key, V value, long seconds) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            Field[] fields = value.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                jedis.hset(key, field.getName(), field.get(value).toString());
//                jedis.expire(key, seconds);
//            }
//            return true;
//        } catch (IllegalAccessException e) {
//            logger.warning("setHash反射获取属性值失败");
//            return false;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    public String getString(String key) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            return jedis.get(key);
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return null;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public String getString(int index, String key) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            return jedis.get(key);
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return null;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public <V> V getHash(String key, Class<V> clazz) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            Map<String, String> map = jedis.hgetAll(key);
//            V v = clazz.getDeclaredConstructor().newInstance();
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                field.set(v, map.get(field.getName()));
//            }
//            return v;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return null;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public <V> V getHash(int index, String key, Class<V> clazz) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            Map<String, String> map = jedis.hgetAll(key);
//            V v = clazz.getDeclaredConstructor().newInstance();
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                field.set(v, map.get(field.getName()));
//            }
//            return v;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return null;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public boolean delete(String key) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            jedis.del(key);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public boolean delete(int index, String key) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            jedis.del(key);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public boolean exists(String key) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            return jedis.exists(key);
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public boolean exists(int index, String key) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            return jedis.exists(key);
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public boolean expire(String key, int seconds) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            jedis.expire(key, seconds);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public boolean expire(int index, String key, int seconds) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            jedis.expire(key, seconds);
//            return true;
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return false;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public Set<String> getKeys(String pattern) {
//        Jedis jedis = jedisUtil.getJedis();
//        try {
//            return jedis.keys(pattern);
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return null;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//
//    @Override
//    public Set<String> getKeys(int index, String pattern) {
//        Jedis jedis = jedisUtil.getJedis(index);
//        try {
//            return jedis.keys(pattern);
//        } catch (Exception e) {
//            logger.warning(e.getMessage());
//            return null;
//        } finally {
//            jedisUtil.close(jedis);
//        }
//    }
//}
