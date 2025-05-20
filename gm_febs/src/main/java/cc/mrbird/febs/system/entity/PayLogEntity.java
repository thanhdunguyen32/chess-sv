package cc.mrbird.febs.system.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

@Data
@Excel("充值记录")
public class PayLogEntity {

	@ExcelField(value = "玩家id")
	private int playerId;

	@ExcelField(value = "姓名")
	private String playerName;

	@ExcelField(value = "订单id")
	private String orderId;

	@ExcelField(value = "金额（元）")
	private int paySum;

	@ExcelField(value = "时间", writeConverter = TimeConverter.class)
	private Date payTime;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getPaySum() {
		return paySum;
	}

	public void setPaySum(int paySum) {
		this.paySum = paySum;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
