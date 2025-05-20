package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.dao.DailyStatDao;
import cc.mrbird.febs.system.entity.DailyStatEntity;
import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.entity.PlayerBaseInfoEntity;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import lion.netty4.message.RequestByteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class QueryService {

	@Autowired
	private DailyStatDao dailyStatDao;
	@Autowired
    private GsService gsService;
	@Autowired
    private GmLanManager gmLanManager;

	public List<DailyStatEntity> getDailyStatAll(){
		return dailyStatDao.findAll();
	}

	public void addDailyStat(DailyStatEntity newEntity) {
		dailyStatDao.save(newEntity);
	}
	
	public List<GsEntity> getAllServer(){
		return gsService.getGsList();
	}

	public String getUserByUsernameAndServer(String username, Integer serverID){
		List<GsEntity> gsAll = gsService.getGsList();
		GsEntity gsEntity = null;
		for (GsEntity gs : gsAll) {
			if (gs.getId().equals(serverID)) {
				gsEntity = gs;
				break;
			}
		}
		if (gsEntity == null) {
			throw new RuntimeException("Không tìm thấy máy chủ trò chơi với ID: " + serverID);
		}

		boolean connectRet = gmLanManager.connect(gsEntity);
		if (!connectRet) {
			throw new RuntimeException("Không thể kết nối đến máy chủ trò chơi");
		}

		log.info("connect success!gsEntity={}", gsEntity);
		log.info("server id={}", serverID);
		log.info("username={}", username);

		gmLanManager.getUserInfo(gsEntity.getHost(), gsEntity.getPort(), username, serverID);
		GsSyncRequest.getInstance().doSend();
		RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
		if (retMsg == null) {
			log.error("get info error!gsEntity={}", gsEntity);
			throw new RuntimeException("Kết nối máy chủ trò chơi bất thường!");
		}

		return retMsg.readString();
	}

	public Map<String, Object> getPlayerAllBase(Integer serverId) {
		if (serverId == null) {
			throw new RuntimeException("ServerId không thể để trống");
		}

		// Lấy server theo ID
		List<GsEntity> gsAll = gsService.getGsList();
		GsEntity gsEntity = gsAll.stream()
			.filter(gs -> gs.getId().equals(serverId))
			.findFirst()
			.orElseThrow(() -> new RuntimeException("Không tìm thấy máy chủ với ID: " + serverId));

		try {
			boolean connectRet = gmLanManager.connect(gsEntity);
			if (!connectRet) {
				log.error("Không thể kết nối tới máy chủ: {}", gsEntity);
				throw new RuntimeException("Không thể kết nối tới máy chủ game");
			}

			gmLanManager.getPlayerAllBase(gsEntity.getHost(), gsEntity.getPort());
			GsSyncRequest.getInstance().doSend();
			RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
			
			if (retMsg == null) {
				log.error("Không nhận được phản hồi từ máy chủ: {}", gsEntity);
				throw new RuntimeException("Không nhận được phản hồi từ máy chủ game");
			}

			List<PlayerBaseInfoEntity> retlist = new ArrayList<>();
			int sizeAll = retMsg.readInt();
			for (int i = 0; i < sizeAll; i++) {
				PlayerBaseInfoEntity aEntity = new PlayerBaseInfoEntity();
				aEntity.setId(retMsg.readInt());
				aEntity.setName(retMsg.readString());
				aEntity.setLevel(retMsg.readInt());
				aEntity.setViplevel(retMsg.readInt());
				aEntity.setFight(retMsg.readInt());
				aEntity.setGold(retMsg.readInt());
				aEntity.setCooper(retMsg.readInt());
				retlist.add(aEntity);
			}
			GsSyncRequest.getInstance().releaseRetMsg();

			// Sắp xếp danh sách
			retlist.sort((o1, o2) -> {
				if (o1.getGold() == 0 && o2.getGold() > 0) return 1;
				if (o1.getGold() > 0 && o2.getGold() == 0) return -1;
				if (o2.getFight() != o1.getFight()) return o2.getFight() - o1.getFight();
				if (o2.getLevel() != o1.getLevel()) return o2.getLevel() - o1.getLevel();
				return o2.getGold() - o1.getGold();
			});

			Map<String, Object> data = new HashMap<>();
			data.put("rows", retlist);
			data.put("total", retlist.size());
			return data;
			
		} catch (Exception e) {
			log.error("Lỗi khi lấy thông tin người chơi từ máy chủ: {}", gsEntity, e);
			throw new RuntimeException("Lỗi khi lấy thông tin người chơi: " + e.getMessage());
		}
	}
}

