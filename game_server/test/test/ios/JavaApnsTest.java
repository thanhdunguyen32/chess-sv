package test.ios;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class JavaApnsTest {

	public static void main(String[] args) {
		ApnsService service = APNS.newService().withCert("pushcert.p12", "garena")
				.withSandboxDestination().build();
		
		String payload = APNS.newPayload().alertBody("Can't be simpler than this!").build();
		String token = "fedfbcfb....";
		service.push(token, payload);
	}

}
