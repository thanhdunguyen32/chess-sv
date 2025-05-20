package game.module.battle.dao;

public class BattlePlayer extends BattlePlayerBase {

    private BattlePlayer targetPlayer;

    public BattlePlayer getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(BattlePlayer targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public BattlePlayer(int battleIndex, int gsid,int level,int pclass, int pos, int maxhp, int hp, int atk, int def, int mdef, float atktime, int range,
                        int msp, int pcri, int pcrid) {
        super(battleIndex, gsid,level,pclass, pos, maxhp, hp, atk, def, mdef, atktime, range, msp, pcri, pcrid);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
