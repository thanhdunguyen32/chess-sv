package game.module.spin.logic;

import game.module.spin.bean.SpinBean;
import game.module.spin.dao.MySpinTemplateCache;
import game.module.template.MySpineTemplate;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SpinManager {

    private static final Logger logger = LoggerFactory.getLogger(SpinManager.class);

    static class SingletonHolder {

        static SpinManager instance = new SpinManager();
    }

    public static SpinManager getInstance() {
        return SingletonHolder.instance;
    }

    public SpinBean createSpin(int playerId, int type) {
        SpinBean sb = new SpinBean();
        sb.setPlayerId(playerId);
        List<List<Integer>> rewardlist = generateSpinRewards(type);
        if (type == 1) {
            sb.setRewardsNormal(rewardlist);
        } else {
            sb.setRewardsAdvance(rewardlist);
        }
        return sb;
    }

    public List<List<Integer>> generateSpinRewards(int type) {
        MySpineTemplate mySpineTemplate = MySpinTemplateCache.getInstance().getSpinByType(type);
        List<List<List<Integer>>> drops = mySpineTemplate.getDrops();
        List<List<Integer>> retlist = new ArrayList<>();
        for (List<List<Integer>> aItemConfg : drops) {
            if (aItemConfg.size() == 1) {
                retlist.add(aItemConfg.get(0));
            } else {
                int randIndex = RandomUtils.nextInt(0, aItemConfg.size());
                List<Integer> targetConfig = aItemConfg.get(randIndex);
                retlist.add(targetConfig);
            }
        }
        return retlist;
    }

}
