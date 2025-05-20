package ws.WsMessageBattle;

import java.util.List;

public class SkillProtocol {
    public static class C2SCraftSkillEffect {
        private int attackerId;
        private int attackerServerId;
        private int attackerHeroId;
        private int attackeeId;
        private int attackeeServerId;
        private int attackeeHeroId;
        private int skillId;
        private int skillLevel;
        private int hurtVal;
        private List<Integer> effectPositionList;
        
        public static final C2SCraftSkillEffect PARSER = new C2SCraftSkillEffect();
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            private C2SCraftSkillEffect message = new C2SCraftSkillEffect();
            
            public Builder setAttackerId(int value) {
                message.attackerId = value;
                return this;
            }
            
            public Builder setAttackerServerId(int value) {
                message.attackerServerId = value;
                return this;
            }
            
            public Builder setAttackerHeroId(int value) {
                message.attackerHeroId = value;
                return this;
            }
            
            public Builder setAttackeeId(int value) {
                message.attackeeId = value;
                return this;
            }
            
            public Builder setAttackeeServerId(int value) {
                message.attackeeServerId = value;
                return this;
            }
            
            public Builder setAttackeeHeroId(int value) {
                message.attackeeHeroId = value;
                return this;
            }
            
            public Builder setSkillId(int value) {
                message.skillId = value;
                return this;
            }
            
            public Builder setSkillLevel(int value) {
                message.skillLevel = value;
                return this;
            }
            
            public Builder setHurtVal(int value) {
                message.hurtVal = value;
                return this;
            }
            
            public Builder setEffectPositionList(List<Integer> value) {
                message.effectPositionList = value;
                return this;
            }
            
            public C2SCraftSkillEffect build() {
                return message;
            }
        }
        
        public int getAttackerId() { return attackerId; }
        public int getAttackerServerId() { return attackerServerId; }
        public int getAttackerHeroId() { return attackerHeroId; }
        public int getAttackeeId() { return attackeeId; }
        public int getAttackeeServerId() { return attackeeServerId; }
        public int getAttackeeHeroId() { return attackeeHeroId; }
        public int getSkillId() { return skillId; }
        public int getSkillLevel() { return skillLevel; }
        public int getHurtVal() { return hurtVal; }
        public List<Integer> getEffectPositionList() { return effectPositionList; }
    }
    
    public static class S2CCraftSkillEffect {
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            public S2CCraftSkillEffect build() {
                return new S2CCraftSkillEffect();
            }
        }
    }
    
    public static class PushCraftAddBuff {
        private int attackerId;
        private int attackerServerId;
        private int attackerHeroId;
        private int attackeeId;
        private int attackeeServerId;
        private int attackeeHeroId;
        private Protocol.PVP_BUFF_TYPE buffType;
        private int hurtVal;
        private int skillId;
        private int skillLevel;
        private List<Integer> effectPositionList;
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static class Builder {
            private PushCraftAddBuff message = new PushCraftAddBuff();
            
            public Builder setAttackerId(int value) {
                message.attackerId = value;
                return this;
            }
            
            public Builder setAttackerServerId(int value) {
                message.attackerServerId = value;
                return this;
            }
            
            public Builder setAttackerHeroId(int value) {
                message.attackerHeroId = value;
                return this;
            }
            
            public Builder setAttackeeId(int value) {
                message.attackeeId = value;
                return this;
            }
            
            public Builder setAttackeeServerId(int value) {
                message.attackeeServerId = value;
                return this;
            }
            
            public Builder setAttackeeHeroId(int value) {
                message.attackeeHeroId = value;
                return this;
            }
            
            public Builder setBuffType(Protocol.PVP_BUFF_TYPE value) {
                message.buffType = value;
                return this;
            }
            
            public Builder setHurtVal(int value) {
                message.hurtVal = value;
                return this;
            }
            
            public Builder setSkillId(int value) {
                message.skillId = value;
                return this;
            }
            
            public Builder setSkillLevel(int value) {
                message.skillLevel = value;
                return this;
            }
            
            public Builder addAllEffectPosition(List<Integer> value) {
                message.effectPositionList = value;
                return this;
            }
            
            public PushCraftAddBuff build() {
                return message;
            }
        }
    }
} 