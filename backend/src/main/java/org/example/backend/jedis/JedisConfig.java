package org.example.backend.jedis;

//@Configuration
public class JedisConfig {

//    private final Logger logger = Logger.getLogger(JedisConfig.class.getName());
//
//    @Value("${spring.data.redis.host}")
//    private String host;
//
//    @Value("${spring.data.redis.ports}")
//    private int[] ports;
//
//    @Value("${spring.data.redis.timeout}")
//    private int timeout;
//
//    @Value("${spring.data.redis.jedis.pool.max-active}")
//    private int maxActive;
//
//    @Value("${spring.data.redis.jedis.pool.max-idle}")
//    private int maxIdle;
//
//    @Value("${spring.data.redis.jedis.pool.min-idle}")
//    private int minIdle;

//    @Bean
//    public List<JedisPool> jedisPools(){
//        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMinIdle(minIdle);
//        jedisPoolConfig.setMaxTotal(maxActive);
//        List<JedisPool> jedisPools= new ArrayList<>();
//
//        for (int port : ports) {
//            JedisPool jedisPool=new JedisPool(jedisPoolConfig,host,port,timeout);
//            logger.info("初始化JedisPool，host:"+host+",port:"+port);
//            jedisPools.add(jedisPool);
//        }
//
//        logger.info("初始化JedisPool集合完成");
//
//        return jedisPools;
//    }
}
