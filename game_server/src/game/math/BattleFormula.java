package game.math;

public class BattleFormula {

	public static float attackCalc(boolean isPhysical, float addedDamage, float attackerAttack,
			float attackeeattackeeArmor, float existArpenetrate, float attackerMagicStrength,
			float attackeeMagicDefence) {
		float realResult = 0;
		if (isPhysical) {
			realResult = checkPhysical(addedDamage + attackerAttack, attackeeattackeeArmor, existArpenetrate);
		} else {
			realResult = checkMagic(addedDamage + attackerMagicStrength, attackeeMagicDefence);
		}
		return realResult;
	}

	private static float checkMagic(float magicStrength, float magicDefence) {
		float damageReduce = 0f;

		if (magicDefence >= 0f && magicDefence <= 30f) {
			damageReduce = magicDefence / 30 * 0.15f;
		} else if (magicDefence <= 60f) {
			damageReduce = 0.15f + (magicDefence - 30) / 30 * 0.12f;
		} else if (magicDefence <= 90f) {
			damageReduce = 0.27f + (magicDefence - 60) / 30 * 0.09f;
		} else if (magicDefence <= 120f) {
			damageReduce = 0.36f + (magicDefence - 90) / 30 * 0.07f;
		} else if (magicDefence <= 150f) {
			damageReduce = 0.43f + (magicDefence - 120) / 30 * 0.05f;
		} else if (magicDefence <= 180f) {
			damageReduce = 0.48f + (magicDefence - 150) / 30 * 0.04f;
		} else if (magicDefence <= 210f) {
			damageReduce = 0.52f + (magicDefence - 180) / 30 * 0.03f;
		} else if (magicDefence <= 240f) {
			damageReduce = 0.55f + (magicDefence - 210) / 30 * 0.025f;
		} else if (magicDefence <= 270f) {
			damageReduce = 0.575f + (magicDefence - 240) / 30 * 0.02f;
		} else {
			damageReduce = 0.595f + (magicDefence - 270) / 80 * 0.03f;
		}

		float damage = magicStrength * (1 - damageReduce);
		return damage;
	}

	private static float checkPhysical(float attackerAttack, float attackeeArmor, float arPenetrate) {
		float damageReduce = 0f;
		if (attackeeArmor >= 0f && attackeeArmor <= 30f) {
			damageReduce = attackeeArmor / 30 * 0.14f;
		} else if (attackeeArmor <= 60f) {
			damageReduce = 0.14f + (attackeeArmor - 30) / 30 * 0.09f;
		} else if (attackeeArmor <= 90f) {
			damageReduce = 0.23f + (attackeeArmor - 60) / 30 * 0.05f;
		} else if (attackeeArmor <= 120f) {
			damageReduce = 0.28f + (attackeeArmor - 90) / 30 * 0.045f;
		} else if (attackeeArmor <= 150f) {
			damageReduce = 0.325f + (attackeeArmor - 120) / 30 * 0.04f;
		} else if (attackeeArmor <= 180f) {
			damageReduce = 0.365f + (attackeeArmor - 150) / 30 * 0.035f;
		} else if (attackeeArmor <= 210f) {
			damageReduce = 0.4f + (attackeeArmor - 180) / 30 * 0.03f;
		} else if (attackeeArmor <= 240f) {
			damageReduce = 0.43f + (attackeeArmor - 210) / 30 * 0.025f;
		} else if (attackeeArmor <= 270f) {
			damageReduce = 0.455f + (attackeeArmor - 240) / 30 * 0.02f;
		} else {
			damageReduce = 0.475f + (attackeeArmor - 270) / 80 * 0.03f;
		}
		float damage = attackerAttack * (arPenetrate + (1000f - arPenetrate) * (1 - damageReduce)) / 1000f;
		return damage;
	}

	public static int guildHeroIncome(int heroBf, int hireCount, long surplusTime) {
		int rentMin = (int) (surplusTime / 1000 / 60);
		int rentIncome = guildHeroRentIncome(heroBf, hireCount);
		if (rentMin < 60) {
			rentIncome = rentMin * rentIncome / 60;
		}
		int timeIncome = guildHeroTimeIncome(heroBf, surplusTime);
		int totalIncome = rentIncome + timeIncome;
		return totalIncome;
	}

	public static int guildHeroTimeIncome(int heroBf, long elapseTime) {
		int rentMin = (int) (elapseTime / 1000 / 60);
		int timeIncome = (int) (heroBf * (rentMin / 180f) + rentMin * 10);
		return timeIncome;
	}

	public static int guildHeroRentIncome(int heroBf, int hireCount) {
		int rentIncome = heroBf * 3 + 5000 * hireCount;
		return rentIncome;
	}

}
