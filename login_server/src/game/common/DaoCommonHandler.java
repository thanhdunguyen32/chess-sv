package game.common;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoCommonHandler {

	public static ResultSetHandler<Boolean> Booleanhandler = new ResultSetHandler<Boolean>() {
		@Override
		public Boolean handle(ResultSet rs) throws SQLException {
			int retFlag = 0;
			if (rs.next()) {
				retFlag = rs.getInt(1);
			}
			return retFlag > 0 ? true : false;
		}
	};

	public static ResultSetHandler<Integer> Integerhandler = new ResultSetHandler<Integer>() {
		@Override
		public Integer handle(ResultSet rs) throws SQLException {
			int ret = 0;
			if (rs.next()) {
				ret = rs.getInt(1);
			}
			return ret;
		}
	};

	public static ResultSetHandler<List<Integer>> IntegerListHandler = new ResultSetHandler<List<Integer>>() {
		@Override
		public List<Integer> handle(ResultSet rs) throws SQLException {
			List<Integer> ret = new ArrayList<Integer>();
			while (rs.next()) {
				ret.add(rs.getInt(1));
			}
			return ret;
		}
	};

	public static ResultSetHandler<String> Stringhandler = new ResultSetHandler<String>() {
		@Override
		public String handle(ResultSet rs) throws SQLException {
			String ret = "";
			if (rs.next()) {
				ret = rs.getString(1);
			}
			return ret;
		}
	};

}
