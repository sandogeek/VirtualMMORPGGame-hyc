package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.exposed.IExcelFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * excel读取监听器
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class ExcelListener extends AnalysisEventListener<ArrayList<String>> {
    private static Logger logger = LoggerFactory.getLogger(ExcelListener.class);
    /**
     * ObjectMapper线程安全的，设为静态，避免频繁创建
     */
    private static ObjectMapper mapper;
    static  {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private ConfigurableListableBeanFactory beanFactory;
    private StaticResDefinition staticResDefinition;
    private IExcelFormat excelFormat;
    private String idFieldJsonName;


    private ImmutableMap.Builder key2ResourceBuilder = ImmutableMap.builder();

    private boolean foundFieldNameLine= false;
    private boolean tableHeadHandleOver = false;
    private Integer idFieldIndex;
    private Map<Integer,String> index2JsonPropertyName = new HashMap<>(16);
    private Map<Integer,Class<?>> index2FieldType = new HashMap<>(16);
    @Override
    @SuppressWarnings("unchecked")
    public void invoke(ArrayList<String> list, AnalysisContext context) {
        if (beanFactory==null){
            List contextCustom = (List) context.getCustom();
            beanFactory = (ConfigurableListableBeanFactory)contextCustom.get(0);
            staticResDefinition = (StaticResDefinition)contextCustom.get(1);
            try {
                excelFormat = beanFactory.getBean(IExcelFormat.class);
            } catch (Exception e) {
                excelFormat = new ExcelFormat();
            }
            idFieldJsonName = Optional.ofNullable(staticResDefinition.getIdField().getAnnotation(JsonProperty.class))
                    .map(JsonProperty::value)
                    .orElseGet(() -> staticResDefinition.getIdField().getName());
        }
        if (!tableHeadHandleOver){
            if (excelFormat.isFieldNamesRow(list)){
                foundFieldNameLine = true;
                for (int i = excelFormat.ignoreFirstNColumn(); i < list.size(); i++) {
                    String content = list.get(i);
                    if (content !=null){
                        index2JsonPropertyName.put(i, content);
                        if (idFieldIndex==null && content.equals(idFieldJsonName) ) {
                            idFieldIndex = i;
                        }
                    }

                }
            }
            if (excelFormat.isTableHeadLastLine(list)){
                if (!foundFieldNameLine){
                    throw new RuntimeException(String.format("资源文件[%s]格式有误，表头不包含字段名称行",staticResDefinition.getFullFileName()));
                }
                tableHeadHandleOver = true;
            }

        }else {
            if (index2FieldType.size()==0){
                initIndex2FieldType();
            }
            // 跳过id字段为空的行
            if (list.get(idFieldIndex)==null){
                return;
            }
            // vClass实例的json数据形式
            ArrayList<String> vClassObjectInJsonArray=new ArrayList<>(16);
            index2JsonPropertyName.keySet().stream().forEach(integer -> {
                String content = list.get(integer);
                Class<?> fieldType = index2FieldType.get(integer);
                if (fieldType.isEnum()||fieldType==String.class){
                    // 枚举或者字符串前后要加引号
                    content = "\""+content+"\"";
                }
                vClassObjectInJsonArray.add("\""+index2JsonPropertyName.get(integer)+"\":"+content);
            });
            String vClassObjectInJson ="{" + String.join("," ,vClassObjectInJsonArray) + "}";
            try {
                Object resource = mapper.readValue(vClassObjectInJson, staticResDefinition.getvClass());
                key2ResourceBuilder.put(staticResDefinition.getIdField().get(resource),resource);
            } catch (IOException e) {
                throw new RuntimeException(String.format("生成资源类[%s]实例时发生IO异常,通常是由于单元格json格式错误或与类字段类型不对应",staticResDefinition.getvClass().getSimpleName()),e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            // logger.debug("当前行：{},内容：{}",context.getCurrentRowNum()+1,list);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (foundFieldNameLine && !tableHeadHandleOver){
            throw new RuntimeException(String.format("资源文件[%s]表头处理失败，未找到表头结束行",staticResDefinition.getFullFileName()));
        }else if (!tableHeadHandleOver) {
            throw new RuntimeException(String.format("资源文件[%s]表头处理失败，未找到表头字段名称行和表头结束行",staticResDefinition.getFullFileName()));
        }
        ImmutableMap key2Resource = key2ResourceBuilder.build();
        staticResDefinition.getStaticRes().setKey2Resource(key2Resource);
        String resBeanName = StringUtils.uncapitalize(staticResDefinition.getiStaticRes().getClass().getSimpleName());
        beanFactory.registerSingleton(resBeanName,staticResDefinition.getiStaticRes());
    }

    /**
     * 初始化index2FieldType
     */
    private void initIndex2FieldType() {
        Field[] declaredFields = staticResDefinition.getvClass().getDeclaredFields();
        Map<String,Field> string2Field = new HashMap<>(16);
        Arrays.stream(declaredFields).forEach(field -> {
            String fieldJsonName = Optional.ofNullable(field.getAnnotation(JsonProperty.class))
                    .map(JsonProperty::value)
                    .orElseGet(field::getName);
            string2Field.put(fieldJsonName,field);
        });
        index2JsonPropertyName.keySet().forEach(integer -> {
            String jsonPropertyName = index2JsonPropertyName.get(integer);
            Field field = string2Field.get(jsonPropertyName);
            if (field==null){
                throw new RuntimeException(String.format("资源文件[%s]字段名为[%s]的列在关联的类[%s]中没有找到对应的字段",
                        staticResDefinition.getFullFileName(),jsonPropertyName,staticResDefinition.getvClass().getSimpleName()));
            }
            index2FieldType.put(integer, field.getType());
        });
    }
}
