package com.mmorpg.mbdl.business.role.model.prop;

import com.mmorpg.mbdl.business.object.model.AbstractCreature;
import com.mmorpg.mbdl.business.role.entity.RoleEntity;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.packet.*;

/**
 * 属性类型，每个类型对应一颗属性树
 *
 * @author Sando Geek
 * @since v1.0 2019/1/21
 **/
public enum PropType {
    /**
     * 当前血量
     */
    CURRENT_HP() {
        @Override
        public PropTree create(AbstractCreature abstractCreature) {
            return new PropTree() {
                @Override
                protected void doSetPropValue(long newValue) {
                    super.doSetPropValue(newValue);
                    if (abstractCreature instanceof Role) {
                        ((Role) abstractCreature).sendPacket(new CurrentHpUpdate(newValue));
                    }
                }
            };
        }
    },
    /**
     * 当前蓝量
     */
    CURRENT_MP() {
        @Override
        public PropTree create(AbstractCreature abstractCreature) {
            return new PropTree() {
                @Override
                protected void doSetPropValue(long newValue) {
                    super.doSetPropValue(newValue);
                    if (abstractCreature instanceof Role) {
                        ((Role) abstractCreature).sendPacket(new CurrentMpUpdate(newValue));
                    }
                }
            };
        }
    },
    /**
     * 最大血量
     */
    MAX_HP() {
        @Override
        public PropTree create(AbstractCreature abstractCreature) {
            return new PropTree() {
                @Override
                protected void doSetPropValue(long newValue) {
                    super.doSetPropValue(newValue);
                    abstractCreature.getPropManager().getOrCreateTree(PropType.CURRENT_HP).setMaxValue(newValue);
                    if (abstractCreature instanceof Role) {
                        ((Role) abstractCreature).sendPacket(new MaxHpUpdate(newValue));
                        if (abstractCreature.getPropManager().getPropValueOf(PropType.CURRENT_HP) > maxValue) {
                            abstractCreature.getPropManager().getOrCreateTree(PropType.CURRENT_HP).setRootNodeValue(maxValue);
                        }
                    }
                }
            };
        }
    },
    /**
     * 最大蓝量
     */
    MAX_MP() {
        @Override
        public PropTree create(AbstractCreature abstractCreature) {
            return new PropTree() {
                @Override
                protected void doSetPropValue(long newValue) {
                    super.doSetPropValue(newValue);
                    abstractCreature.getPropManager().getOrCreateTree(PropType.CURRENT_MP).setMaxValue(newValue);
                    if (abstractCreature instanceof Role) {
                        ((Role) abstractCreature).sendPacket(new MaxMpUpdate(newValue));
                        if (abstractCreature.getPropManager().getPropValueOf(PropType.CURRENT_HP) > maxValue) {
                            abstractCreature.getPropManager().getOrCreateTree(PropType.CURRENT_HP).setRootNodeValue(maxValue);
                        }
                    }
                }
            };
        }
    },
    /**
     * 攻击力
     */
    ATTACK,
    /**
     * 防御力
     */
    DEFENCE,
    /**
     * 等级
     */
    LEVEL() {
        @Override
        public PropTree create(AbstractCreature abstractCreature) {
            Role role = (Role) abstractCreature;
            EntityPropTree entityPropTree = new EntityPropTree() {
                @Override
                protected void doSetPropValue(long newValue) {
                    RoleEntity roleEntity = role.getRoleEntity();
                    roleEntity.setLevel((short)newValue);
                    role.sendPacket(new LevelUpdate((int)newValue));
                    RoleManager.getInstance().mergeUpdateRoleEntity(roleEntity);
                }

                @Override
                public long doGetPropValue() {
                    return role.getRoleEntity().getLevel();
                }
            };
            return entityPropTree;
        }
    },
    /**
     * 所在场景ID
     */
    SCENE_ID() {
        @Override
        public PropTree create(AbstractCreature abstractCreature) {
            Role role = (Role) abstractCreature;
            return new EntityPropTree() {
                @Override
                protected void doSetPropValue(long newValue) {
                    RoleEntity roleEntity = role.getRoleEntity();
                    roleEntity.setSceneId((short)newValue);
                    RoleManager.getInstance().mergeUpdateRoleEntity(roleEntity);
                }

                @Override
                public long doGetPropValue() {
                    return role.getRoleEntity().getSceneId();
                }
            };
        }
    },
    /**
     * 经验
     */
    EXP() {
        @Override
        public PropTree create(AbstractCreature abstractCreature) {
            Role role = (Role) abstractCreature;
            return new EntityPropTree() {
                @Override
                protected void doSetPropValue(long newValue) {
                    RoleEntity roleEntity = role.getRoleEntity();
                    roleEntity.setExp((short)newValue);
                    role.sendPacket(new ExpUpdate(newValue));
                    RoleManager.getInstance().mergeUpdateRoleEntity(roleEntity);
                }

                @Override
                public long doGetPropValue() {
                    return role.getRoleEntity().getExp();
                }
            };
        }
    }
    ;
    public PropTree create(AbstractCreature abstractCreature) {
        return new PropTree();
    }

}
