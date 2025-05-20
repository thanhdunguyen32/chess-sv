package lion.http;

/**
 * Created by shaoh on 2017/5/2.
 */
public abstract class HttpMapUriHandler implements ChannelReadHttpHandler {

	private String[] name;

	public HttpMapUriHandler(String... name) {
		this.name = name;
	}

	public String[] getName() {
		return name;
	}

}