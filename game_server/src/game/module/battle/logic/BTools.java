package game.module.battle.logic;

public class BTools {
    private Integer _seed;

    public BTools(Integer _seed) {
        this._seed = _seed;
    }

    public BattleHurt getHurm(int level, int attval, int defval, int extra, float pcr, float vcr, float tinc, float inc, float trdc, float rdc, int c) {
        defval = Math.max(defval, 0);
        float h = attval * (1 - Math.min(.95f, defval / (200f + 20 * (level - 1))) + .7f * extra) * Math.max(1 + tinc + inc - trdc - rdc, 0);
        int d = 0;
        if (2 == c && this.getRandom() < pcr) {
            h *= vcr;
            d = 1;
        }
        return new BattleHurt((int) Math.floor(h), d, vcr);
    }

    public float getRandom() {
        this._seed = (int) ((9301L * this._seed + 49297) % 233280);
        return this._seed / 233280f;
    }

    public static final class BattleHurt {

        public int hurm;
        public int crit;
        public float vcr;

        public BattleHurt(int hurm, int crit, float vcr) {
            this.hurm = hurm;
            this.crit = crit;
            this.vcr = vcr;
        }

    }

}
