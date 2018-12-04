package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.alibaba.excel.ExcelReader;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.exposed.AbstractBeanFactoryAwareResResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel静态资源解析器
 * 注意：此时各种bean还没有实例化，所以这里不能使用@Autowired
 * @author Sando Geek
 * @since v1.0
 **/
@Component
public class ExcelResResolver extends AbstractBeanFactoryAwareResResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExcelResResolver.class);

    @Override
    public String suffix() {
        return ".xlsx";
    }

    @Override
    public void resolve(StaticResDefinition staticResDefinition) {
        try (InputStream inputStream = staticResDefinition.getResource().getInputStream()){
            List<Object> context = new ArrayList<>(2);
            context.add(beanFactory);
            context.add(staticResDefinition);
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, context,listener);
            excelReader.read();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("读取资源文件[%s]发生异常",staticResDefinition.getFullFileName()));
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
