package game.module.battle;

public class ProtoMessageBattle {
    public static class C2SBossPropChange {
        private int targetPlayerId;
        private int targetServerId;
        private int targetHeroId;
        private int val;
        private int castPlayerId;
        private int castServerId;
        private int castHeroId;
        private boolean isCrit;
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            private C2SBossPropChange message = new C2SBossPropChange();
            
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
            
            public C2SBossPropChange build() {
                return message;
            }
        }
    }
    
    public static class PushBossPropChange {
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
            private PushBossPropChange message = new PushBossPropChange();
            
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
            
            public PushBossPropChange build() {
                return message;
            }
        }
    }
} 