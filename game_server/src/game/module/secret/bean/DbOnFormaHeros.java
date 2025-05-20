package game.module.secret.bean;

import java.util.LinkedList;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class DbOnFormaHeros {

	private LinkedList<SecretHeroDb> onFormaHeroList = new LinkedList<SecretHeroDb>();

	public DbOnFormaHeros() {
	}

	public DbOnFormaHeros(LinkedList<SecretHeroDb> objList) {
		this.onFormaHeroList = objList;
	}

	public LinkedList<SecretHeroDb> getOnFormaHeroList() {
		return onFormaHeroList;
	}

	public void setOnFormaHeroList(LinkedList<SecretHeroDb> onFormaHeroList) {
		this.onFormaHeroList = onFormaHeroList;
	}

	public boolean remove(SecretHeroDb hero) {
		if (hero != null) {
			return onFormaHeroList.remove(hero);
		}
		return false;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
