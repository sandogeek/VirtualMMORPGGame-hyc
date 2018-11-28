package com.mmorpg.mbdl.easyexcel;

import com.alibaba.excel.ExcelReader;
import com.mmorpg.mbdl.bussiness.common.error.resource.ErrorTipsRes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;

/**
 * 读测试
 *
 * @author Sando Geek
 * @since v1.0
 **/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReadTest {
    private Resource[] resources;
    @BeforeAll
    void 资源初始化() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        resources = resourcePatternResolver.getResources("classpath*:**/*.xls*");
    }

    @Test
    void excel读取() {
        String filename = resources[0].getFilename();
        try (InputStream inputStream = resources[0].getInputStream();
             // InputStream inputStream2 = resources[1].getInputStream()
        ) {
            // 解析每行结果在listener中处理
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ErrorTipsRes.class,listener);
            excelReader.read();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
