package com.mmorpg.mbdl.business.role.model;

import com.mmorpg.mbdl.business.container.entity.ContainerEntity;
import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.equip.entity.EquipEntity;
import com.mmorpg.mbdl.business.equip.manager.EquipManager;
import com.mmorpg.mbdl.business.object.model.AbstractCreature;
import com.mmorpg.mbdl.business.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.business.object.model.SceneObjectType;
import com.mmorpg.mbdl.business.object.packet.CustomRoleUiInfoResp;
import com.mmorpg.mbdl.business.role.entity.RoleEntity;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.business.role.model.prop.EntityPropTree;
import com.mmorpg.mbdl.business.role.model.prop.PropTree;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.role.packet.*;
import com.mmorpg.mbdl.business.role.resource.RoleLevelRes;
import com.mmorpg.mbdl.business.skill.entity.SkillEntity;
import com.mmorpg.mbdl.business.skill.manager.SkillManager;
import com.mmorpg.mbdl.business.world.manager.SceneManager;
import com.mmorpg.mbdl.business.world.scene.model.Scene;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 角色
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class Role extends AbstractCreature {
    private static Logger logger = LoggerFactory.getLogger(Role.class);
    private ISession session;

    /** 角色相关实体 **/
    private RoleEntity roleEntity;
    private ContainerEntity containerEntity;
    private EquipEntity equipEntity;
    private SkillEntity skillEntity;

    public Role(Long objectId, String name) {
        super(objectId,name);
    }

    /**
     * 绑定各种实体
     */
    public void bindEntity() {
        ContainerManager.getInstance().bindContainerEntity(this);
        EquipManager.getInstance().bindEntity(this);
        SkillManager.getInstance().bindEntity(this);
    }

    /**
     * 角色初始化
      */
    @Override
    public void init() {
        propManager.setPropTreeOnPropType(new PropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                super.doSetPropValue(newValue);
                broadcast(new CurrentHpUpdate(getRoleId(), newValue), true);
            }
        }, PropType.CURRENT_HP);
        propManager.setPropTreeOnPropType(new PropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                super.doSetPropValue(newValue);
                broadcast(new CurrentMpUpdate(getRoleId(), newValue), true);
            }
        }, PropType.CURRENT_MP);
        propManager.setPropTreeOnPropType(new PropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                super.doSetPropValue(newValue);
                PropTree propTree = getPropManager().getOrCreateTree(PropType.CURRENT_HP);
                propTree.setMaxValue(newValue);
                Long maxValue = propTree.getMaxValue();
                broadcast(new MaxHpUpdate(getRoleId(), newValue), true);
                if (getPropManager().getPropValueOf(PropType.CURRENT_HP) > maxValue) {
                    propTree.setRootNodeValue(maxValue);
                }
            }
        }, PropType.MAX_HP);
        propManager.setPropTreeOnPropType(new PropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                super.doSetPropValue(newValue);
                PropTree propTree = getPropManager().getOrCreateTree(PropType.CURRENT_MP);
                propTree.setMaxValue(newValue);
                Long maxValue = propTree.getMaxValue();
                broadcast(new MaxMpUpdate(getRoleId(), newValue), true);
                if (getPropManager().getPropValueOf(PropType.CURRENT_MP) > maxValue) {
                    propTree.setRootNodeValue(maxValue);
                }
            }
        }, PropType.MAX_MP);
        propManager.getOrCreateTree(PropType.ATTACK);
        propManager.getOrCreateTree(PropType.DEFENCE);
        propManager.setPropTreeOnPropType(new EntityPropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                RoleEntity roleEntity = getRoleEntity();
                roleEntity.setLevel((short) newValue);
                broadcast(new LevelUpdate(getRoleId(), (int) newValue), true);
                RoleManager.getInstance().mergeUpdateRoleEntity(roleEntity);
            }

            @Override
            public long doGetPropValue() {
                return getRoleEntity().getLevel();
            }
        }, PropType.LEVEL);
        propManager.setPropTreeOnPropType(new EntityPropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                RoleEntity roleEntity = getRoleEntity();
                roleEntity.setSceneId((short) newValue);
                RoleManager.getInstance().mergeUpdateRoleEntity(roleEntity);
            }

            @Override
            public long doGetPropValue() {
                return getRoleEntity().getSceneId();
            }
        }, PropType.SCENE_ID);
        propManager.setPropTreeOnPropType(new EntityPropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                RoleEntity roleEntity = getRoleEntity();
                roleEntity.setExp((short) newValue);
                sendPacket(new ExpUpdate(newValue));
                RoleManager.getInstance().mergeUpdateRoleEntity(roleEntity);
            }

            @Override
            public long doGetPropValue() {
                return getRoleEntity().getExp();
            }
        }, PropType.EXP);
        updatePropForLevel();
        fullHP();
        fullMP();
    }

    public void updatePropForLevel() {
        RoleLevelRes roleLevelRes = RoleManager.getInstance().getRoleLevelResByLevel(roleEntity.getLevel());
        propManager.getPropTreeByType(PropType.MAX_HP).getOrCreateChild("level").set(roleLevelRes.getMaxHp());
        propManager.getPropTreeByType(PropType.MAX_MP).getOrCreateChild("level").set(roleLevelRes.getMaxMp());
        propManager.getPropTreeByType(PropType.ATTACK).getOrCreateChild("level").set(roleLevelRes.getAttack());
        propManager.getPropTreeByType(PropType.DEFENCE).getOrCreateChild("level").set(roleLevelRes.getDefence());
    }

    @Override
    public int getSceneId() {
        return (int)propManager.getPropValueOf(PropType.SCENE_ID);
    }

    // public void useSkillOnTarget(int skillId, AbstractCreature target) {
    //     SkillRes skillRes = SkillManager.getInstance().getSkillResById(skillId);
    //     int mpCost = skillRes.getMpCost();
    //     long damage = GameMathUtil.computeDamage(skillRes.getBasicDamage(),
    //             (int) propManager.getPropValueOf(PropType.ATTACK),
    //             skillRes.getAttackPercent(),
    //             (int) target.getPropManager().getPropValueOf(PropType.DEFENCE));
    //     // TODO 目前没有其它途径消耗蓝，如果有要注意是否会出现线程安全问题,可能需要把后续判断等操作放到回调中，以便上锁
    //     long currentMp = propManager.getPropValueOf(PropType.CURRENT_MP);
    //     if (mpCost > currentMp) {
    //         this.sendPacket(new GlobalMessage("蓝量不足,技能释放失败"));
    //         return;
    //     }
    //     this.changeMp(-mpCost);
    //     // 给目标造成的血量扣除
    //     target.changeHp(-damage);
    // }

    /**
     * 展示给其他人的ui信息
     * @param witness 得到可见信息的角色
     * @return
     */
    @Override
    public AbstractPacket getUiInfoResp(Role witness) {
        return getCustomRoleUiInfoResp();
    }


    /**
     * 展示给自己的信息
     * @return
     */
    public CustomRoleUiInfoResp getCustomRoleUiInfoResp() {
        RoleEntity roleEntity = this.getRoleEntity();
        CustomRoleUiInfoResp customRoleUiInfoResp = new CustomRoleUiInfoResp()
                .setMaxHp(this.getPropManager().getPropValueOf(PropType.MAX_HP))
                .setMaxMp(this.getPropManager().getPropValueOf(PropType.MAX_MP))
                .setCurrentHp(this.getPropManager().getPropValueOf(PropType.CURRENT_HP))
                .setCurrentMp(this.getPropManager().getPropValueOf(PropType.CURRENT_MP));
        customRoleUiInfoResp.setRoleId(this.getRoleId())
                .setName(this.getName())
                .setLevel(roleEntity.getLevel())
                .setRoleType(roleEntity.getRoleType());
        return customRoleUiInfoResp;
    }

    @Override
    public AbstractVisibleSceneObject setSceneId(int sceneId) {
        this.getPropManager().setRootNodeValueOnType(PropType.SCENE_ID,sceneId);
        return super.setSceneId(sceneId);
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public Role setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
        return this;
    }

    public EquipEntity getEquipEntity() {
        return equipEntity;
    }

    public Role setEquipEntity(EquipEntity equipEntity) {
        this.equipEntity = equipEntity;
        return this;
    }

    public ContainerEntity getContainerEntity() {
        return containerEntity;
    }

    public Role setContainerEntity(ContainerEntity containerEntity) {
        this.containerEntity = containerEntity;
        return this;
    }

    public SkillEntity getSkillEntity() {
        return skillEntity;
    }

    public Role setSkillEntity(SkillEntity skillEntity) {
        this.skillEntity = skillEntity;
        return this;
    }

    public Long getRoleId(){
        return getObjectId();
    }


    public ISession getSession() {
        return session;
    }

    public Role setSession(ISession session) {
        if (this.session == null){
            this.session = session;
        } else {
            logger.error("Role的session已设置，请勿重复设置");
        }
        return this;
    }

    public ChannelFuture sendPacket(AbstractPacket abstractPacket){
        return session.sendPacket(abstractPacket);
    }

    public ChannelFuture sendPacket(AbstractPacket abstractPacket,boolean flushNow){
        return session.sendPacket(abstractPacket,flushNow);
    }

    public void broadcastNotIncludeSelf(AbstractPacket abstractPacket) {
        broadcast(abstractPacket,false);
    }
    /**
     * 把消息广播给当前场景中的所有可见玩家
     * @param abstractPacket 要广播的包
     * @param includeSelf 是否包含自身
     */
    public void broadcast(AbstractPacket abstractPacket,boolean includeSelf){
        Scene scene = SceneManager.getInstance().getSceneBySceneId(getSceneId());
        Collection<Role> values = scene.getObjId2Role().values();
        if (includeSelf){
            // 处理刚上线初始化时尚未加入到场景中的情况
            if (!values.contains(this)) {
                this.sendPacket(abstractPacket);
            }
            for (Role role : values) {
                role.sendPacket(abstractPacket);
            }
        }else {
            values.stream()
                    .filter(role -> role.equals(this)).forEach(role -> role.sendPacket(abstractPacket));
        }

    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.PLAYER;
    }

    @Override
    public String toString() {
        return String.format("角色[%s]", roleEntity.getName());
    }

}
