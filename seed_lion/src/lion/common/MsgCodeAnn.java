package lion.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MsgCodeAnn {

	public int msgcode();

	/**
	 * 相同session访问时间间隔，ms为单位
	 * @return
	 */
	public int accessLimit() default 0;
	
}
