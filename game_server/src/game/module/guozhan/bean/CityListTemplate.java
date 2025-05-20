package game.module.guozhan.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CityListTemplate {

	@JsonProperty("ChengID")
	private int chengID;
    private int countryType;
    private int pointX;
    private int pointY;
    private String img;
    @JsonProperty("ChengName")
    private String chengName;
    @JsonProperty("Ziyuanlei")
    private int ziyuanlei;

	public int getChengID() {
		return chengID;
	}

	public void setChengID(int chengID) {
		this.chengID = chengID;
	}

	public int getCountryType() {
		return countryType;
	}

    public void setCountryType(int countryType) {
        this.countryType = countryType;
    }
    
    public int getPointX() {
        return pointX;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getChengName() {
        return chengName;
    }

    public void setChengName(String chengName) {
        this.chengName = chengName;
    }

    public int getZiyuanlei() {
        return ziyuanlei;
    }

    public void setZiyuanlei(int ziyuanlei) {
        this.ziyuanlei = ziyuanlei;
    }

	@Override
	public String toString() {
		return "CityListTemplate [ChengID=" + chengID + ", countryType=" + countryType + "]";
	}

}
