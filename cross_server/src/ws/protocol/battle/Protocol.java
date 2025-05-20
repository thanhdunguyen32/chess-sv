package ws.protocol.battle;

import java.util.List;

public class Protocol {
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
    
    // ... existing code ...
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
} 