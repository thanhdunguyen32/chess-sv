package cc.mrbird.febs.common.converter;

import cc.mrbird.febs.common.utils.DateUtil;
import com.wuwenze.poi.convert.WriteConverter;
import com.wuwenze.poi.exception.ExcelKitWriteConverterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Execl导出时间类型字段格式化
 *
 * @author MrBird
 */
@Slf4j
public class DateConverter implements WriteConverter {
    @Override
    public String convert(Object value) {
        if (value == null)
            return "";
        else {
            try {
                String valueStr = value.toString();
                if(valueStr.endsWith(".0")){
                    valueStr = valueStr.substring(0,valueStr.length()-2);
                }
                Date realDate = DateUtils.parseDate(valueStr,DateUtil.FULL_TIME_SPLIT_PATTERN);
                return DateFormatUtils.format(realDate,"yyyy-MM-dd");
            } catch (ParseException e) {
                String message = "Ngoại lệ chuyển đổi thời gian";
                log.error(message, e);
                throw new ExcelKitWriteConverterException(message);
            }
        }
    }
}
