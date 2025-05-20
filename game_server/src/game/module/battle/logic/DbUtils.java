package game.module.battle.logic;

import game.module.battle.dao.BattlePlayer;
import game.module.battle.dao.BattlePlayerBase;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.template.ChapterBattleTemplate;
import game.module.template.GeneralTemplate;
import ws.WsMessageBase;

public class DbUtils {

    public static BattlePlayer getPveProperInfo(int battleIndex, int pos, ChapterBattleTemplate chapterBattleTemplate) {
        int gsid = chapterBattleTemplate.getGsid();
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        int hp =
                (int) Math.floor((generalTemplate.getPROPERTY().getHP() + generalTemplate.getGROWTH().getHP() * (chapterBattleTemplate.getLevel() - 1))
                        * (1 + generalTemplate.getSHP_ADD() / 1e3f) * (1 + (chapterBattleTemplate.getExhp() != null ? chapterBattleTemplate.getExhp() : 0)));
        if (chapterBattleTemplate.getHpcover() != null && chapterBattleTemplate.getHpcover() > 0) {
            hp = chapterBattleTemplate.getExhp().intValue();
        }
        int atk =
                (int) Math.floor((generalTemplate.getPROPERTY().getATK() + generalTemplate.getGROWTH().getATK() * (chapterBattleTemplate.getLevel() - 1))
                        * (1 + generalTemplate.getSHP_ADD() / 1e3) * (1 + (chapterBattleTemplate.getExatk() != null ? chapterBattleTemplate.getExatk() : 0)));
        int def = (int) (generalTemplate.getPROPERTY().getDEF() + generalTemplate.getGROWTH().getDEF() * (chapterBattleTemplate.getLevel() - 1));
        int mdef = (int) (generalTemplate.getPROPERTY().getMDEF() + generalTemplate.getGROWTH().getMDEF() * (chapterBattleTemplate.getLevel() - 1));
        return new BattlePlayer(battleIndex, chapterBattleTemplate.getGsid(), chapterBattleTemplate.getLevel(), chapterBattleTemplate.getPclass(), pos, hp, hp
                , atk, def, mdef, generalTemplate.getPROPERTY().getATKTIME(), generalTemplate.getPROPERTY().getRANGE(), generalTemplate.getPROPERTY().getMSP(),
                generalTemplate.getPROPERTY().getPCRI(), generalTemplate.getPROPERTY().getPCRID());
    }

    public static BattlePlayerBase getPveProperInfo(int formationpos, int gsid, int level, int pclass, float exhp, float exatk) {
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        int hp = (int) Math.floor((generalTemplate.getPROPERTY().getHP() + generalTemplate.getGROWTH().getHP() * (level - 1))
                * (1 + generalTemplate.getSHP_ADD() / 1e3f) * (1 + exhp));
        int atk = (int) Math.floor((generalTemplate.getPROPERTY().getATK() + generalTemplate.getGROWTH().getATK() * (level - 1))
                * (1 + generalTemplate.getSHP_ADD() / 1e3) * (1 + exatk));
        int def = (int) (generalTemplate.getPROPERTY().getDEF() + generalTemplate.getGROWTH().getDEF() * (level - 1));
        int mdef = (int) (generalTemplate.getPROPERTY().getMDEF() + generalTemplate.getGROWTH().getMDEF() * (level - 1));
        return new BattlePlayerBase(0, gsid, level, pclass, formationpos, hp, hp, atk, def, mdef, generalTemplate.getPROPERTY().getATKTIME(),
                generalTemplate.getPROPERTY().getRANGE(), generalTemplate.getPROPERTY().getMSP(), generalTemplate.getPROPERTY().getPCRI(),
                generalTemplate.getPROPERTY().getPCRID());
    }

    public static int getPropertyPower(WsMessageBase.IOProperty ioProperty) {
        return (int) Math.floor(.0625 * ioProperty.hp + ioProperty.atk + 2.5 * ioProperty.def + 2.5 * ioProperty.mdef + 10 * ioProperty.pasp + 10 * ioProperty.ppthr + 10 * ioProperty.pmthr + 10 * (ioProperty.pcri - 50) + 2 * (ioProperty.pcrid - 1500) + 10 * ioProperty.pdex + 10 * ioProperty.pmdex);
    }

    public static int getRobotPower(BattlePlayerBase battlePlayer) {
        return (int) Math.floor(.0625 * battlePlayer.getHp() + battlePlayer.getAtk() + 2.5 * battlePlayer.getDef() + 2.5 * battlePlayer.getMdef()
                + 10 * 0 + 10 * 0 + 10 * 0 + 10 * (battlePlayer.getPcri() - 50) + 2 * (battlePlayer.getPcrid() - 1500) + 10 * 0 + 10 * 0);
    }

}
