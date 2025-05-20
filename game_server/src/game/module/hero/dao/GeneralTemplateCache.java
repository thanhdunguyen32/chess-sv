package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import game.module.item.dao.EquipSetTemplateCache;
import game.module.item.dao.EquipTemplateCache;
import game.module.item.dao.TreasureTemplateCache;
import game.module.legion.dao.LegionTechTemplateCache;
import game.module.legion.logic.LegionManager;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorTemplateCache;
import game.module.manor.dao.OfficialItemsTemplateCache;
import game.module.template.*;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.HeadFrameTemplateCache;
import game.module.user.dao.PlayerHideCache;
import game.module.user.logic.PlayerHeadManager;
import game.session.SessionManager;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(GeneralTemplateCache.class);

    static class SingletonHolder {
        static GeneralTemplateCache instance = new GeneralTemplateCache();
    }

    public static GeneralTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, GeneralTemplate> templateMap;

    private volatile Map<Integer, List<Integer>> starTemplateMap;

    private volatile Map<String, Integer> nameTemplateMap;

    public static final String[] OCCU_NAME = {"Chiến","Thích","Xạ","Quân","Mưu"};

    @Override
    public void reload() {
        try {
            String fileName = GeneralTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, GeneralTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<Map<Integer, GeneralTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            nameTemplateMap = new HashMap<>();
            for (int aIconId : PlayerHeadManager.ICONS) {
                GeneralTemplate generalTemplate = templateWrapperMap.get(aIconId);
                nameTemplateMap.put(generalTemplate.getNAME(), aIconId);
            }
            for (GeneralTemplate generalTemplate : templateWrapperMap.values()) {
                if (generalTemplate.getSTAR() == 5 && !nameTemplateMap.containsKey(generalTemplate.getNAME())) {
                    nameTemplateMap.put(generalTemplate.getNAME(), generalTemplate.getGSID());
                }
            }
            starTemplateMap = new HashMap<>();
            for (GeneralTemplate generalTemplate : templateWrapperMap.values()) {
                Integer generalTemplateSTAR = generalTemplate.getSTAR();
                if (!starTemplateMap.containsKey(generalTemplateSTAR)) {
                    List<Integer> gsidList = new ArrayList<>();
                    starTemplateMap.put(generalTemplateSTAR, gsidList);
                }
                starTemplateMap.get(generalTemplateSTAR).add(generalTemplate.getGSID());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getIconIdByName(String generalName) {
        return nameTemplateMap.get(generalName);
    }

    public boolean containsId(int id) {
        return templateMap.containsKey(id);
    }

    public GeneralTemplate getHeroTemplate(int id) {
        return templateMap.get(id);
    }

    public List<Integer> getGeneralByStar(int aStar) {
        return starTemplateMap.get(aStar);
    }

    public static void main(String[] args) {
        GeneralTemplateCache.getInstance().reload();
        boolean ret1 = GeneralTemplateCache.getInstance().containsId(192);
        System.out.println(ret1);
    }

    public void calcGeneralProperty(GeneralBean generalBean) {
        int gsid = generalBean.getTemplateId();
        GeneralTemplate generalTemplate = templateMap.get(gsid);
        HeroTemplateProperty propertyTemplate = generalTemplate.getPROPERTY();
        //DBUtils#getPveProperInfo
        //equip
        List<Integer> equips = generalBean.getEquip();
        Map<String, Integer> addonAll = new HashMap<>();
        if (equips != null) {
            Map<Integer, Integer> equipSetMap = new HashMap<>();
            for (int aEquipId : equips) {
                if (aEquipId == 0) {
                    continue;
                }
                EquipTemplate equipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(aEquipId);
                List<KVTemplate> propList = equipTemplate.getPROP();
                for (KVTemplate kvTemplate : propList) {
                    putProperty(addonAll, kvTemplate.getKEY(), kvTemplate.getVAL());
                }
                //equip set addon
                Integer setid = equipTemplate.getSETID();
                if (setid != null) {
                    EquipSetTemplate equipSetTemplate = EquipSetTemplateCache.getInstance().getEquipSetTemplateById(setid);
                    List<Integer> equipSets = equipSetTemplate.getEQUIP();
                    int setNum = 0;
                    for (int aSetId : equipSets) {
                        for (int myEquipId : equips) {
                            if (myEquipId == aSetId) {
                                setNum++;
                                break;
                            }
                        }
                    }
                    //put map
                    equipSetMap.put(setid, setNum);
                }
            }
            //套装加成
            for (Map.Entry<Integer, Integer> aEntry : equipSetMap.entrySet()) {
                Integer setid = aEntry.getKey();
                Integer setNum = aEntry.getValue();
                EquipSetTemplate equipSetTemplate = EquipSetTemplateCache.getInstance().getEquipSetTemplateById(setid);
                List<KVTemplateNum> propertyList = equipSetTemplate.getPROPERTY();
                for (int i = propertyList.size() - 1; i >= 0; i--) {
                    KVTemplateNum kvTemplateNum = propertyList.get(i);
                    if (setNum >= kvTemplateNum.getNUM()) {
                        putProperty(addonAll, kvTemplateNum.getKEY(), kvTemplateNum.getVAL());
                        break;
                    }
                }
            }
        }
        //treasure
        Integer treasureId = generalBean.getTreasure();
        if (treasureId != null && treasureId > 0) {
            TreasureTemplate treasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(treasureId);
            List<KVTemplate> propList = treasureTemplate.getPROP();
            for (KVTemplate kvTemplate : propList) {
                putProperty(addonAll, kvTemplate.getKEY(), kvTemplate.getVAL());
            }
        }
        //exclusive
        GeneralExclusive generalExclusive = generalBean.getExclusive();
        if (generalExclusive.getProperty() != null) {
            for (Map.Entry<String, Integer> aEntry : generalExclusive.getProperty().entrySet()) {
                putProperty(addonAll, aEntry.getKey(), aEntry.getValue());
            }
        }
        Integer addonHp = addonAll.get("HP");
        addonHp = addonHp != null ? addonHp : 0;
        Integer addonATK = addonAll.get("ATK");
        addonATK = addonATK != null ? addonATK : 0;
        Integer addonDEF = addonAll.get("DEF");
        addonDEF = addonDEF != null ? addonDEF : 0;
        Integer addonMDEF = addonAll.get("MDEF");
        addonMDEF = addonMDEF != null ? addonMDEF : 0;
        Integer addonPHP = addonAll.get("PHP");
        addonPHP = addonPHP != null ? addonPHP : 0;
        Integer addonPATK = addonAll.get("PATK");
        addonPATK = addonPATK != null ? addonPATK : 0;
        Integer addonPDEF = addonAll.get("PDEF");
        addonPDEF = addonPDEF != null ? addonPDEF : 0;
        Integer addonPMDEF = addonAll.get("PMDEF");
        addonPMDEF = addonPMDEF != null ? addonPMDEF : 0;
        //star
        int starAddPhp = generalTemplate.getSHP_ADD();
        int starAddPatk = generalTemplate.getSATK_ADD();
        //class
        Integer pclass = generalBean.getPclass();
        GeneralClassTemplate classTemplate = GeneralClassTemplateCache.getInstance().getConfigByLevel(pclass);
        GeneralClassTemplate.GeneralClassTemplateAdd generalClassTemplateAdd = classTemplate.getADD();
        Integer classAddPhp = generalClassTemplateAdd.getHP();
        Integer classAddPatk = generalClassTemplateAdd.getATK();
        Integer classAddPdef = generalClassTemplateAdd.getDEF();
        Integer classAddPmdef = generalClassTemplateAdd.getMDEF();
        //军团建筑加成
        Map<Integer, Integer> legionOfficalManorAddon = new HashMap<>();
        int playerId = generalBean.getPlayerId();
        Map<Integer, Integer> legionTech = LegionManager.getInstance().getLegionTech(playerId);
        if (legionTech != null) {
            for (Map.Entry<Integer, Integer> aEntry : legionTech.entrySet()) {
                int techId = aEntry.getKey();
                Integer techLv = aEntry.getValue();
                LegionTechTemplate legionTechTemplate = LegionTechTemplateCache.getInstance().getLegionTechTemplate(techId);
                if (legionTechTemplate.getOCCU().equals(generalBean.getOccu())) {
                    Map<Integer, List<Integer>> attr = legionTechTemplate.getATTR();
                    for (Map.Entry<Integer, List<Integer>> entry2 : attr.entrySet()) {
                        Integer attrId = entry2.getKey();
                        List<Integer> attrlist = entry2.getValue();
                        putAddon(legionOfficalManorAddon, attrId, attrlist.get(techLv - 1));
                    }
                }
            }
        }
        //官职加成
        Map<Integer, PlayerProp> playerHidden = PlayerHideCache.getInstance().getPlayerHidden(playerId);
        int myOfficial = 0;
        if (playerHidden != null && playerHidden.containsKey(GameConfig.PLAYER.OFFICIAL)) {
            myOfficial = playerHidden.get(GameConfig.PLAYER.OFFICIAL).getCount();
        }
        OfficialItemsTemplate officialItemsTemplate = OfficialItemsTemplateCache.getInstance().getOfficialItemsTemplate(myOfficial);
        List<KVTemplate> attr = officialItemsTemplate.getATTR();
        for (KVTemplate kvTemplate : attr) {
            putAddon(legionOfficalManorAddon, Integer.valueOf(kvTemplate.getKEY()), kvTemplate.getVAL());
        }
        //领地建筑加成
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        if (manorBean != null && manorBean.getManorBuilding() != null) {
            Map<Integer, ManorBean.DbManorBuilding1> buildings = manorBean.getManorBuilding().getBuildings();
            for (ManorBean.DbManorBuilding1 dbManorBuilding1 : buildings.values()) {
                Integer buildingId = dbManorBuilding1.getId();
                Integer buildingLevel = dbManorBuilding1.getLevel();
                ManorTemplate.ManorUpTemplate upTemplate = ManorTemplateCache.getInstance().getUpTemplate(buildingId);
                List<ManorTemplate.IdNumTemplate> att = upTemplate.getINFO().get(buildingLevel).getATT();
                if (att != null) {
                    for (ManorTemplate.IdNumTemplate idNumTemplate : att) {
                        if (StringUtils.isNumeric(idNumTemplate.getID())) {
                            putAddon(legionOfficalManorAddon, Integer.parseInt(idNumTemplate.getID()), idNumTemplate.getNUM());
                        }
                    }
                }
            }
        }
        //头像框加成
        PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
        if (playingRole != null) {
            Integer frameid = playingRole.getPlayerBean().getFrameid();
            HeadFrameTemplate headFrameTemplate = HeadFrameTemplateCache.getInstance().getHeadFrameTemplate(frameid);
            if (headFrameTemplate.getARRATT() != null && headFrameTemplate.getARRATT().size() > 0) {
                for (HeadFrameTemplate.AttTemplate attTemplate : headFrameTemplate.getARRATT()) {
                    putAddon(legionOfficalManorAddon, attTemplate.getATT(), attTemplate.getCOUNT());
                }
            }
        }
        //addon sum
        for (Map.Entry<Integer, Integer> aEntry : legionOfficalManorAddon.entrySet()) {
            Integer addonType = aEntry.getKey();
            Integer addonVal = aEntry.getValue();
            if (addonType.equals(301)) {
                addonPHP += addonVal;
            } else if (addonType.equals(302)) {
                addonPATK += addonVal;
            } else if (addonType.equals(201)) {//PMPS   能量恢复速度
                putProperty(addonAll, "PMPS", addonVal);
            } else if (addonType.equals(202)) {//PASP   攻击速度
                putProperty(addonAll, "PASP", addonVal);
            } else if (addonType.equals(205)) {//PCRI   暴击率
                putProperty(addonAll, "PCRI", addonVal);
            } else if (addonType.equals(211)) {//PDAM   伤害加成
                putProperty(addonAll, "PDAM", addonVal);
            } else if (addonType.equals(215)) {//PCD    冷却缩减
                putProperty(addonAll, "PCD", addonVal);
            }
        }

        generalBean.setHp((int) Math.floor((propertyTemplate.getHP() + generalTemplate.getGROWTH().getHP() * (generalBean.getLevel() - 1))
                * (1 + classAddPhp / 1000f) * (1 + starAddPhp / 1000f) * (1 + addonPHP / 1000f) + addonHp));
        generalBean.setAtk((int) Math.floor((propertyTemplate.getATK() + generalTemplate.getGROWTH().getATK() * (generalBean.getLevel() - 1))
                * (1 + classAddPatk / 1000f) * (1 + starAddPatk / 1000f) * (1 + addonPATK / 1000f) + addonATK));
        generalBean.setDef((int) Math.floor((propertyTemplate.getDEF() + generalTemplate.getGROWTH().getDEF() * (generalBean.getLevel() - 1))
                * (1 + classAddPdef / 1000f) * (1 + addonPDEF / 1000f) + addonDEF));
        generalBean.setMdef((int) Math.floor((propertyTemplate.getMDEF() + generalTemplate.getGROWTH().getMDEF() * (generalBean.getLevel() - 1))
                * (1 + classAddPmdef / 1000f) * (1 + addonPMDEF / 1000f) + addonMDEF));
        //property reset
        resetGeneralProperty(generalBean, generalTemplate);
        //other property1
        generalBean.setPhp(addonPHP + classAddPhp + starAddPhp);
        generalBean.setPatk(addonPATK + classAddPatk + starAddPatk);
        generalBean.setPdef(addonPDEF + classAddPdef);
        generalBean.setPmdef(addonPMDEF + classAddPmdef);
        //other property2
        for (Map.Entry<String, Integer> aEntry : addonAll.entrySet()) {
            addGeneralProperty(generalBean, aEntry.getKey(), aEntry.getValue());
        }
    }

    private void putAddon(Map<Integer, Integer> addonMap, Integer attrId, Integer attrVal) {
        if (addonMap.containsKey(attrId)) {
            addonMap.put(attrId, addonMap.get(attrId) + attrVal);
        } else {
            addonMap.put(attrId, attrVal);
        }
    }

    public void resetGeneralProperty(GeneralBean generalBean, GeneralTemplate generalTemplate) {
        generalBean.setPhp(0);
        generalBean.setPatk(0);
        generalBean.setPdef(0);
        generalBean.setPmdef(0);
        generalBean.setAtktime(generalTemplate.getPROPERTY().getATKTIME());
        generalBean.setRange(generalTemplate.getPROPERTY().getRANGE());
        generalBean.setMsp(generalTemplate.getPROPERTY().getMSP());
        generalBean.setPcri(generalTemplate.getPROPERTY().getPCRI());
        generalBean.setPcrid(generalTemplate.getPROPERTY().getPCRID());
        generalBean.setPasp(0);
        generalBean.setPpthr(0);
        generalBean.setPmthr(0);
        generalBean.setPdex(0);
        generalBean.setPmdex(0);
        generalBean.setPatkdam(0);
        generalBean.setPskidam(0);
        generalBean.setPefc(0);
        generalBean.setPpbs(0);
        generalBean.setPmbs(0);
        generalBean.setPckatk(0);
        generalBean.setPmsatk(0);
        generalBean.setPmps(0);
        generalBean.setPcd(0);
    }

    public void addGeneralProperty(GeneralBean generalBean, String propertyKey, Integer propertyVal) {
        switch (propertyKey) {
            case "RANGE":
                generalBean.setRange(generalBean.getRange() + propertyVal);
                break;
            case "ATKTIME":
                generalBean.setAtktime(generalBean.getAtktime() + propertyVal);
                break;
            case "MSP":
                generalBean.setMsp(generalBean.getMsp() + propertyVal);
                break;
            case "PASP":
                generalBean.setPasp(generalBean.getPasp() + propertyVal);
                break;
            case "PPTHR":
                generalBean.setPpthr(generalBean.getPpthr() + propertyVal);
                break;
            case "PMTHR":
                generalBean.setPmthr(generalBean.getPmthr() + propertyVal);
                break;
            case "PCRI":
                generalBean.setPcri(generalBean.getPcri() + propertyVal);
                break;
            case "PCRID":
                generalBean.setPcrid(generalBean.getPcrid() + propertyVal);
                break;
            case "PDEX":
                generalBean.setPdex(generalBean.getPdex() + propertyVal);
                break;
            case "PMDEX":
                generalBean.setPmdex(generalBean.getPmdex() + propertyVal);
                break;
            case "PATKDAM":
                generalBean.setPatkdam(generalBean.getPatkdam() + propertyVal);
                break;
            case "PSKIDAM":
                generalBean.setPskidam(generalBean.getPskidam() + propertyVal);
                break;
            case "PEFC":
                generalBean.setPefc(generalBean.getPefc() + propertyVal);
                break;
            case "PPBS":
                generalBean.setPpbs(generalBean.getPpbs() + propertyVal);
                break;
            case "PMBS":
                generalBean.setPmbs(generalBean.getPmbs() + propertyVal);
                break;
            case "PCKATK":
                generalBean.setPckatk(generalBean.getPckatk() + propertyVal);
                break;
            case "PMSATK":
                generalBean.setPmsatk(generalBean.getPmsatk() + propertyVal);
                break;
            case "PDAM":
                generalBean.setPdam(generalBean.getPdam() + propertyVal);
                break;
            case "PMPS":
                generalBean.setPmps(generalBean.getPmps() + propertyVal);
                break;
            case "PCD":
                generalBean.setPcd(generalBean.getPcd() + propertyVal);
                break;
        }
    }

    private void putProperty(Map<String, Integer> addonAll, String propKey, Integer propVal) {
        if (addonAll.containsKey(propKey)) {
            addonAll.put(propKey, addonAll.get(propKey) + propVal);
        } else {
            addonAll.put(propKey, propVal);
        }
    }

    public int getGeneralPower(GeneralBean a) {
        int ret =
                (int) Math.floor(0.0625f * a.getHp() + a.getAtk() + 2.5f * a.getDef() + 2.5f * a.getMdef() + 10 * (a.getPasp() != null ? a.getPasp() : 0) + 10 *
                        (a.getPpthr() != null ? a.getPpthr() : 0) + 10 * (a.getPmthr() != null ? a.getPmthr() : 0) + 10 * ((a.getPcri() != null ?
                        a.getPcri() : 0) - 50) + 2 * ((a.getPcrid() != null ? a.getPcrid() : 0) - 1500) + 10 * (a.getPdex() != null ? a.getPdex() : 0) +
                        10 * (a.getPmdex() != null ? a.getPmdex() : 0));
        return ret;
    }

    public String getOccuCn(int occu){
        return OCCU_NAME[occu-1];
    }

}