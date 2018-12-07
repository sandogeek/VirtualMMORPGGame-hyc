package com.mmorpg.mbdl.framework.common.generator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Id生成器工厂
 * @author sando
 */
@Component
public class IdGeneratorFactory {
    private static final Logger logger = LoggerFactory.getLogger(IdGeneratorFactory.class);
    private static final long maxServerId = IdGenerator.maxServerId;
    private static final long maxDatacenterId = IdGenerator.maxDatacenterId;
    /** 开始时间截 TODO 以下字段变为静态字段并使用@Prop直接注入 */
    @Value("${server.config.beginOn}")
    private String beginOnString = "2018/10/30";
    private long beginOn ;
    /** 数据中心ID(0~31) */
    @Value("${server.config.dataCenterId}")
    private long datacenterId = 1;
    /** 服务器ID(0~63) */
    @Value("${server.config.serverId}")
    private long serverId = 1;
    /** 用来生成玩家Id的实例的数量 */
    private int maxPlayerInstance = 6;
    /** 玩家IdGenerator实例数组下标 */
    private AtomicInteger arrayIndexOfPlayer = new AtomicInteger(-1);
    /** 用来生成对象Id的实例的数量 */
    private int maxObjectInstance = 10;
    /** 对象IdGenerator实例数组下标 */
    private AtomicInteger arrayIndexOfObject = new AtomicInteger(-1);
    private IdGenerator[] playerInstanceIdGenerators = new IdGenerator[maxPlayerInstance];
    private IdGenerator[] ObjectInstanceIdGenerators = new IdGenerator[maxObjectInstance];

    private static IdGeneratorFactory self;
    public static IdGeneratorFactory getIntance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self = this;
        String[] date = StringUtils.split(this.beginOnString,'/');
        Calendar calendar = new Calendar.Builder().setDate(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2])).build();
        this.beginOn = calendar.getTimeInMillis();
        if (serverId > maxServerId || serverId < 0) {
            throw new IllegalArgumentException(String.format("服务器Id不能大于%d或小于0", maxServerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("数据中心Id不能大于%d或小于0", maxDatacenterId));
        }
        if (this.beginOn >= System.currentTimeMillis()) {
            throw new IllegalArgumentException("当前时间未到设定的运营时间: " + DateFormat.getDateInstance().format(new Date(this.beginOn)) + "无法生成id");
        }
        for (int i = 0; i < playerInstanceIdGenerators.length; i++) {
            playerInstanceIdGenerators[i] = new IdGenerator(i,datacenterId,serverId,beginOn);
        }
        for (int i = 0; i < ObjectInstanceIdGenerators.length; i++) {
            // +6保证生成器id不同
            ObjectInstanceIdGenerators[i] = new IdGenerator(i+6,datacenterId,serverId,beginOn);
        }
    }

    /**
     * 获取玩家IdGenerator
     * @return IdGenerator
     */
    public IdGenerator getPlayerIdGenerator(){
        return playerInstanceIdGenerators[arrayIndexOfPlayer.updateAndGet(n -> (n+1)%maxPlayerInstance)];
    }

    /**
     * 获取对象IdGenerator
     * @return IdGenerator
     */
    public IdGenerator getObjectIdGenerator(){
        return ObjectInstanceIdGenerators[arrayIndexOfObject.updateAndGet(n -> (n+1)%maxObjectInstance)];
    }
}
