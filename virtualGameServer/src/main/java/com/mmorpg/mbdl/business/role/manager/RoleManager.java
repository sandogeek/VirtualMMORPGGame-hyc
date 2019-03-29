package com.mmorpg.mbdl.business.role.manager;

import com.mmorpg.mbdl.common.resource.GlobalSettingRes;
import com.mmorpg.mbdl.business.role.dao.RoleEntityDao;
import com.mmorpg.mbdl.business.role.entity.RoleEntity;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.RoleType;
import com.mmorpg.mbdl.business.role.packet.AddRoleReq;
import com.mmorpg.mbdl.business.role.resource.RoleLevelRes;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import com.mmorpg.mbdl.framework.common.utils.CommonUtils;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 角色管理器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/20
 **/
@Component
public class RoleManager {
    private static RoleManager self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static RoleManager getInstance() {
        return self;
    }
    private static Logger logger = LoggerFactory.getLogger(RoleManager.class);
    @Autowired
    private RoleEntityDao roleEntityDao;
    @Autowired
    private IStaticRes<String, GlobalSettingRes> globalSettingResIStaticRes;
    @Autowired
    private IStaticRes<Short, RoleLevelRes> roleLevelResMap;

    private Map<ISession, Role> session2Role = new ConcurrentHashMap<>(128);

    public RoleLevelRes getRoleLevelResByLevel(short level) {
        return roleLevelResMap.get(level);
    }

    /**
     * 由数据中心和服务器id确定
     */
    private static final int SERVER_TOKEN = CommonUtils.getSeverTokenById(IdGeneratorFactory.getIntance().getRoleIdGenerator().generate());
    private final int maxRoleSize = RoleType.values().length;

    /**
     * 当前服务器是否已存在同名角色
     * @param addRoleReq
     * @return
     */
    public boolean isExist(AddRoleReq addRoleReq) {
        RoleEntity roleEntity = roleEntityDao.findByNameAndServerToken(addRoleReq.getRoleName(), SERVER_TOKEN);
        if (roleEntity != null) {
            return true;
        }
        return false;
    }

    public Map<ISession, Role> getSession2Role() {
        return session2Role;
    }

    public List<RoleEntity> getRoleEntityList(String account){
        return roleEntityDao.findAllByAccount(account);
    }

    /**
     * 能否创建角色
     * @param account
     * @return
     */
    public boolean canCreateRole(String account) {
        List<RoleEntity> roleEntities = getRoleEntityList(account);
        if (roleEntities.size()< maxRoleSize) {
            return true;
        }
        return false;
    }

    /**
     * 创建角色Entity,并添加到数据库
     * @param session
     * @param addRoleReq
     */
    public RoleEntity createRoleEntity(ISession session, AddRoleReq addRoleReq) {
        RoleEntity roleEntityToCreate = new RoleEntity();
        roleEntityToCreate.setAccount(session.getAccount())
                .setName(addRoleReq.getRoleName())
                .setRoleId(IdGeneratorFactory.getIntance().getRoleIdGenerator().generate())
                .setRoleType(addRoleReq.getRoleType())
                .setSceneId(globalSettingResIStaticRes.get("InitMapId").getValue())
                .setServerToken(SERVER_TOKEN);
        roleEntityDao.create(roleEntityToCreate);
        return roleEntityToCreate;
    }

    public void mergeUpdateRoleEntity(RoleEntity roleEntity) {
        roleEntityDao.mergeUpdate(roleEntity);
    }

    public void updateRoleEntity(RoleEntity roleEntity) {
        roleEntityDao.update(roleEntity);
    }



    /**
     * 初始化角色
     * @param session
     * @param roleEntity
     * @return 成功，true,失败,false
     */
    public Role initRole(ISession session,RoleEntity roleEntity){
        Role role = new Role(roleEntity.getId(), roleEntity.getName());
        role.setRoleEntity(roleEntity);
        role.setSession(session);
        role.init();
        role.bindEntity();
        if (getSession2Role().values().contains(role)){
            logger.error("玩家[roleId={}]重复初始化",role.getRoleId());
            return null;
        }
        addRole(role);
        return role;
    }

    /**
     * 添加角色
     * @param role
     */
    public void addRole(Role role){
        session2Role.put(role.getSession(),role);
    }

    /**
     * 删除角色
     * @param session
     */
    public void removeRoleBySession(ISession session) {
        session2Role.remove(session);
    }

    /**
     * 通过session获取role
     * @param session
     */
    public Role getRoleBySession(ISession session) {
        return session2Role.get(session);
    }
    /**
     * 删除角色实体
     * @param id 角色id
     */
    public void removeRoleEntity(Long id) {
        roleEntityDao.remove(id);
    }

    public RoleEntity findByNameAndServerToken(String name) {
        return roleEntityDao.findByNameAndServerToken(name, SERVER_TOKEN);
    }
}
