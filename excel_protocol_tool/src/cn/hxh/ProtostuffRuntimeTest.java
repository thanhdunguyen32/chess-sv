package cn.hxh;

import com.google.common.io.Files;
import lion.common.CollectionWrap;
import lion.common.ProtostuffUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProtostuffRuntimeTest {

	public static void main(String[] args) {
//		mapTest();
		Foo foo = new Foo();
		foo.setId(101);
		foo.setName("hexuhui");
		foo.setAddress("shanghai");
		foo.setJobs(new int[]{101,202,303,404,11111 });
		Map<String,String> toolsMap = new HashMap<>();
		for (int i = 0; i < 2000; i++) {
			toolsMap.put("t"+i,"p"+i);
		}
		foo.setTools(toolsMap);
		Foo foo2 = null;
		byte[] fieldBlob = ProtostuffUtil.serialize(foo2);
		System.out.println("finish!"+fieldBlob);
	}

	public static void createFile() {
		// ser
		try {
			Foo foo = new Foo();
			foo.setId(101);
			foo.setName("hexuhui");
			foo.setAddress("shanghai");
			foo.setJobs(new int[]{101,202,303,404,11111 });
			Map<String,String> toolsMap = new HashMap<>();
			for (int i = 0; i < 2000; i++) {
				toolsMap.put("t"+i,"p"+i);
			}
			foo.setTools(toolsMap);
			byte[] protostuff = ProtostuffUtil.serialize(foo);

			try {
				Files.write(protostuff, new File("data.pro"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			Thread.sleep(1000);
			byte[] byteArray = Files.toByteArray(new File("data.pro"));
			Foo deserializer = ProtostuffUtil.deserialize(byteArray, Foo.class);
			System.out.println(deserializer.toString());
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void mapTest(){
		try {
			Map<Integer,LegionPlayer> members = new HashMap<Integer,LegionPlayer>();
			members.put(1,new LegionPlayer(1,"何徐辉"));
			members.put(2,new LegionPlayer(2,"灰机哥"));
			members.put(3,new LegionPlayer(3,"灰机哥3"));
			members.put(4,new LegionPlayer(4,"灰机哥4"));
			members.put(5,new LegionPlayer(5,"灰机哥5"));
			byte[] protostuff = ProtostuffUtil.serialize(new CollectionWrap<>(members));

			try {
				Files.write(protostuff, new File("data.pro"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			Thread.sleep(1000);
			byte[] byteArray = Files.toByteArray(new File("data.pro"));
			CollectionWrap<Map<Integer,LegionPlayer>> deserializer = ProtostuffUtil.deserialize(byteArray, new CollectionWrap<>());
			System.out.println(deserializer.toString());
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void readProto() throws IOException {
//		Schema<TemplateCollection> schema = RuntimeSchema.getSchema(TemplateCollection.class);
//		TemplateCollection tc = new TemplateCollection();
//		byte[] pomByte = Files.toByteArray(new File("Stage.pro"));
//		ProtostuffIOUtil.mergeFrom(pomByte, tc, schema);
//		List<StageTemplate> tplList = tc.getObjList(StageTemplate.class);
//		System.out.println(tplList);
	}

	public static final class LegionPlayer{
		private int playerId;
		private String name;

		public LegionPlayer(int playerId, String name) {
			this.playerId = playerId;
			this.name = name;
		}

		public int getPlayerId() {
			return playerId;
		}

		public void setPlayerId(int playerId) {
			this.playerId = playerId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "LegionPlayer{" +
					"playerId=" + playerId +
					", name='" + name + '\'' +
					'}';
		}
	}

}
