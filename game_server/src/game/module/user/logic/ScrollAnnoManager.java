package game.module.user.logic;

import game.entity.PlayingRole;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.item.dao.EquipTemplateCache;
import game.module.item.dao.TreasureTemplateCache;
import game.module.template.EquipTemplate;
import game.module.template.ExclusiveTemplate;
import game.module.template.GeneralTemplate;
import game.module.template.TreasureTemplate;
import game.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Collection;

public class ScrollAnnoManager {

    private static Logger logger = LoggerFactory.getLogger(ScrollAnnoManager.class);

    static class SingletonHolder {

        static ScrollAnnoManager instance = new ScrollAnnoManager();
    }

    public static ScrollAnnoManager getInstance() {
        return SingletonHolder.instance;
    }

    public void getHero(PlayingRole playingRole, String playerName, String generalName) {
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 2;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playerName);
        int gsid = GeneralTemplateCache.getInstance().getIconIdByName(generalName);
        GeneralTemplate template = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        pushScrollAnno.params.add(template.getNAME_VN() != null ? template.getNAME_VN() : template.getNAME());
        send2All(pushScrollAnno);
    }

    public void heroAddStar(PlayingRole playingRole, String playerName, String generalName, int star) {
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 1;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playerName);
        int gsid = GeneralTemplateCache.getInstance().getIconIdByName(generalName);
        GeneralTemplate template = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        pushScrollAnno.params.add(template.getNAME_VN() != null ? template.getNAME_VN() : template.getNAME());
        pushScrollAnno.params.add(String.valueOf(star));
        send2All(pushScrollAnno);
    }

    public void equipQuality(PlayingRole playingRole, int gsid) {
        EquipTemplate equipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(gsid);
        if (equipTemplate.getQUALITY() < 5) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 3;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playingRole.getPlayerBean().getName());
        pushScrollAnno.params.add("橙");
        pushScrollAnno.params.add(equipTemplate.getNAME());
        send2All(pushScrollAnno);
    }

    public void equipStar(PlayingRole playingRole, int gsid) {
        EquipTemplate equipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(gsid);
        if (equipTemplate.getSTAR() < 5) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 4;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playingRole.getPlayerBean().getName());
        pushScrollAnno.params.add(String.valueOf(equipTemplate.getSTAR()));
        pushScrollAnno.params.add(equipTemplate.getNAME());
        send2All(pushScrollAnno);
    }

    public void treasure(PlayingRole playingRole, int gsid) {
        TreasureTemplate treasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(gsid);
        if (treasureTemplate.getSTAR() < 5) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 5;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playingRole.getPlayerBean().getName());
        pushScrollAnno.params.add(treasureTemplate.getSTAR() == 5 ? "橙" : "红");
        pushScrollAnno.params.add(treasureTemplate.getNAME());
        send2All(pushScrollAnno);
    }

    public void exclusiveAddStar(PlayingRole playingRole, int generalGsid, ExclusiveTemplate exclusiveTemplate) {
        if (exclusiveTemplate.getQUALITY() < 6) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 6;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playingRole.getPlayerBean().getName());
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalGsid);
        pushScrollAnno.params.add(heroTemplate.getNAME_VN() != null ? heroTemplate.getNAME_VN() : heroTemplate.getNAME());
        pushScrollAnno.params.add(exclusiveTemplate.getNAME());
        send2All(pushScrollAnno);
    }

    public void tower(PlayingRole playingRole, int towerLevel) {
        if (towerLevel >= 100 && towerLevel % 30 == 0) {
            WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
            pushScrollAnno.msg_id = 7;
            pushScrollAnno.params = new ArrayList<>();
            pushScrollAnno.params.add(playingRole.getPlayerBean().getName());
            pushScrollAnno.params.add(String.valueOf(towerLevel));
            send2All(pushScrollAnno);
        }
    }

    public void pvp(PlayingRole playingRole, int pvpRank) {
        if (pvpRank > 10) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 8;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playingRole.getPlayerBean().getName());
        pushScrollAnno.params.add(String.valueOf(pvpRank));
        send2All(pushScrollAnno);
    }

    public void exped(PlayingRole playingRole, int expedLevel) {
        if (expedLevel % 15 != 0) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 16;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playingRole.getPlayerBean().getName());
        pushScrollAnno.params.add(String.valueOf(expedLevel));
        send2All(pushScrollAnno);
    }

    public void legionLevelup(String legionName, int legionLevel) {
        if (legionLevel < 5) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 17;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(legionName);
        pushScrollAnno.params.add(String.valueOf(legionLevel));
        send2All(pushScrollAnno);
    }

    public void heroAddLevel(PlayingRole playingRole, String playerName, String generalName, int level) {
        if (level < 140 || level % 10 != 0) {
            return;
        }
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 18;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playerName);
        int gsid = GeneralTemplateCache.getInstance().getIconIdByName(generalName);
        GeneralTemplate template = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        pushScrollAnno.params.add(template.getNAME_VN() != null ? template.getNAME_VN() : template.getNAME());
        pushScrollAnno.params.add(String.valueOf(level));
        send2All(pushScrollAnno);
    }

    public void kingOnline(String playerName) {
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 13;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(playerName);
        send2All(pushScrollAnno);
    }

    public void guozhanPvpPass(String nationName) {
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id = 14;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(nationName);
        send2All(pushScrollAnno);
    }

    public void sendGmAnnouncement(String content) {
        WsMessageHall.PushScrollAnno pushScrollAnno = new WsMessageHall.PushScrollAnno();
        pushScrollAnno.msg_id  = 19;
        pushScrollAnno.params = new ArrayList<>();
        pushScrollAnno.params.add(content);
        send2All(pushScrollAnno);
    }

    private void send2All(WsMessageHall.PushScrollAnno pushScrollAnno) {
        Collection<PlayingRole> players = SessionManager.getInstance().getAllPlayers();
        if (players == null || players.isEmpty()) {
            return;
        }
        
        for (PlayingRole aPlayingRole : players) {
            try {
                if (aPlayingRole != null && aPlayingRole.isChannelActive()) {
                    aPlayingRole.writeAndFlush(pushScrollAnno.build(aPlayingRole.alloc()));
                    
                }
            } catch (Exception e) {
                logger.error("Error sending scroll anno to player {}: {}", 
                    aPlayingRole != null ? aPlayingRole.getId() : "null", e.getMessage());
            }
        }
    }

}
