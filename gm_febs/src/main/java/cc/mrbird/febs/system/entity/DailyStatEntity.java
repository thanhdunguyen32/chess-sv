package cc.mrbird.febs.system.entity;

import cc.mrbird.febs.common.converter.DateConverter;
import cc.mrbird.febs.common.converter.PercentConverter;
import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("daily_stat")
@Excel("每日统计")
public class DailyStatEntity implements Serializable {

	@ExcelField(value = "新增")
	private Integer newPlayer;

	@ExcelField(value = "活跃")
	private Integer activePlayer;

	@ExcelField(value = "新增付费人数")
	private Integer newPay;

	@ExcelField(value = "付费人数")
	private Integer payCount;

	@ExcelField(value = "付费金额")
	private Integer paySum;
	/**
	 * 昨日留存
	 */
	@ExcelField(value = "昨日留存",writeConverter = PercentConverter.class)
	private Float yesterdayRetain;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@ExcelField(value = "时间", writeConverter = DateConverter.class)
	private Date statTime;

	@Override
	public String toString() {
		return "DailyStatEntity{" +
				"newPlayer=" + newPlayer +
				", activePlayer=" + activePlayer +
				", newPay=" + newPay +
				", payCount=" + payCount +
				", paySum=" + paySum +
				", yesterdayRetain=" + yesterdayRetain +
				", statTime=" + statTime +
				'}';
	}
}