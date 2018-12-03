package com.mmorpg.mbdl.framework.resource.resolver.excel;

import java.util.ArrayList;

/**
 * excel表格格式定义接口
 *
 * @author Sando Geek
 * @since v1.0
 **/
public interface IExcelFormat {
    /**
     * 表格前N(从1计数)列的内容不当作数据部分
     * @return N的具体数值
     */
    int ignoreFirstNColumn();

    /**
     * 是否是字段名所在行,这一行必须出现在表头最后一行前，或就在表头最后一行，字段名为空的列不会被处理
     * @param arrayList Excel的一行数据
     * @return 是字段名所在行，true,否则，false
     */
    boolean isFieldNameRow(ArrayList<String> arrayList);

    /**
     * 是否是表头最后一行
     * @param arrayList Excel的一行数据
     * @return 是表头最后一行，true,否则，false
     */
    boolean isTableHeadLastLine(ArrayList<String> arrayList);
}
