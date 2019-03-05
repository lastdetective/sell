package com.imooc.utils;

import java.util.Random;

public class IdGeneratorUtil {

    /**
     * 生成唯一的主键
     * 格式：时间+随机数
     *
     * @return 生成的随机数字[100000,1000000)
     */

    public synchronized static String getUniqueId() {
        Random random = new Random();
        Integer randomInteger = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(randomInteger);
    }
}
