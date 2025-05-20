package cc.mrbird.febs.system.entity;

import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class Activity4Gm {

	private Integer templateId;

	private Date startTime;

	private Date endTime;

	private String title;

	private String description;

	private Integer isOpen;

	private Integer validDayCount;

	private byte[] params;

	private String startDateStr;

	private String startTimeStr;

	private String endDateStr;

	private String endTimeStr;

	private String isOpenStr;

	private String paramsStr;

	private String openTime;

	private String closeTime;
	
	private List<Integer> selected_gs;

	private String isReset;

	@Override
	public String toString() {
		return "Activity4Gm{" +
				"templateId=" + templateId +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", isOpen=" + isOpen +
				", validDayCount=" + validDayCount +
				", params=" + Arrays.toString(params) +
				", startDateStr='" + startDateStr + '\'' +
				", startTimeStr='" + startTimeStr + '\'' +
				", endDateStr='" + endDateStr + '\'' +
				", endTimeStr='" + endTimeStr + '\'' +
				", isOpenStr='" + isOpenStr + '\'' +
				", paramsStr='" + paramsStr + '\'' +
				", openTime='" + openTime + '\'' +
				", closeTime='" + closeTime + '\'' +
				", selected_gs=" + selected_gs +
				", isReset='" + isReset + '\'' +
				'}';
	}
}
