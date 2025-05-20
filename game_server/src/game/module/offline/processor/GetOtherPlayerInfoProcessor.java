package game.module.offline.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.logic.BattleFormationManager;
import game.module.guozhan.logic.GuoZhanManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralAwakeTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralManager;
import game.module.kingpvp.logic.KingPvpManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pvp.logic.PvpManager;
import game.module.template.GeneralTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHall.C2SGetOtherPlayerInfo.id, accessLimit = 200)
public class GetOtherPlayerInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetOtherPlayerInfoProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SGetOtherPlayerInfo reqMsg = WsMessageHall.C2SGetOtherPlayerInfo.parse(request);
        int playerId = playingRole.getId();
        logger.info("get other player info,playerId={},req={}", playerId, reqMsg);
        int[] playerIds = reqMsg.ids;
        //ret
        WsMessageHall.S2CGetOtherPlayerInfo respmsg = new WsMessageHall.S2CGetOtherPlayerInfo();
        respmsg.items = new ArrayList<>();
        for (int aPlayerId : playerIds) {
            PlayerBaseBean playerOffline = PlayerOfflineManager.getInstance().getPlayerOfflineCache(aPlayerId);
            if (playerOffline == null) {
                continue;
            }
            WsMessageBase.IOOtherPlayer ioOtherPlayer = new WsMessageBase.IOOtherPlayer();
            ioOtherPlayer.rid = playerOffline.getId();
            ioOtherPlayer.rno = playerOffline.getId();
            ioOtherPlayer.rname = playerOffline.getName();
            ioOtherPlayer.sex = playerOffline.getSex();
            ioOtherPlayer.power = playerOffline.getPower();
            ioOtherPlayer.iconid = playerOffline.getIconid();
            ioOtherPlayer.headid = playerOffline.getHeadid();
            ioOtherPlayer.frameid = playerOffline.getFrameid();
            ioOtherPlayer.imageid = playerOffline.getImageid();
            ioOtherPlayer.level = playerOffline.getLevel();
            ioOtherPlayer.vip = playerOffline.getVipLevel();
            ioOtherPlayer.office_index = GuoZhanManager.getInstance().getOfficeIndex(aPlayerId);
            ioOtherPlayer.points = PvpManager.getInstance().getPvpScore(aPlayerId);//Điểm đấu trường
            Map<Long, GeneralBean> generalAll = PlayerOfflineManager.getInstance().getGeneralAll(aPlayerId);
            BattleFormation battleFormation = PlayerOfflineManager.getInstance().getBattleFormation(aPlayerId);
            if (battleFormation != null) {
                ioOtherPlayer.battleset = new WsMessageBase.IOBattleSet();
                int battleFormationIndex = 0;
                if (reqMsg.pvpdef) {
                    battleFormationIndex = 4;
                } else if (StringUtils.isNotBlank(reqMsg.battleset) && !reqMsg.battleset.equals("kingpvp")) {
                    battleFormationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, reqMsg.battleset);
                }
                if (battleFormation.getMythics() != null && battleFormation.getMythics().containsKey(battleFormationIndex)) {
                    ioOtherPlayer.battleset.mythic = battleFormation.getMythics().get(battleFormationIndex);
                }
                Map<Integer, Long> battleFormationSet = BattleFormationManager.getInstance().getFormationByType(battleFormationIndex, battleFormation);
                if (battleFormationSet != null) {
                    ioOtherPlayer.battleset.team = new HashMap<>();
                    for (Map.Entry<Integer, Long> aEntry : battleFormationSet.entrySet()) {
                        GeneralBean generalBean = generalAll.get(aEntry.getValue());
                        //Có lẽ anh hùng đã bị xóa
                        if (generalBean == null) {
                            continue;
                        }
                        if (battleFormationIndex == 9 || battleFormationIndex == 10 || battleFormationIndex == 11) {
                            generalBean = generalTmpUpgrade(generalBean);
                        }
                        WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                        ioOtherPlayer.battleset.team.put(aEntry.getKey(), ioGeneralBean);
                    }
                }
            } else {
                //get best generals
                List<GeneralBean> generalList = new ArrayList<>(generalAll.values());
                generalList.sort((g1, g2) -> g2.getPower().compareTo(g1.getPower()));
                ioOtherPlayer.bestgeneral = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    if (i >= generalList.size()) {
                        break;
                    }
                    GeneralBean generalBean = generalList.get(i);
                    ioOtherPlayer.bestgeneral.add(GeneralManager.getInstance().buildIoGeneral(generalBean));
                }
            }
            //
            if (reqMsg.battleset.equals("kingpvp")) {
                int kpOnlineNum = KingPvpManager.getInstance().getKpOnlineNum(aPlayerId);
                ioOtherPlayer.battlelines = new ArrayList<>();
                for (int i = 0; i < kpOnlineNum; i++) {
                    String kingpvpFormation = "kingpvp" + (i + 1);
                    int formationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, kingpvpFormation);
                    Map<Integer, Long> kpFormationSet = BattleFormationManager.getInstance().getFormationByType(formationIndex, battleFormation);
                    if (kpFormationSet == null) {
                        ioOtherPlayer.battlelines.add(new WsMessageBase.IOBattleLine(0));
                    } else {
                        int sumPower = 0;
                        for (long generalUuid : kpFormationSet.values()) {
                            GeneralBean generalBean = generalAll.get(generalUuid);
                            //Có lẽ anh hùng đã bị xóa
                            if (generalBean == null) {
                                continue;
                            }
                            //Các sĩ quan có trình độ đào tạo thấp hơn cấp độ 140/6 sao sẽ được tự động nâng cấp lên cấp độ 140/6 sao
                            GeneralBean newGeneral = generalTmpUpgrade(generalBean);
                            sumPower += newGeneral.getPower();
                        }
                        ioOtherPlayer.battlelines.add(new WsMessageBase.IOBattleLine(sumPower));
                    }
                }
                //battle set all
                ioOtherPlayer.kpBattleset = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String kingpvpFormation = "kingpvp" + (i + 1);
                    int battleFormationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, kingpvpFormation);
                    Map<Integer, Long> kpFormationSet = BattleFormationManager.getInstance().getFormationByType(battleFormationIndex, battleFormation);
                    if (kpFormationSet != null) {
                        WsMessageBase.IOBattleSet battleset = new WsMessageBase.IOBattleSet();
                        if (battleFormation.getMythics() != null && battleFormation.getMythics().containsKey(battleFormationIndex)) {
                            battleset.mythic = battleFormation.getMythics().get(battleFormationIndex);
                        }
                        battleset.team = new HashMap<>();
                        int sumPower = 0;
                        for (Map.Entry<Integer, Long> aEntry : kpFormationSet.entrySet()) {
                            GeneralBean generalBean = generalAll.get(aEntry.getValue());
                            //Có lẽ anh hùng đã bị xóa
                            if (generalBean == null) {
                                continue;
                            }
                            if (battleFormationIndex == 9 || battleFormationIndex == 10 || battleFormationIndex == 11) {
                                generalBean = generalTmpUpgrade(generalBean);
                            }
                            sumPower += generalBean.getPower();
                            WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                            battleset.team.put(aEntry.getKey(), ioGeneralBean);
                        }
                        battleset.power = sumPower;
                        ioOtherPlayer.kpBattleset.add(battleset);
                    }
                }
            }
            respmsg.items.add(ioOtherPlayer);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    /**
     * 培养程度低于140级/6星的武将，将自动提升至140级/6星
     *
     * @param generalBean
     * @return
     */
    public static GeneralBean generalTmpUpgrade(GeneralBean generalBean) throws InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        Integer gsid = generalBean.getTemplateId();
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        Integer gStar = generalTemplate.getSTAR();
        if (generalBean.getLevel() >= 140 && gStar >= 6) {
            return generalBean;
        } else {
            GeneralBean newGeneral = (GeneralBean) BeanUtils.cloneBean(generalBean);
            if (generalBean.getLevel() < 140) {
                newGeneral.setLevel(140);
            }
            if (gStar < 6) {
                newGeneral.setStar(6);
            }
            while (gStar < 6) {
                gsid = GeneralAwakeTemplateCache.getInstance().getAwakeGsid(gsid);
                GeneralTemplate generalTemplate1 = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
                gStar = generalTemplate1.getSTAR();
            }
            newGeneral.setTemplateId(gsid);
            GeneralManager.getInstance().updatePropertyAndPower(newGeneral);
            return newGeneral;
        }
    }

}
