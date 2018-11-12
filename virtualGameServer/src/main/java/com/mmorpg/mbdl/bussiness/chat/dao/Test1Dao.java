package com.mmorpg.mbdl.bussiness.chat.dao;

import com.mmorpg.mbdl.bussiness.chat.packet.ChatResp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Test1Dao extends JpaRepository<ChatResp,Long> {
}
