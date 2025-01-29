package com.zhf.util;

public class SnowFlake {
    /**
     * 起始的时间戳
     * long类型表示的毫秒值，转换成date为2020-01-01 00:00:00
     * 可以根据自己的系统定义
     */
    private final static long START_STMP = 1577808000000L;
 
    /** 序号部分占用12位二进制数，最大可以在一个毫秒内产生4096个序号，2^12 = 4096 */
    private final static long SEQUENCE_BIT = 12;
    /** 机器标识用5位二进制表示，可以表示32台机器，2^5 = 32 */
    private final static long MACHINE_BIT = 5;
    /** 数据中心（机房）用5位二进制表示，可以表示32个中心，2^5 = 32 */
    private final static long DATACENTER_BIT = 5;
 
    /** 数据中心十进制最大值：31 */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    /** 机器编号十进制最大值：31 */
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    /** 每个毫秒内序号号十进制最大值：4095 */
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);
 
    /** 机器码移位值，最右边是12位序号，因此机器码移位数：12 */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    /** 数据中心（机房）移位值：12 + 5 = 17 */
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    /** 时间戳移位值：17 + 5 = 22 */
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;
 
    /** 数据中心（机房）Id */
    private long datacenterId;
    /** 机器Id */
    private long machineId;
    /** 序号，初始值为0 */
    private long sequence = 0L;
    /** 上一次生成的时间戳，初始为-1 */
    private long lastStmp = -1L;
 
    /**
     * 构造函数，从外部传入数据中心id和机房id
     *
     * @param datacenterId
     * @param machineId
     */
    public SnowFlake(long datacenterId, long machineId) {
        // 数据中心（机房）id不能小于0，不能大于31
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        // 机器id不能小于0，不能大于31
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }
 
    /**
     * 核心方法，产生唯一Id
     *
     * @return
     */
    public synchronized long nextId() {
        // 获取当前毫秒值
        long currStmp = getNewstmp();
        // 如果当前毫秒值小于上次生成的毫秒值，说明产生了时钟回拨，此时抛出异常
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
 
        // 当前毫秒值和上次生成的毫秒值相等，说明在同一毫秒内生产了不止一个唯一id
        if (currStmp == lastStmp) {
            // 相同毫秒内，序列号自增,MAX_SEQUENCE=4095,1111 1111 1111
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                // 等待，获取到下一个毫秒值
                currStmp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0，重新计数
            sequence = 0L;
        }
        // 更新上次生成的时间戳
        lastStmp = currStmp;
 
        // 执行二进制拼接
        // 当前时间戳减去起始时间戳后左移22位
        // 数据中心（机房）左移17位
        // 机器编号左移12位
        // 序号部分直接拼接在最后
        return (currStmp - START_STMP) << TIMESTMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }
 
    /**
     * 获取下一毫秒值
     *
     * @return
     */
    private long getNextMill() {
        // 获取当前毫秒
        long mill = getNewstmp();
        // 如果当前毫秒小于等于上次时间戳则一直循环，
        // 直到当前毫秒值大于上次时间戳的毫秒值
        while (mill <= lastStmp) {
            // 重新获取当前毫秒值并赋值
            mill = getNewstmp();
        }
        // 返回下一个毫秒值
        return mill;
    }
 
    /**
     * 获取当前服务器毫秒值
     *
     * @return
     */
    private long getNewstmp() {
        return System.currentTimeMillis();
    }
 
    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(2, 3);
 
        for (int i = 0; i < 1000; i++) {
            System.out.println(snowFlake.nextId());
        }
    }
}