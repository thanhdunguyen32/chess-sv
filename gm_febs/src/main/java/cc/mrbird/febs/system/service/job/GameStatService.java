package cc.mrbird.febs.system.service.job;

import cc.mrbird.febs.system.entity.DailyStatEntity;
import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.service.GsService;
import cc.mrbird.febs.system.service.QueryService;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import lion.netty4.message.RequestByteMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component("gameStatService")
@Slf4j
public class GameStatService {
	
	@Autowired
	private GmLanManager gmLanManager;
	
	@Autowired
	private GsService gsConfigService;
	
	@Autowired
	private QueryService queryService;

    /**
     * 每天晚上11点59分统计服务器信息
     */
    @Scheduled(cron = "55 59 23 * * ?")
    public void dailyStat() {
    	log.info("daily server stat start!!!");
    	int online_count = 0;
		int new_player = 0;
		int active_player = 0;
		int new_pay_players = 0;
		int pay_player_count = 0;
		int pay_val = 0;
		int today_login_count = 0;
		int yesterday_create_count = 0;
		// 发送至seed_server
		Collection<GsEntity> gsAll = gsConfigService.getValidGsEntity();
		for (GsEntity gsEntity : gsAll) {
			boolean connectRet = gmLanManager.connect(gsEntity);
			if (connectRet) {
				gmLanManager.getMainStatParams(gsEntity.getHost(), gsEntity.getPort());
				GsSyncRequest.getInstance().doSend();
				RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
				if (retMsg == null) {
					log.error("get info error!gsEntity={}",gsEntity);
				} else {
					online_count += retMsg.readInt();
					new_player += retMsg.readInt();
					active_player += retMsg.readInt();
					new_pay_players += retMsg.readInt();
					pay_player_count += retMsg.readInt();
					pay_val += retMsg.readInt();
					today_login_count += retMsg.readInt();
					yesterday_create_count += retMsg.readInt();
					GsSyncRequest.getInstance().releaseRetMsg();
				}
			} else {
				log.error("connect error!gsEntity={}",gsEntity);
			}
		}
		//昨日留存
		float yesterday_retain = 0f;
		if (yesterday_create_count > 0) {
			yesterday_retain = today_login_count * 100f / yesterday_create_count;
		}
		DailyStatEntity aEntity = new DailyStatEntity();
		aEntity.setActivePlayer(active_player);
		aEntity.setNewPay(new_pay_players);
		aEntity.setNewPlayer(new_player);
		aEntity.setPayCount(pay_player_count);
		aEntity.setPaySum(pay_val);
		aEntity.setStatTime(new Date());
		aEntity.setYesterdayRetain(yesterday_retain);
		queryService.addDailyStat(aEntity);
		log.info("daily server stat end,val={}",aEntity);
    }

}
