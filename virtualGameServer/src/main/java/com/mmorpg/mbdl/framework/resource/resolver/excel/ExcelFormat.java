package com.mmorpg.mbdl.framework.resource.resolver.excel;

import java.util.ArrayList;

/**
 * excel格式自定义
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class ExcelFormat implements IExcelFormat {
    @Override
    public int ignoreFirstNColumn() {
        return 1;
    }

    @Override
    public boolean isFieldNameRow(ArrayList<String> arrayList) {
        if (arrayList.get(0).startsWith("S")){
            return true;
        }
        return false;
    }

    @Override
    public boolean isTableHeadLastLine(ArrayList<String> arrayList) {
        if (arrayList.get(0).startsWith("S")){
            return true;
        }
        return false;
    }
}
