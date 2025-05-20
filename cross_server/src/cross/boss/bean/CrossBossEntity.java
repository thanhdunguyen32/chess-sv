package cross.boss.bean;

public class CrossBossEntity {
	
	private int uuid;
	
	private int template_id;
	
	private int hp;
	
	private float[] pos_pair;

	private float[] born_position;
	
	private float speed_up_rate;

	public int getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(int template_id) {
		this.template_id = template_id;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public float[] getPos_pair() {
		return pos_pair;
	}

	public void setPos_pair(float[] pos_pair) {
		this.pos_pair = pos_pair;
	}

	public float[] getBorn_position() {
		return born_position;
	}

	public void setBorn_position(float[] born_position) {
		this.born_position = born_position;
	}

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public float getSpeed_up_rate() {
		return speed_up_rate;
	}

	public void setSpeed_up_rate(float speed_up_rate) {
		this.speed_up_rate = speed_up_rate;
	}
	
}
