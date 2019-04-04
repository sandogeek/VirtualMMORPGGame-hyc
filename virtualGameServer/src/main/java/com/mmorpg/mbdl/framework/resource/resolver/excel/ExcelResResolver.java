package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.alibaba.excel.ExcelReader;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.exposed.AbstractBeanFactoryAwareResResolver;
import com.mmorpg.mbdl.framework.resource.exposed.IExcelFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Excel静态资源解析器
 * 注意：此时各种bean还没有实例化，所以这里不能使用@Autowired
 * @author Sando Geek
 * @since v1.0
 **/
@Component
public class ExcelResResolver extends AbstractBeanFactoryAwareResResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExcelResResolver.class);
    private static IExcelFormat iExcelFormat;

    static {
        try {
            iExcelFormat = beanFactory.getBean(IExcelFormat.class);
        } catch (Exception e) {
            iExcelFormat = new ExcelFormat();
        }
    }

    @Override
    public String suffix() {
        return ".xlsx";
    }

    @Override
    public List<Object> resolve(StaticResDefinition staticResDefinition) {
        try (InputStream inputStream = staticResDefinition.getResource().getInputStream()){
            List<Object> context = new ArrayList<>(2);
            ExcelListener listener = new ExcelListener();
            listener.beanFactory = beanFactory;
            listener.staticResDefinition = staticResDefinition;
            listener.excelFormat = iExcelFormat;
            listener.idFieldJsonName = Optional.ofNullable(staticResDefinition.getIdField().getAnnotation(JsonProperty.class))
                    .map(JsonProperty::value)
                    .orElseGet(() -> staticResDefinition.getIdField().getName());
            ExcelReader excelReader = new ExcelReader(inputStream, context, listener);
            excelReader.read();
            return listener.getResList();
        } catch (IOException e) {
            throw new RuntimeException(String.format("读取资源文件[%s]发生IO异常", staticResDefinition.getFullFileName()), e);
        }
    }

    // private StaticResDefinition getFromMapByRelative2ClassPath(Map<String, StaticResDefinition> fileName2StaticResDefinition,String resPathRelative2ClassPath){
    //     String[] splitPath = resPathRelative2ClassPath.split("/");
    //     String pathToUse = splitPath[splitPath.length-1];
    //     for (int i = splitPath.length -2; i > -1; i--) {
    //         logger.info(pathToUse);
    //         pathToUse = splitPath[i] + "/" + pathToUse;
    //     }
    //     logger.info(pathToUse);
    //     return new StaticResDefinition();
    // }
}
