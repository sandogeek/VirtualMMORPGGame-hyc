package com.mmorpg.nb.bussiness.command.model;

/**
 * 命令选项
 * @author sando
 */
public class Option {
    // 选项描述,如：目标场景名称，在输入框中作为placeholder显示
    private String desc;
    // 选项值
    private String value;
    // 选项值类型
    // private Class clazz;
    // 是否是必备选项
    private boolean required;
    // 选项参数名 如"-target <村庄>"，则其值应设置为"target"
    private String argName;
    // 选项是否可用,命令初始化前不可用，命令重新初始化时先要设置所有选项不可用，以免读取到命令未设置的值
    private boolean capable =false;

    public static Option valueOf(String argName, boolean required, String desc){
        Option option=new Option();
        option.setArgName(argName);
        option.setRequired(required);
        option.setDesc(desc);
        // option.clazz=clazz;
        return option;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isRequired() {
        return required;
    }

    public String getArgName() {
        return argName;
    }

    public boolean isCapable() {
        return capable;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setValue(String value) {
        // 设置值得同时把此选项设置为可用
        setCapable(true);
        this.value = value;
    }

    public String getValue() {
        if (isCapable()){
            return value;
        }else {
            throw new RuntimeException(String.format("尝试使用当前不可用的选项[%s]",argName));
        }

    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setArgName(String argName) {
        this.argName = argName;
    }

    public void setCapable(boolean capable) {
        this.capable = capable;
    }

}
