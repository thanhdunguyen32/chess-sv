package game.module.battle;

import java.util.List;

public class WsMessageBattle {
    public static enum PVP_PROPERTIES {
        PVP_PROP_HP,
        PVP_PROP_SP
    }
    
    public static enum PVP_STATE {
        PVP_STATE_NONE,
        PVP_STATE_CHASE,
        PVP_STATE_PREPARE_ATTACK,
        PVP_STATE_CAST_SKILL,
        PVP_STATE_DIE
    }
    
    public static enum PVP_BUFF_TYPE {
        PASSIVE,
        ACTION
    }
    
    public static class C2SCraftPropChange {
        private int targetPlayerId;
        private int targetServerId;
        private int targetHeroId;
        private PVP_PROPERTIES propType;
        private int val;
        private int castPlayerId;
        private int castServerId;
        private int castHeroId;
        private boolean isCrit;
        
        public static final C2SCraftPropChange PARSER = new C2SCraftPropChange();
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            private C2SCraftPropChange message = new C2SCraftPropChange();
            
            public Builder setTargetPlayerId(int value) {
                message.targetPlayerId = value;
                return this;
            }
            
            public Builder setTargetServerId(int value) {
                message.targetServerId = value;
                return this;
            }
            
            public Builder setTargetHeroId(int value) {
                message.targetHeroId = value;
                return this;
            }
            
            public Builder setPropType(PVP_PROPERTIES value) {
                message.propType = value;
                return this;
            }
            
            public Builder setVal(int value) {
                message.val = value;
                return this;
            }
            
            public Builder setCastPlayerId(int value) {
                message.castPlayerId = value;
                return this;
            }
            
            public Builder setCastServerId(int value) {
                message.castServerId = value;
                return this;
            }
            
            public Builder setCastHeroId(int value) {
                message.castHeroId = value;
                return this;
            }
            
            public Builder setIsCrit(boolean value) {
                message.isCrit = value;
                return this;
            }
            
            public C2SCraftPropChange build() {
                return message;
            }
        }
        
        public int getTargetPlayerId() { return targetPlayerId; }
        public int getTargetServerId() { return targetServerId; }
        public int getTargetHeroId() { return targetHeroId; }
        public PVP_PROPERTIES getPropType() { return propType; }
        public int getVal() { return val; }
        public int getCastPlayerId() { return castPlayerId; }
        public int getCastServerId() { return castServerId; }
        public int getCastHeroId() { return castHeroId; }
        public boolean getIsCrit() { return isCrit; }
    }
    
    public static class S2CCraftPropChange {
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            public S2CCraftPropChange build() {
                return new S2CCraftPropChange();
            }
        }
    }
    
    public static class PushCraftPropChange {
        private int targetPlayerId;
        private int targetServerId;
        private int targetHeroId;
        private int val;
        private int castPlayerId;
        private int castServerId;
        private int castHeroId;
        private boolean isCrit;
        private boolean showTip;
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            private PushCraftPropChange message = new PushCraftPropChange();
            
            public Builder setTargetPlayerId(int value) {
                message.targetPlayerId = value;
                return this;
            }
            
            public Builder setTargetServerId(int value) {
                message.targetServerId = value;
                return this;
            }
            
            public Builder setTargetHeroId(int value) {
                message.targetHeroId = value;
                return this;
            }
            
            public Builder setVal(int value) {
                message.val = value;
                return this;
            }
            
            public Builder setCastPlayerId(int value) {
                message.castPlayerId = value;
                return this;
            }
            
            public Builder setCastServerId(int value) {
                message.castServerId = value;
                return this;
            }
            
            public Builder setCastHeroId(int value) {
                message.castHeroId = value;
                return this;
            }
            
            public Builder setIsCrit(boolean value) {
                message.isCrit = value;
                return this;
            }
            
            public Builder setShowTip(boolean value) {
                message.showTip = value;
                return this;
            }
            
            public PushCraftPropChange build() {
                return message;
            }
        }
    }
    
    public static class S2CPvpSwitchState {
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            public S2CPvpSwitchState build() {
                return new S2CPvpSwitchState();
            }
        }
    }
    
    public static class C2SCraftSwitchStatus {
        private PVP_STATE targetState;
        private int heroId;
        private int skillId;
        private float d;
        private boolean autoSwitchTarget;
        private List<Integer> targetHeroList;
        
        public static final C2SCraftSwitchStatus PARSER = new C2SCraftSwitchStatus();
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            private C2SCraftSwitchStatus message = new C2SCraftSwitchStatus();
            
            public Builder setTargetState(PVP_STATE value) {
                message.targetState = value;
                return this;
            }
            
            public Builder setHeroId(int value) {
                message.heroId = value;
                return this;
            }
            
            public Builder setSkillId(int value) {
                message.skillId = value;
                return this;
            }
            
            public Builder setD(float value) {
                message.d = value;
                return this;
            }
            
            public Builder setAutoSwitchTarget(boolean value) {
                message.autoSwitchTarget = value;
                return this;
            }
            
            public Builder setTargetHeroList(List<Integer> value) {
                message.targetHeroList = value;
                return this;
            }
            
            public C2SCraftSwitchStatus build() {
                return message;
            }
        }
        
        public PVP_STATE getTargetState() { return targetState; }
        public int getHeroId() { return heroId; }
        public int getSkillId() { return skillId; }
        public float getD() { return d; }
        public boolean getAutoSwitchTarget() { return autoSwitchTarget; }
        public List<Integer> getTargetHeroList() { return targetHeroList; }
    }
    
    public static class S2CCraftSwitchStatus {
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            public S2CCraftSwitchStatus build() {
                return new S2CCraftSwitchStatus();
            }
        }
    }
    
    public static class PushCraftSwitchStatus {
        private int playerId;
        private int serverId;
        private int heroId;
        private PVP_STATE targetState;
        private List<Integer> targetHeroList;
        private boolean autoSwitchTarget;
        private int skillId;
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            private PushCraftSwitchStatus message = new PushCraftSwitchStatus();
            
            public Builder setPlayerId(int value) {
                message.playerId = value;
                return this;
            }
            
            public Builder setServerId(int value) {
                message.serverId = value;
                return this;
            }
            
            public Builder setHeroId(int value) {
                message.heroId = value;
                return this;
            }
            
            public Builder setTargetState(PVP_STATE value) {
                message.targetState = value;
                return this;
            }
            
            public Builder addAllTargetHero(List<Integer> value) {
                message.targetHeroList = value;
                return this;
            }
            
            public Builder setAutoSwitchTarget(boolean value) {
                message.autoSwitchTarget = value;
                return this;
            }
            
            public Builder setSkillId(int value) {
                message.skillId = value;
                return this;
            }
            
            public PushCraftSwitchStatus build() {
                return message;
            }
        }
    }
} 