package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.mmorpg.mbdl.framework.common.utils.JsonUtil;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.exposed.IAfterLoad;
import com.mmorpg.mbdl.framework.resource.exposed.IExcelFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

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
    /**
     * Excel中下标到字段名的映射
     */
    private Map<Integer,String> index2FieldJsonName = new HashMap<>(16);
    /**
     * 下标到字段类型的映射
     */
    private Map<Integer,Class<?>> index2FieldType = new HashMap<>(16);
    /**
     * 用于检查key是否唯一
     */
    private Map<Object,Integer> key2RowNumber = new HashMap<>(16);

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
                        index2FieldJsonName.put(i, content);
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
                initAndCheck();
            }
            // 跳过id字段为空的行
            if (list.get(idFieldIndex)==null){
                return;
            }
            // vClass实例的json数据形式
            ArrayList<String> vClassObjectInJsonArray=new ArrayList<>(16);
            int max = list.size()-1;
            for (Integer integer : index2FieldJsonName.keySet()) {
                if (integer > max){
                    break;
                }
                String content = list.get(integer);
                if (StringUtils.isEmpty(content)) {
                    continue;
                }
                Class<?> fieldType = index2FieldType.get(integer);
                if (fieldType.isEnum() || fieldType == String.class) {
                    // 枚举或者字符串前后要加引号
                    content = "\"" + content + "\"";
                }
                vClassObjectInJsonArray.add("\"" + index2FieldJsonName.get(integer) + "\":" + content);
            }
            String vClassObjectInJson ="{" + String.join("," ,vClassObjectInJsonArray) + "}";
            Object resource = JsonUtil.string2Object(vClassObjectInJson, staticResDefinition.getvClass());
            Object obj = null;
            try {
                obj = staticResDefinition.getIdField().get(resource);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (key2RowNumber.containsKey(obj)) {
                throw new RuntimeException(String.format("资源文件[%s]主键列[%s]，主键不唯一，第[%s]行与第[%s]行重复,重复值[%s]",
                        staticResDefinition.getFullFileName(),
                        idFieldJsonName,
                        key2RowNumber.get(obj)+1,
                        context.getCurrentRowNum()+1,
                        obj
                ));
            }
            if (resource instanceof IAfterLoad) {
                IAfterLoad afterLoad = (IAfterLoad) resource;
                afterLoad.afterLoad();
            }
            key2RowNumber.put(obj,context.getCurrentRowNum());
            key2ResourceBuilder.put(obj,resource);
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
     * 初始化index2FieldType并检查类字段与表的字段能否一一对应
     */
    private void initAndCheck() {
        Field[] declaredFields = staticResDefinition.getvClass().getDeclaredFields();
        Map<String,Field> fieldJsonName2Field = new HashMap<>(16);
        Arrays.stream(declaredFields).forEach(field -> {
            String fieldJsonName = Optional.ofNullable(field.getAnnotation(JsonProperty.class))
                    .map(JsonProperty::value)
                    .orElseGet(field::getName);
            fieldJsonName2Field.put(fieldJsonName,field);
        });
        Set<String> tempSet = new HashSet<>(fieldJsonName2Field.keySet());
        tempSet.removeAll(index2FieldJsonName.values());
        if (tempSet.size() != 0) {
            throw new RuntimeException(String.format("类[%s]中名为[%s]的字段在资源文件[%s]找不到对应的字段",
                            staticResDefinition.getvClass().getSimpleName(),
                            StringUtils.join(tempSet,","),
                            staticResDefinition.getFullFileName()
                            ));
        }
        tempSet.clear();
        tempSet.addAll(index2FieldJsonName.values());
        tempSet.removeAll(fieldJsonName2Field.keySet());
        if (tempSet.size() != 0) {
            throw new RuntimeException(String.format("资源文件[%s]中名为[%s]的字段在类[%s]中找不到对应的字段",
                    staticResDefinition.getFullFileName(),
                    StringUtils.join(tempSet,","),
                    staticResDefinition.getvClass().getSimpleName()
            ));
        }
        index2FieldJsonName.keySet().forEach(integer -> {
            String fieldJsonName = index2FieldJsonName.get(integer);
            Field field = fieldJsonName2Field.get(fieldJsonName);
            if (field==null){
                throw new RuntimeException(String.format("资源文件[%s]字段名为[%s]的列在关联的类[%s]中没有找到对应的字段",
                        staticResDefinition.getFullFileName(),fieldJsonName,staticResDefinition.getvClass().getSimpleName()));
            }
            index2FieldType.put(integer, field.getType());
        });
    }
}
