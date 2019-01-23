package com.mmorpg.mbdl.business.role.service;

import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.object.packet.CustomRoleUiInfoResp;
import com.mmorpg.mbdl.business.role.entity.RoleEntity;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.business.role.packet.*;
import com.mmorpg.mbdl.business.role.packet.vo.RoleInfo;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import com.mmorpg.mbdl.framework.event.core.SyncEventBus;
import com.mmorpg.mbdl.framework.event.preset.SessionCloseEvent;
import com.mmorpg.mbdl.framework.thread.task.Task;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 角色服务
 *
 * @author Sando Geek
 * @since v1.0 2018/12/17
 **/
@Component
public class RoleService {
    private static Logger logger = LoggerFactory.getLogger(RoleService.class);
    @Autowired
    private RoleManager roleManager;


    public AddRoleResp handleAddRoleReq(ISession session, AddRoleReq addRoleReq) {
        AddRoleResp addRoleResp = new AddRoleResp().setResult(false);
        if (roleManager.isExist(addRoleReq)){
            return addRoleResp;
        }
        if (roleManager.canCreateRole(session.getAccount())){
            RoleEntity roleEntity = roleManager.createRoleEntity(session, addRoleReq);
            RoleInfo roleInfo = new RoleInfo().setName(roleEntity.getName())
                    .setRoleType(roleEntity.getRoleType())
                    .setLevel(roleEntity.getLevel());
            addRoleResp.setResult(true);
            addRoleResp.setRoleInfo(roleInfo);
        }
        return addRoleResp;
    }

    public GetRoleListResp handleGetRoleListReq(ISession session, GetRoleListReq getRoleListReq) {
        List<RoleEntity> roleEntities = roleManager.getRoleEntityList(session.getAccount());
        GetRoleListResp roleListResp = new GetRoleListResp();
        List<RoleInfo> roleInfoList = roleListResp.getRoleInfoList();
        roleEntities.stream().forEach(roleEntity -> {
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.setName(roleEntity.getName())
                    .setLevel(roleEntity.getLevel())
                    .setRoleType(roleEntity.getRoleType());
            roleInfoList.add(roleInfo);
        });
        return roleListResp;
    }

    public DeleteRoleResp handleDeleteRoleReq(ISession session, DeleteRoleReq deleteRoleReq) {
        DeleteRoleResp deleteRoleResp = new DeleteRoleResp().setResult(false);
        List<RoleEntity> roleEntities = roleManager.getRoleEntityList(session.getAccount());
        Optional<RoleEntity> entityOptional = roleEntities.stream().filter(roleEntity -> roleEntity.getName().equals(deleteRoleReq.getRoleName()))
                .findAny();
        entityOptional.ifPresent(roleEntity -> {
            roleManager.removeRoleEntity(roleEntity.getId());
            deleteRoleResp.setNameDelete(roleEntity.getName());
            deleteRoleResp.setResult(true);
        });
        return deleteRoleResp;
    }

    public ChooseRoleResp handleChooseRoleReq(ISession session, ChooseRoleReq chooseRoleReq) {
        ChooseRoleResp chooseRoleResp = new ChooseRoleResp().setResult(false);
        RoleEntity roleEntity = roleManager.findByNameAndServerToken(chooseRoleReq.getName());
        Role role = roleManager.initRole(session, roleEntity);
        if (role!=null){
            chooseRoleResp.setResult(true);
            CustomRoleUiInfoResp customRoleUiInfoResp = new CustomRoleUiInfoResp()
                    .setMaxHp(role.getPropManager().getPropValueOf(PropType.MAX_HP))
                    .setMaxMp(role.getPropManager().getPropValueOf(PropType.MAX_MP))
                    .setCurrentHp(role.getPropManager().getPropValueOf(PropType.CURRENT_HP))
                    .setCurrentMp(role.getPropManager().getPropValueOf(PropType.CURRENT_MP));
            customRoleUiInfoResp.setRoleId(role.getRoleId())
                    .setName(role.getName())
                    .setLevel(role.getRoleEntity().getLevel())
                    .setRoleType(roleEntity.getRoleType());
            chooseRoleResp.setCustomRoleUiInfoResp(customRoleUiInfoResp);
        }
        session.setRoleId(roleEntity.getId());
        session.setState(SessionState.GAMEING);
        return chooseRoleResp;
    }

    /**
     * 处理session关闭事件
     * TODO 这里没有断线重连机制，session关闭会触发玩家下线事件
     * @param sessionCloseEvent
     */
    public void handleSessionClose(SessionCloseEvent sessionCloseEvent) {
        ISession session = sessionCloseEvent.getSession();
        Role role = roleManager.getRoleBySession(session);
        if (role == null){
            return;
        }
        // logout事件业务池处理
        TaskDispatcher.getInstance().dispatch(new Task(role.getRoleId()) {
            @Override
            public String taskName() {
                return "Logout事件";
            }

            @Override
            public void execute() {
                SyncEventBus.getInstance().post(new RoleLogoutEvent(role));
            }
        }.setMaxExecuteTime(TimeUnit.NANOSECONDS.convert(20, TimeUnit.MILLISECONDS)));
    }

    public void handleRoleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {
        Role role = roleLogoutEvent.getRole();
        roleManager.updateRoleEntity(role.getRoleEntity());
        roleManager.removeRoleBySession(role.getSession());
    }
}
