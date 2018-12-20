package com.mmorpg.mbdl.bussiness.role.service;

import com.mmorpg.mbdl.bussiness.common.GlobalSettingRes;
import com.mmorpg.mbdl.bussiness.role.dao.RoleEntityDao;
import com.mmorpg.mbdl.bussiness.role.entity.RoleEntity;
import com.mmorpg.mbdl.bussiness.role.model.RoleType;
import com.mmorpg.mbdl.bussiness.role.packet.*;
import com.mmorpg.mbdl.bussiness.role.packet.vo.RoleInfo;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import com.mmorpg.mbdl.framework.common.utils.CommonUtils;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 角色服务
 *
 * @author Sando Geek
 * @since v1.0 2018/12/17
 **/
@Component
public class RoleService {
    @Autowired
    private RoleEntityDao roleEntityDao;
    @Autowired
    private IStaticRes<String, GlobalSettingRes> globalSettingResIStaticRes;
    /**
     * 由数据中心和服务器id确定
     */
    private final int serverToken = CommonUtils.getSeverTokenById(IdGeneratorFactory.getIntance().getRoleIdGenerator().generate());
    private final int maxRoleSize = RoleType.values().length;


    public AddRoleResp handleAddRoleReq(ISession session, AddRoleReq addRoleReq) {
        AddRoleResp addRoleResp = new AddRoleResp().setResult(false);
        RoleEntity roleEntity = roleEntityDao.findByNameAndServerToken(addRoleReq.getRoleName(), serverToken);
        if (roleEntity != null) {
            return addRoleResp.setResult(false);
        }
        List<RoleEntity> roleEntities = roleEntityDao.findAllByAccount(session.getAccount());
        if (roleEntities.size()< maxRoleSize) {
            RoleEntity roleEntityToCreate = new RoleEntity();
            roleEntityToCreate.setAccount(session.getAccount())
                    .setName(addRoleReq.getRoleName())
                    .setRoleId(IdGeneratorFactory.getIntance().getRoleIdGenerator().generate())
                    .setRoleTypeCode(addRoleReq.getRoleType().getCode())
                    .setMapId(globalSettingResIStaticRes.get("InitMapId").getValue())
                    .setServerToken(serverToken);
            roleEntityDao.create(roleEntityToCreate);
            RoleInfo roleInfo = new RoleInfo().setName(roleEntityToCreate.getName())
                    .setRoleType(roleEntityToCreate.getRoleType())
                    .setLevel(roleEntityToCreate.getLevel());
            addRoleResp.setResult(true);
            addRoleResp.setRoleInfo(roleInfo);
        }
        return addRoleResp;
    }

    public GetRoleListResp handleGetRoleListReq(ISession session, GetRoleListReq getRoleListReq) {
        List<RoleEntity> roleEntities = roleEntityDao.findAllByAccount(session.getAccount());
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
        List<RoleEntity> roleEntities = roleEntityDao.findAllByAccount(session.getAccount());
        Optional<RoleEntity> entityOptional = roleEntities.stream().filter(roleEntity -> roleEntity.getName().equals(deleteRoleReq.getRoleName()))
                .findAny();
        entityOptional.ifPresent(roleEntity -> {
            roleEntityDao.remove(roleEntity.getId());
            deleteRoleResp.setNameDelete(roleEntity.getName());
            deleteRoleResp.setResult(true);
        });
        return deleteRoleResp;
    }

    public ChooseRoleResp handleChooseRoleReq(ISession session, ChooseRoleReq chooseRoleReq) {
        ChooseRoleResp chooseRoleResp = new ChooseRoleResp().setResult(false);
        return chooseRoleResp;
    }
}
