package com.mmorpg.nb.bussiness.command.model;

import com.mmorpg.nb.bussiness.command.manager.CommandManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抽象的command
 */
public abstract class AbstractCommand {
    @Autowired
    private CommandManager commandManager;
    // 参数值可以为空字符串，则选项表示一个flag
    private static Pattern pattern=Pattern.compile("-(\\w+)\\s+((?:\\w|[\\u4e00-\\u9fa5])*)\\s*");

    // Option.argName->Option
    protected Map<String,Option> optionsMap=new HashMap<>();

    @PostConstruct
    public void init(){
        construct();
        commandManager.registerCommand(this);
    }

    /**
     * 获取命令类型
     * @return 命令类型
     */
    public abstract CommandType getType();
    /**
     * 执行命令
     */
    public void execute(String command){
        setOptions(command);
    }

    /**
     * 构建命令
     */
    protected abstract void construct();
    /**
     * 根据命令给各个选项设置值
     */
    private void setOptions(String command){
        disableAll();
        Matcher matcher = AbstractCommand.pattern.matcher(command);
        // <参数名：如target,参数值> 由命令字符串解析出来的map
        Map<String,String> commandMap = new HashMap<>(1);
        while (matcher.find()){
            // 如果参数名相同，只有最后一次设置有效
            commandMap.put(StringUtils.upperCase(matcher.group(1)),matcher.group(2));
        }
        for (Map.Entry<String,Option> entry:this.optionsMap.entrySet()){
            if (entry.getValue().isRequired()){
                String value=commandMap.get(entry.getKey());
                if (value==null){
                    throw new RuntimeException(
                            String.format("命令[%s]必备选项[-%s]未包含在命令中",
                                    this.getClass().getSimpleName(),entry.getValue().getArgName()));
                }
                entry.getValue().setValue(value);
            } else if (commandMap.containsKey(entry.getKey())){
                entry.getValue().setValue(commandMap.get(entry.getKey()));
            }
        }
    }
    /**
     * 使所有选项失效，在{@link AbstractCommand#setOptions}前调用
     */
    private void disableAll(){
        for (Option option : this.optionsMap.values()) {
            option.setCapable(false);
        }
    }
}
