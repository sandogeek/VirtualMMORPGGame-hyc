package com.mmorpg.nb.bussiness.command.manager;

import com.mmorpg.nb.bussiness.command.model.AbstractCommand;
import com.mmorpg.nb.bussiness.command.model.CommandType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 收集存储所有command
 *
 * @author sando
 */
@Component
public class CommandManager {
    private static Logger logger=LoggerFactory.getLogger(CommandManager.class);
    private static CommandManager self;
    private static CommandManager getInstance(){return self;}
    @PostConstruct
    public void init(){self=this;}

    private Map<CommandType, AbstractCommand> commandMap=new HashMap<>();

    public void registerCommand(AbstractCommand abstractCommand){
        this.commandMap.put(abstractCommand.getType(),abstractCommand);
    }

    /**
     * 根据浏览器端传过来的字符串执行命令
     * @param command 命令
     */
    public void executeCommand(String command){
        command=StringUtils.trim(command);
        if(StringUtils.isBlank(command)){
            return;
        }
        String commandTypeName=StringUtils.left(command,StringUtils.indexOf(command," "));
        CommandType commandType=null;
        try{
            commandType=CommandType.valueOf(StringUtils.upperCase(commandTypeName));
        }catch (Exception e){
            logger.error(String.format("指令执行失败，指令不存在或指令格式有误"));
            return;
        }
        if (commandType!=null){
            this.commandMap.get(commandType).execute(command);
        }
    }

    public static void main(String[] args){
        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
        CommandManager commandManager= (CommandManager)applicationContext.getBean("commandManager");
        try{
            commandManager.executeCommand("Move -target 猪猪村");
        }catch (Exception e){
            logger.warn("传入指令不完善\n"+e.getMessage());
        }

    }

}
