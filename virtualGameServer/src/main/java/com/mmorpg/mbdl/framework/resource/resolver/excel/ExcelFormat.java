package com.mmorpg.mbdl.framework.resource.resolver.excel;

import java.util.ArrayList;
import java.util.Optional;

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
    public boolean isFieldNamesRow(ArrayList<String> arrayList) {
        return Optional.ofNullable(arrayList.get(0)).map(value -> value.startsWith("S")).orElse(false);
    }

    @Override
    public boolean isTableHeadLastLine(ArrayList<String> arrayList) {
        return Optional.ofNullable(arrayList.get(0)).map(value -> value.startsWith("S")).orElse(false);
    }
}
