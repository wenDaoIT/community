package top.tom666.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.tom666.community.util.RedisKeyUtil;
import top.tom666.community.util.exception.BizException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: liujisen
 * @date： 2022-09-24
 */
@Service
public class DataService {

    @Autowired
    private RedisTemplate redisTemplate;
    private SimpleDateFormat df =new SimpleDateFormat("yyyyMMdd");

    /**
     * @param ip 将指定IP计入UV
     */
    public void recordUV(String ip){
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey,ip);
    }

    public long calculateUV(Date start,Date end){
        if (start == null || end ==null){
            throw new BizException("参数不能为空");
        }
        //整理
        List<String> keyList = new ArrayList<String>();
        Calendar instance = Calendar.getInstance();
        instance.setTime(start);
        while (!instance.getTime().after(end)){
            String key  = RedisKeyUtil.getUVKey(df.format(instance.getTime()));
            keyList.add(key);
            instance.add(Calendar.DATE , 1);
        }

        String redisKey = RedisKeyUtil.getUVKey(df.format(start),df.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey,keyList.toArray());

        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    public void recordDAU(int userId){
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey,userId,true);
    }

    //统计指定范围中的DAU
    public long calcuteDAU(Date start, Date end){
        if (null == start || end ==null){
            throw new BizException("参数不能为空");
        }
        //整理
        List<byte[]> keyList = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        instance.setTime(start);
        while (!instance.getTime().after(end)){
            String key  = RedisKeyUtil.getDAUKey(df.format(instance.getTime()));
            keyList.add(key.getBytes());
            instance.add(Calendar.DATE , 1);
        }

        //or 运算
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
                    connection.bitOp(RedisStringCommands.BitOperation.OR,
                            redisKey.getBytes(),
                            keyList.toArray(new byte[0][0]));
                    return connection.bitCount(redisKey.getBytes());
            }
        });
    }






}
