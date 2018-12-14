package com.mmorpg.mbdl.framework.common.utils;

import com.mmorpg.mbdl.framework.common.generator.IdGenerator;

/**
 * 通用工具
 *
 * @author Sando Geek
 * @since v1.0 2018/12/14
 **/
public class CommonUtils {
    /**
     * 根据雪花算法生成的id获取serverToken
     * @param id 雪花算法生成的id
     * @return serverToken标识所在的服务器
     */
    public static int getSeverTokenById(long id){
        // token所占位数
        long tokenBitLength = IdGenerator.getDatacenterIdBits() + IdGenerator.getServerIdBits();
        long tokenBitMax = ~(-1L << tokenBitLength);
        return (int)((id >> IdGenerator.getSequenceBits()) & tokenBitMax);
    }
}
