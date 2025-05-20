package cc.mrbird.febs.common.converter;

import cc.mrbird.febs.common.utils.DateUtil;
import com.wuwenze.poi.convert.WriteConverter;
import com.wuwenze.poi.exception.ExcelKitWriteConverterException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

/**
 * Execl导出时间类型字段格式化
 *
 * @author MrBird
 */
@Slf4j
public class PercentConverter implements WriteConverter {
    @Override
    public String convert(Object value) {
        if (value == null)
            return "";
        else {
            float retainValue = Float.parseFloat(value.toString())/100f;
            java.text.NumberFormat percentFormat = java.text.NumberFormat.getPercentInstance();
            percentFormat.setMaximumFractionDigits(2); //最大小数位数
            percentFormat.setMinimumFractionDigits(0); //最小小数位数
            String ret = percentFormat.format(retainValue);//自动转换成百分比显示..
            return ret;
        }
    }
}
