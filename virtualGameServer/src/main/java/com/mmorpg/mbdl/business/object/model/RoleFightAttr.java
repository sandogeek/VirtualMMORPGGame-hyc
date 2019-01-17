package com.mmorpg.mbdl.business.object.model;

/**
 * 角色战斗属性
 *
 * @author Sando Geek
 * @since v1.0 2019/1/16
 **/
public class RoleFightAttr extends CreatureFightAttr {

    public RoleFightAttr(Role owner) {
        super(owner);
    }

    public long getExp() {
        return this.getOwner().getRoleEntity().getExp();
    }

    private void setExp(long exp) {
        this.getOwner().getRoleEntity().setExp(exp);
    }

    public void addExp(long exp) {
        setExp(getExp()+exp);
        // TODO
    }

    @Override
    public short getLevel() {
        return this.getOwner().getRoleEntity().getLevel();
    }

    @Override
    public void setLevel(short level) {
        throw new RuntimeException("请勿在角色上使用此函数设置等级");
    }

    /**
     * 升级
     * @param level 提升的级数
     */
    public void upLevel(short level) {
        // TODO 升级相关业务
    }

    @Override
    public Role getOwner() {
        return (Role) super.getOwner();
    }
}
