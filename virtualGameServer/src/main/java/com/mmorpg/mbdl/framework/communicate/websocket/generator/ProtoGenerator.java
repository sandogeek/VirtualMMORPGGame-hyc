package com.mmorpg.mbdl.framework.communicate.websocket.generator;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static com.mmorpg.mbdl.framework.utils.FileUtils.createFile;

public class ProtoGenerator {
    // TODO 把路径配置放到dev.properties中
    /**
     * proto文件存放位置
     */
    public static final String PROTO_PATH = "C:\\假桌面天下第一\\VirtualGame\\browser-client\\src\\assets\\proto";
    private static Set<Class<?>> typesCache = new HashSet<>();
    private static Set<Class<?>> enumCache = new HashSet<>();


    /**
     * 生成abstractPacket对应的.proto文件
     */
    public static void generateProto(AbstractPacket abstractPacket){
        StringBuilder result = new StringBuilder();
        String code = ProtobufIDLGenerator.getIDL(abstractPacket.getClass(),typesCache,enumCache,true);
        Annotation protoDesc = abstractPacket.getClass().getAnnotation(ProtoDesc.class);
        if (protoDesc!=null){
            String desc = ((ProtoDesc) protoDesc).description();

            File file = new File(String.format("%s\\%s-%s-%s.proto",PROTO_PATH,
                    abstractPacket.getPacketId(),abstractPacket.getClass().getSimpleName(),desc));
            try {
                createFile(file);
                FileWriter fw = new FileWriter(file);
                fw.write(code);
                fw.flush();
                fw.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException(String.format("AbstractPacket类[%s]没有ProtoDesc注解",abstractPacket.getClass().getSimpleName()));
        }
        // result.append("\n"+code);
        // logger.info(result.toString());
    }
}
