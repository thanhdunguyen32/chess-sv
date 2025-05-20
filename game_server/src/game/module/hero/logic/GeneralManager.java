package game.module.hero.logic;

import game.common.IoBeanConvertor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.dao.BattlePlayer;
import game.module.battle.dao.BattlePlayerBase;
import game.module.battle.logic.DbUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import game.module.hero.dao.*;
import game.module.hero_exclusive.dao.ExclusiveLvTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.mission.logic.MissionManager;
import game.module.template.*;
import game.module.user.logic.ScrollAnnoManager;
import game.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHero;

import java.util.*;

public class GeneralManager {

    private static Logger logger = LoggerFactory.getLogger(GeneralManager.class);

    static class SingletonHolder {
        static GeneralManager instance = new GeneralManager();
    }

    public static GeneralManager getInstance() {
        return SingletonHolder.instance;
    }

    public void addHero(PlayingRole playingRole, Integer heroTemplateId, int addCount) {
        int playerId = playingRole.getId();
        logger.info("add hero!player={},heroTemplateId={},count={}", playerId, heroTemplateId, addCount);
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(heroTemplateId);
        for (int i = 0; i < addCount; i++) {
            GeneralBean heroBean = new GeneralBean();
            heroBean.setUuid(SessionManager.getInstance().generateSessionId());
            heroBean.setPlayerId(playerId);
            heroBean.setTemplateId(heroTemplateId);
            heroBean.setLevel(1);
            heroBean.setPclass(0);
            //
            heroBean.setStar(generalTemplate.getSTAR());
            heroBean.setOccu(generalTemplate.getOCCU());
            heroBean.setTreasure(0);
            heroBean.setSkill(new ArrayList<>(Collections.singletonList(generalTemplate.getSKILL().get(0))));
            heroBean.setEquip(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));
            //
            heroBean.setAtktime(generalTemplate.getPROPERTY().getATKTIME());
            heroBean.setRange(generalTemplate.getPROPERTY().getRANGE());
            heroBean.setMsp(generalTemplate.getPROPERTY().getMSP());
            heroBean.setPcri(generalTemplate.getPROPERTY().getPCRI());
            heroBean.setPcrid(generalTemplate.getPROPERTY().getPCRID());
            heroBean.setPasp(0);
            heroBean.setPmdex(0);
            heroBean.setPdam(0);
            heroBean.setPhp(0);
            heroBean.setPatk(0);
            heroBean.setPdef(0);
            heroBean.setPmdef(0);
            heroBean.setPpbs(0);
            heroBean.setPmbs(0);
            heroBean.setPefc(0);
            heroBean.setPpthr(0);
            heroBean.setPatkdam(0);
            heroBean.setPskidam(0);
            heroBean.setPckatk(0);
            heroBean.setPmthr(0);
            heroBean.setPdex(0);
            heroBean.setPmsatk(0);
            heroBean.setPmps(0);
            heroBean.setPcd(0);
            //talent
            heroBean.setTalent(Arrays.asList(0, 0, 0, 0, 0, 0));
            //
            GeneralExclusive generalExclusive = new GeneralExclusive();
            generalExclusive.setGsid(39000);
            generalExclusive.setLevel(0);
            heroBean.setExclusive(generalExclusive);
            //
            updatePropertyAndPower(heroBean);
            //save
            GeneralDaoHelper.asyncInsertHero(heroBean);
            heroCache.put(heroBean.getUuid(), heroBean);
            //push
            WsMessageHero.PushAddGeneral pushmsg = new WsMessageHero.PushAddGeneral();
            WsMessageBase.IOGeneralBean ioGeneralBean = buildIoGeneral(heroBean);
            pushmsg.action = "add";
            pushmsg.general_info = ioGeneralBean;
            playingRole.write(pushmsg.build(playingRole.alloc()));
            //update mission progress
            AwardUtils.changeRes(playingRole, MissionConstants.GET_GENERAL, 1, LogConstants.MODULE_GENERAL);
            MissionManager.getInstance().generalStarChangeProgress(playingRole, generalTemplate.getSTAR());
            if(generalTemplate.getSTAR() >= 5) {
                ScrollAnnoManager.getInstance().getHero(playingRole, playingRole.getPlayerBean().getName(), generalTemplate.getNAME());
            }
        }
    }

    public void removeGeneral(PlayingRole playingRole, long aGeneralGuid) {
        int playerId = playingRole.getId();
        logger.info("sub hero!player={},heroGuid={}", playerId, aGeneralGuid);
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        heroCache.remove(aGeneralGuid);
        GeneralDaoHelper.asyncRemoveHero(aGeneralGuid);
        //push
        WsMessageHero.PushAddGeneral pushmsg = new WsMessageHero.PushAddGeneral();
        WsMessageBase.IOGeneralBean ioGeneralBean = new WsMessageBase.IOGeneralBean();
        ioGeneralBean.guid = aGeneralGuid;
        pushmsg.action = "reduce";
        pushmsg.general_info = ioGeneralBean;
        playingRole.write(pushmsg.build(playingRole.alloc()));
    }

    public void generalDecompAwards(PlayingRole playingRole, GeneralBean generalBean, Map<Integer, Integer> rewardMap, boolean isDo) {
        //star
        Integer generalStar = generalBean.getStar();
        List<RewardTemplateSimple> backRewards = GeneralDecompTemplateCache.getInstance().getConfigByLevel(generalStar);
        //exclusive
        if (generalBean.getExclusive().getLevel() > 0) {
            for (int i = generalBean.getExclusive().getLevel() - 1; i >= 0; i--) {
                ExclusiveLvTemplate exclusiveLvTemplate = ExclusiveLvTemplateCache.getInstance().getExclusiveLvTemplate(i);
                List<RewardTemplateSimple> itemsTemplates = exclusiveLvTemplate.getITEMS();
                putRewards(rewardMap, itemsTemplates);
            }
        }
        putRewards(rewardMap, backRewards);
        //level & pclass & takeoff equip and treasure
        generalResetAwards(playingRole, generalBean, rewardMap, isDo);
    }

    public void generalRebornAwards(PlayingRole playingRole, GeneralBean generalBean, Map<Integer, Integer> rewardMap, boolean isDo) {
        //exclusive
        if (generalBean.getExclusive().getLevel() > 0) {
            for (int i = generalBean.getExclusive().getLevel() - 1; i >= 0; i--) {
                ExclusiveLvTemplate exclusiveLvTemplate = ExclusiveLvTemplateCache.getInstance().getExclusiveLvTemplate(i);
                List<RewardTemplateSimple> itemsTemplates = exclusiveLvTemplate.getITEMS();
                putRewards(rewardMap, itemsTemplates);
            }
        }
        //star
        Integer generalTemplateId = generalBean.getTemplateId();
        GeneralCompTemplate generalCompTemplate = GeneralCompTemplateCache.getInstance().getGeneralCompTemplate(generalTemplateId);
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
        while (generalTemplate.getSTAR() > 5) {
            List<RewardTemplateSimple> costItems = generalCompTemplate.getITEMS();
            if (costItems != null) {
                putRewards(rewardMap, costItems);
            }
            generalTemplateId = generalCompTemplate.getMAIN();
            generalCompTemplate = GeneralCompTemplateCache.getInstance().getGeneralCompTemplate(generalTemplateId);
            generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
        }
        //hero back
        Integer generalStar = generalBean.getStar();
        int gsidCount = GeneralConstants.GENERAL_REBORN_GSID_COUNT[generalStar - 6];
        List<RewardTemplateSimple> rewardsList = new ArrayList<>();
        rewardsList.add(new RewardTemplateSimple(generalTemplateId, gsidCount));
        putRewards(rewardMap, rewardsList);
        //level & pclass & takeoff equip and treasure
        generalResetAwards(playingRole, generalBean, rewardMap, isDo);
    }

    public void generalResetAwards(PlayingRole playingRole, GeneralBean generalBean, Map<Integer, Integer> rewardMap, boolean isDo) {
        //level
        Integer generalLevel = generalBean.getLevel();
        List<List<RewardTemplateSimple>> generalLevelTemplates = GeneralLevelTemplateCache.getInstance().getConfigAll();
        for (int i = 1; i < generalLevel; i++) {
            List<RewardTemplateSimple> rewardTemplateSimples = generalLevelTemplates.get(i - 1);
            putRewards(rewardMap, rewardTemplateSimples);
        }
        //class
        Integer pclass = generalBean.getPclass();
        List<GeneralClassTemplate> generalClassTemplates = GeneralClassTemplateCache.getInstance().getConfigAll();
        for (int i = 0; i < pclass; i++) {
            GeneralClassTemplate generalClassTemplate = generalClassTemplates.get(i + 1);
            putRewards(rewardMap, generalClassTemplate.getITEMS());
        }
        if (isDo) {
            //skill reset
            GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
            generalBean.setSkill(new ArrayList<>(Collections.singletonList(generalTemplate.getSKILL().get(0))));
            //takeoff equip
            List<Integer> equips = generalBean.getEquip();
            if (equips != null && equips.size() > 0) {
                for (int aEquipId : equips) {
                    if (aEquipId > 0) {
                        AwardUtils.changeRes(playingRole, aEquipId, 1, LogConstants.MODULE_GENERAL_DECOMP);
                    }
                }
                for (int i = 0; i < equips.size(); i++) {
                    equips.set(i, 0);
                }
            }
            //takeoff treasure
            if (generalBean.getTreasure() != null && generalBean.getTreasure() > 0) {
                AwardUtils.changeRes(playingRole, generalBean.getTreasure(), 1, LogConstants.MODULE_GENERAL_DECOMP);
                generalBean.setTreasure(0);
            }
        }
    }

    private void putRewards(Map<Integer, Integer> rewardMap, List<RewardTemplateSimple> rewardConfig) {
        for (RewardTemplateSimple rewardTemplateSimple : rewardConfig) {
            Integer gsid = rewardTemplateSimple.getGSID();
            Integer count = rewardTemplateSimple.getCOUNT();
            if (rewardMap.containsKey(gsid)) {
                rewardMap.put(gsid, count + rewardMap.get(gsid));
            } else {
                rewardMap.put(gsid, count);
            }
        }
    }

    public WsMessageBase.IOGeneralBean buildIoGeneral(GeneralBean heroBean) {
        WsMessageBase.IOGeneralBean ioGeneralBean = new WsMessageBase.IOGeneralBean();
        ioGeneralBean.guid = heroBean.getUuid();
        ioGeneralBean.gsid = heroBean.getTemplateId();
        ioGeneralBean.level = heroBean.getLevel();
        ioGeneralBean.star = heroBean.getStar();
        ioGeneralBean.pclass = heroBean.getPclass();
        ioGeneralBean.power = heroBean.getPower();
        //
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(heroBean.getTemplateId());
        ioGeneralBean.camp = heroTemplate.getCAMP();
        ioGeneralBean.occu = heroTemplate.getOCCU();
        ioGeneralBean.treasure = heroBean.getTreasure();
        ioGeneralBean.equip = new ArrayList<>(heroBean.getEquip());
        ioGeneralBean.skill = new ArrayList<>(heroBean.getSkill());
        ioGeneralBean.property = new WsMessageBase.IOProperty(heroBean.getHp(), heroBean.getAtk(), heroBean.getDef(), heroBean.getMdef(), heroBean.getAtktime()
                , heroBean.getRange(), heroBean.getMsp(), heroBean.getPasp(), heroBean.getPcri(), heroBean.getPcrid(), heroBean.getPdam(), heroBean.getPhp(),
                heroBean.getPatk(), heroBean.getPdef(), heroBean.getPmdef(), heroBean.getPpbs(), heroBean.getPmbs(), heroBean.getPefc(), heroBean.getPpthr(),
                heroBean.getPatkdam(), heroBean.getPskidam(), heroBean.getPckatk(), heroBean.getPmthr(), heroBean.getPdex(), heroBean.getPmdex(),
                heroBean.getPmsatk(), heroBean.getPmps(),heroBean.getPcd());
        //
        ioGeneralBean.exclusive = new WsMessageBase.IOExclusive();
        GeneralExclusive generalExclusive = heroBean.getExclusive();
        ioGeneralBean.exclusive.gsid = generalExclusive.getGsid();
        ioGeneralBean.exclusive.level = generalExclusive.getLevel();
        if (generalExclusive.getSkill() != null) {
            ioGeneralBean.exclusive.skill = new ArrayList<>(generalExclusive.getSkill());
        }
        if (generalExclusive.getProperty() != null) {
            ioGeneralBean.exclusive.property = IoBeanConvertor.map2Pairs(generalExclusive.getProperty());
        }
        ioGeneralBean.talent = heroBean.getTalent();
        ioGeneralBean.hppercent = 10000;
        return ioGeneralBean;
    }

    public WsMessageBase.IOGeneralBean buildIoGeneral(ChapterBattleTemplate chapterBattleTemplate) {
        WsMessageBase.IOGeneralBean ioGeneralBean = new WsMessageBase.IOGeneralBean();
        ioGeneralBean.guid = 0;
        ioGeneralBean.gsid = chapterBattleTemplate.getGsid();
        ioGeneralBean.level = chapterBattleTemplate.getLevel();
        ioGeneralBean.pclass = chapterBattleTemplate.getPclass();
        //
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(chapterBattleTemplate.getGsid());
        ioGeneralBean.star = heroTemplate.getSTAR();
        ioGeneralBean.camp = heroTemplate.getCAMP();
        ioGeneralBean.occu = heroTemplate.getOCCU();
        ioGeneralBean.treasure = 0;
        ioGeneralBean.equip = null;
        //skill
        int pclass = chapterBattleTemplate.getPclass();
        GeneralClassTemplate generalClassTemplate = GeneralClassTemplateCache.getInstance().getConfigByLevel(pclass);
        Integer unlockSkillIndex = generalClassTemplate.getUNLOCK();
        ioGeneralBean.skill = new ArrayList<>();
        for (int i = 0; i < unlockSkillIndex && i < heroTemplate.getSKILL().size(); i++) {
            ioGeneralBean.skill.add(heroTemplate.getSKILL().get(i));
        }
        //property
        BattlePlayer pveProperInfo = DbUtils.getPveProperInfo(0, 0, chapterBattleTemplate);
        ioGeneralBean.property = new WsMessageBase.IOProperty(pveProperInfo.getHp(), pveProperInfo.getAtk(), pveProperInfo.getDef(), pveProperInfo.getMdef(),
                heroTemplate.getPROPERTY().getATKTIME(), heroTemplate.getPROPERTY().getRANGE(), heroTemplate.getPROPERTY().getMSP(), 0,
                heroTemplate.getPROPERTY().getPCRI(), heroTemplate.getPROPERTY().getPCRID(), 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,0,0);
        //
        ioGeneralBean.power = DbUtils.getPropertyPower(ioGeneralBean.property);
        ioGeneralBean.exclusive = null;
        ioGeneralBean.talent = null;
        ioGeneralBean.hppercent = 10000;
        return ioGeneralBean;
    }

    public WsMessageBase.IOGeneralBean buildIoGeneral(BattlePlayerBase battlePlayer) {
        WsMessageBase.IOGeneralBean ioGeneralBean = new WsMessageBase.IOGeneralBean();
        ioGeneralBean.guid = 0;
        ioGeneralBean.gsid = battlePlayer.getGsid();
        ioGeneralBean.level = battlePlayer.getLevel();
        ioGeneralBean.pclass = battlePlayer.getPclass();
        //
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(battlePlayer.getGsid());
        ioGeneralBean.star = heroTemplate.getSTAR();
        ioGeneralBean.camp = heroTemplate.getCAMP();
        ioGeneralBean.occu = heroTemplate.getOCCU();
        ioGeneralBean.treasure = 0;
        ioGeneralBean.equip = null;
        //skill
        int pclass = battlePlayer.getPclass();
        GeneralClassTemplate generalClassTemplate = GeneralClassTemplateCache.getInstance().getConfigByLevel(pclass);
        Integer unlockSkillIndex = generalClassTemplate.getUNLOCK();
        ioGeneralBean.skill = new ArrayList<>();
        for (int i = 0; i < unlockSkillIndex && i < heroTemplate.getSKILL().size(); i++) {
            ioGeneralBean.skill.add(heroTemplate.getSKILL().get(i));
        }
        //property
        ioGeneralBean.property = new WsMessageBase.IOProperty(battlePlayer.getHp(), battlePlayer.getAtk(), battlePlayer.getDef(), battlePlayer.getMdef(),
                heroTemplate.getPROPERTY().getATKTIME(), heroTemplate.getPROPERTY().getRANGE(), heroTemplate.getPROPERTY().getMSP(), 0,
                heroTemplate.getPROPERTY().getPCRI(), heroTemplate.getPROPERTY().getPCRID(), 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,0,0);
        //
        ioGeneralBean.power = DbUtils.getPropertyPower(ioGeneralBean.property);
        ioGeneralBean.exclusive = null;
        ioGeneralBean.talent = null;
        ioGeneralBean.hppercent = 10000;
        return ioGeneralBean;
    }

    public void updatePropertyAndPower(GeneralBean generalBean) {
        //init hp atk def mdef
        GeneralTemplateCache.getInstance().calcGeneralProperty(generalBean);
        //calc power
        int generalPower = GeneralTemplateCache.getInstance().getGeneralPower(generalBean);
        generalBean.setPower(generalPower);
    }

}
