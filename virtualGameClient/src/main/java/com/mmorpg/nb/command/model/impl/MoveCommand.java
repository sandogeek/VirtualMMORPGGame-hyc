package com.mmorpg.nb.command.model.impl;

import com.mmorpg.nb.command.model.AbstractCommand;
import com.mmorpg.nb.command.model.CommandType;
import com.mmorpg.nb.command.model.Option;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MoveCommand extends AbstractCommand {
    private Logger logger=LoggerFactory.getLogger(MoveCommand.class);

    @Override
    public CommandType getType() {
        return CommandType.MOVE;
    }

    @Override
    public void execute(String command) {
        super.execute(command);
        logger.debug("move命令执行成功");
    }

    @Override
    protected void construct() {
        Option option=Option.valueOf("target",true,"目标村庄名称");
        this.optionsMap.put(StringUtils.upperCase(option.getArgName()),option);
    }

}
