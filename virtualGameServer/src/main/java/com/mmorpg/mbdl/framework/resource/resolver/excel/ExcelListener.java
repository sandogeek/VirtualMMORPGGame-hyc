package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.ImmutableMap;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * excel读取监听器
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class ExcelListener extends AnalysisEventListener<ArrayList> {
    private static Logger logger = LoggerFactory.getLogger(ExcelListener.class);

    private ConfigurableListableBeanFactory beanFactory;
    private StaticResDefinition staticResDefinition;

    ImmutableMap key2Resource;
    /** 建立字段名称到excel数据行数组下标的映射 */
    String[] fieldName;
    @Override
    public void invoke(ArrayList list, AnalysisContext context) {
        if (beanFactory==null){
            List contextCustom = (List) context.getCustom();
            beanFactory = (ConfigurableListableBeanFactory)contextCustom.get(0);
            staticResDefinition = (StaticResDefinition)contextCustom.get(1);
        }
        IExcelFormat excelFormat = beanFactory.getBean(IExcelFormat.class);
        logger.info("当前行："+context.getCurrentRowNum());
        logger.info("{}",list);
        doSomething(list);//根据自己业务做处理
    }

    private void doSomething(ArrayList list) {
        //1、入库调用接口
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

        // datas.clear();//解析结束销毁不用的资源
    }
}
