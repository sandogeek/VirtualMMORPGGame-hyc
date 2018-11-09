package com.mmorpg.mbdl.framework.common.generator;

import com.google.common.base.CaseFormat;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PacketIdTsGenerator {
    private static final Logger logger= LoggerFactory.getLogger(PacketIdTsGenerator.class);


    @Value("${dev.PACKET_ID_TS_PATH}")
    private String PACKET_ID_TS_PATH = "C:\\假桌面天下第一\\VirtualGame\\browser-client\\src\\app\\shared\\model\\packet\\PacketId.ts";
    // 客户端boot.sh pbts命令自动生成的.d.ts名称
    private static final String DTS_NAME = "bundle";
    private static final Pattern PATTERN = Pattern.compile(
            "import\\s+\\{\\s*(?<importClass>.*)\\s*\\}.*"+DTS_NAME
                    +".*?//\\s*start(?<IdAssign>.*?)//\\s*end.*"
                    +"constructor.*//\\s*start(?<mapInput>.*?)//\\s*end",Pattern.DOTALL);

    private static PacketIdTsGenerator self;
    @PostConstruct
    private void init(){
        self = this;
    }
    public static PacketIdTsGenerator getInstance(){
        return self;
    }

    /**
     * 自动生成浏览器端的PacketId.ts
     */
    public void generatePacketIdTs(){
        File packetIdTs = new File(this.PACKET_ID_TS_PATH);
        StringBuilder packetIdTsSB = new StringBuilder();
        if (!packetIdTs.exists()){
            throw new RuntimeException(this.PACKET_ID_TS_PATH+"文件不存在");
        }
        try (BufferedReader packetIdTsBR=new BufferedReader(new FileReader(packetIdTs))) {
            String line = packetIdTsBR.readLine();
            while ( line != null){
                packetIdTsSB.append(line+"\r\n");
                line = packetIdTsBR.readLine();
            }
            // logger.info("\n"+packetIdTsSB.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
        String temp = packetIdTsSB.toString();
        Matcher matcher = PATTERN.matcher(temp);
        // 需要写入三个部分：加载的类、id的赋值部分、constructor中的map.put部分，这三部分把字符串分隔成4个部分
        if (matcher.find()) {
            // 加载的类
            String importClass = matcher.group("importClass");
            String idAssign = matcher.group("IdAssign");
            String mapInput = matcher.group("mapInput");

            int importClassStart = temp.indexOf(importClass);
            String part1 = temp.substring(0,importClassStart);
            int importClassEnd = importClassStart + importClass.length();

            int idAssignStart = temp.indexOf(idAssign);
            String part2 = temp.substring(importClassEnd,idAssignStart);
            int idAssignEnd = idAssignStart + idAssign.length();

            int mapInputStart = temp.indexOf(mapInput);
            String part3 = temp.substring(idAssignEnd,mapInputStart);
            int mapInputEnd = mapInputStart + mapInput.length();

            String part4 = temp.substring(mapInputEnd);

            // if (temp.equals(part1+importClass+part2+idAssign+part3+mapInput+part4)) {
            //     // logger.info(idAssign.indexOf("\r\n")+"");
            //     logger.info("切割正确");
            // }

            StringBuilder importClassNew = new StringBuilder() ;
            StringBuilder idAssignNew = new StringBuilder();
            StringBuilder mapInputNew = new StringBuilder();

            int size = PacketIdManager.getIntance().getAbstractPackets().size();
            int count = 0;
            for (Class<? extends AbstractPacket> clazz:
                    PacketIdManager.getIntance().getAbstractPackets()) {
                count++;
                String simpleName =clazz.getSimpleName();
                String sinpleNameUpperUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,simpleName);
                Annotation protoDesc = clazz.getAnnotation(ProtoDesc.class);
                String desc = "";
                String tap = "    ";
                if (protoDesc!=null) {
                    desc = ((ProtoDesc) protoDesc).description();
                }
                // 最后一个需要做一点特殊处理
                if (count==size){
                    importClassNew.append(simpleName+" ");
                    idAssignNew.append("\r\n"+tap+"/** "+desc+"*/"+"\r\n"
                            + tap+"static readonly "+ sinpleNameUpperUnderscore
                            + ": number = " + PacketIdManager.getIntance().getPacketId(clazz)+";"+"\r\n"+tap
                    );
                    mapInputNew.append("\r\n"+tap+tap+"PacketId.put(PacketId."+sinpleNameUpperUnderscore+", "+simpleName+");"+"\r\n"+tap+tap);
                    break;
                }
                importClassNew.append(simpleName+", ");
                idAssignNew.append("\r\n"+tap+"/** "+desc+"*/"+"\r\n"
                     + tap+"static readonly "+ sinpleNameUpperUnderscore
                        + ": number = " + PacketIdManager.getIntance().getPacketId(clazz)+";"
                );
                mapInputNew.append("\r\n"+tap+tap+"PacketId.put(PacketId."+sinpleNameUpperUnderscore+", "+simpleName+");");
            }
            // logger.info(mapInputNew.toString());
            StringBuilder result = new StringBuilder();
            result.append(part1+importClassNew+part2+idAssignNew+part3+mapInputNew+part4);
            // logger.info(result.toString());
            try (BufferedWriter out=new BufferedWriter(new FileWriter(PACKET_ID_TS_PATH))) {
                out.write(result.toString());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
