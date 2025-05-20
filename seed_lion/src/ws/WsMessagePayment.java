package ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lion.netty4.message.MyRequestMessage;



public final class WsMessagePayment {

    public static final class BuyPaymentItem {
        public String userId;
        public String orderId;
        public int money;
        public int time;
        public String pid;
        public int serverId;
    }

    public static final class C2SBuyPaymentItem {
        private static final Logger logger = LoggerFactory.getLogger(C2SBuyPaymentItem.class);
        public static final int id = 11001;
        public String userId;
        public String orderId;
        public int money;
        public int time;
        public String pid;
        public int serverId;

        @Override
        public String toString() {
            return "C2SBuyPaymentItem [userId=" + userId + ", orderId=" + orderId + ", money=" + money + ", time="
                    + time
                    + ", pid=" + pid + ", serverId=" + serverId + "]";
        }

        public static C2SBuyPaymentItem parse(MyRequestMessage request) {
            C2SBuyPaymentItem retObj = new C2SBuyPaymentItem();
            try {
                retObj.userId = request.readString();
                retObj.orderId = request.readString();
                retObj.money = request.readInt();
                retObj.time = request.readInt();
                retObj.pid = request.readString();
                retObj.serverId = request.readInt();
            } catch (Exception e) {
                logger.error("bulid protocol error!", e);
            }
            return retObj;
        }
    }
}
