//package test.sdk;
//
//import java.io.IOException;
//import java.util.Date;
//
//import sdk.iap.IOSVerify;
//import sun.security.provider.MD5;
//
///**
// * @author qili
// * 
// */
//public class IOSAction extends BaseAction {
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * 客户端向服务器验证
//	 * 
//	 * 
//	 * * checkState A 验证成功有效(返回收据) B 账单有效，但己经验证过 C 服务器数据库中没有此账单(无效账单) D 不处理
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
//	public void iOSVerify() throws IOException {
//
//		HttpServletRequest request = ServletActionContext.getRequest();
//		HttpServletResponse response = ServletActionContext.getResponse();
//		System.out.println(new Date().toLocaleString() + "  来自苹果端的验证...");
//		// 苹果客户端传上来的收据,是最原据的收据
//		String receipt = request.getParameter("receipt");
//		System.out.println(receipt);
//		// 拿到收据的MD5
//		String md5_receipt = MD5.md5Digest(receipt);
//		// 默认是无效账单
//		String result = R.BuyState.STATE_C + "#" + md5_receipt;
//		// 查询数据库，看是否是己经验证过的账号
//		boolean isExists = DbServiceImpl_PNM.isExistsIOSReceipt(md5_receipt);
//		String verifyResult = null;
//		if (!isExists) {
//			String verifyUrl = IOSVerify.getVerifyURL();
//			verifyResult = IOSVerify.buyAppVerify(receipt, verifyUrl);
//			// System.out.println(verifyResult);
//			if (verifyResult == null) {
//				// 苹果服务器没有返回验证结果
//				result = R.BuyState.STATE_D + "#" + md5_receipt;
//			} else {
//				// 跟苹果验证有返回结果------------------
//				JSONObject job = JSONObject.fromObject(verifyResult);
//				String states = job.getString("status");
//				if (states.equals("0"))// 验证成功
//				{
//					String r_receipt = job.getString("receipt");
//					JSONObject returnJson = JSONObject.fromObject(r_receipt);
//					// 产品ID
//					String product_id = returnJson.getString("product_id");
//					// 数量
//					String quantity = returnJson.getString("quantity");
//					// 跟苹果的服务器验证成功
//					result = R.BuyState.STATE_A + "#" + md5_receipt + "_" + product_id + "_" + quantity;
//					// 交易日期
//					String purchase_date = returnJson.getString("purchase_date");
//					// 保存到数据库
//					DbServiceImpl_PNM.saveIOSReceipt(md5_receipt, product_id, purchase_date, r_receipt);
//				} else {
//					// 账单无效
//					result = R.BuyState.STATE_C + "#" + md5_receipt;
//				}
//				// 跟苹果验证有返回结果------------------
//			}
//			// 传上来的收据有购买信息==end=============
//		} else {
//			// 账单有效，但己验证过
//			result = R.BuyState.STATE_B + "#" + md5_receipt;
//		}
//		// 返回结果
//		try {
//			System.out.println("验证结果     " + result);
//			System.out.println();
//			response.getWriter().write(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//}
