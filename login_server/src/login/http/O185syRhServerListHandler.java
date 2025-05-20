package login.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpHtmlHandler;
import login.bean.ServerList4Db;
import login.http.bean.O185syServerRet;
import login.http.bean.O185syServerRet.O185syServerItem;
import login.logic.ServerListManager;

import java.util.ArrayList;
import java.util.List;

public class O185syRhServerListHandler extends HttpHtmlHandler {

	public O185syRhServerListHandler(String name) {
		super(name);
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, String content) throws Exception {
		List<ServerList4Db> retList = ServerListManager.getInstance().getServerList();
		O185syServerRet ret = new O185syServerRet();
		ret.setState(1);
		List<O185syServerItem> itemList = new ArrayList<>();
		for (ServerList4Db serverList4Db : retList) {
			O185syServerItem serverItem = new O185syServerItem(serverList4Db.getId(), serverList4Db.getId(),
					serverList4Db.getId() + "#" + serverList4Db.getName());
			itemList.add(serverItem);
		}
		ret.setData(itemList);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(ret);
		return json;
	}
	
	public static void main(String[] args) throws JsonProcessingException {
		O185syServerRet ret = new O185syServerRet();
		ret.setState(1);
		List<O185syServerItem> itemList = new ArrayList<>();
		O185syServerItem serverItem = new O185syServerItem(1,1,"s1");
		itemList.add(serverItem);
		O185syServerItem serverItem2 = new O185syServerItem(2,2,"s2");
		itemList.add(serverItem2);
		ret.setData(itemList);
		ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(ret);
        System.out.println(json);
	}
	
}
