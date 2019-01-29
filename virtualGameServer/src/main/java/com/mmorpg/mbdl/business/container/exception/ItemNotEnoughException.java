package com.mmorpg.mbdl.business.container.exception;

/**
 * 物品不足异常
 *
 * @author Sando Geek
 * @since v1.0 2019/1/29
 **/
public class ItemNotEnoughException extends RuntimeException {
    public ItemNotEnoughException(String message) {
        super(message);
    }
}
