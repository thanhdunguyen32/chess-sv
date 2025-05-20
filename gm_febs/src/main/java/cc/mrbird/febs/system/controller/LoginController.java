package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.GsTreeWrap;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.exception.RedisConnectException;
import cc.mrbird.febs.common.utils.CaptchaUtil;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.monitor.entity.LoginLog;
import cc.mrbird.febs.monitor.service.ILoginLogService;
import cc.mrbird.febs.monitor.service.IRedisService;
import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.GsService;
import cc.mrbird.febs.system.service.IUserService;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import com.wf.captcha.base.Captcha;
import lion.netty4.message.RequestByteMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author MrBird
 */
@Validated
@RestController
@Slf4j
public class LoginController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ILoginLogService loginLogService;
    @Autowired
    private GsService gsService;
    @Autowired
    private GmLanManager gmLanManager;
    @Autowired
    private IRedisService redisService;

    @GetMapping("test")
    public FebsResponse test1() {
        return new FebsResponse().success().data("test1=========================");
    }
    @PostMapping("login")
    @Limit(key = "login", period = 60, count = 20, name = "Giao diện đăng nhập", prefix = "limit")
    public FebsResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        log.info("login,username={},password={},verifyCode={},rememberMe={}", username, password, verifyCode, rememberMe);
        log.info("request={}", request);
        // if (!CaptchaUtil.verify(verifyCode, request)) {
        //     throw new FebsException("验证码错误！");
        // }
        password = MD5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // Lưu nhật ký đăng nhập
        log.info("login success,username={}", username, password, token);
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }

    @PostMapping("regist")
    public FebsResponse regist(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws FebsException {
        User user = userService.findByName(username);
        if (user != null) {
            throw new FebsException("Tên người dùng này đã tồn tại");
        }
        this.userService.regist(username, password);
        return new FebsResponse().success();
    }

    @GetMapping("gs_set/{gs_id}")
    public FebsResponse setDefaultGs(@NotNull(message = "{required}") @PathVariable Integer gs_id) {
        log.info("set default gs entity,gs_id={}", gs_id);
        GsEntity gsEntity = gsService.getGsById(gs_id);
        if (gsEntity == null) {
            return new FebsResponse().fail();
        }
        User user = getCurrentUser();
        user.setGsEntity(gsEntity);
        //set default gs
        try {
            redisService.set(user.getUsername() + "_gs", gs_id.toString());
        } catch (RedisConnectException e) {
            log.error("", e);
        }
        //ret
        Map<String, Object> data = new HashMap<>();
        data.put("gsEntity", gsEntity);
        return new FebsResponse().success().data(data);
    }

    @GetMapping("gs/tree")
    public List<GsTreeWrap> gsTree() {
        List<GsTreeWrap> retlist = new ArrayList<>();
        List<GsEntity> gsList = gsService.getGsList();
        User currentUser = getCurrentUser();
        for (GsEntity gsEntity : gsList) {
            GsTreeWrap gsTreeWrap = new GsTreeWrap();
            gsTreeWrap.setId(gsEntity.getId());
            gsTreeWrap.setName(gsEntity.getName());
            if (currentUser.getGsEntity() != null && currentUser.getGsEntity().getId().equals(gsEntity.getId())) {
                gsTreeWrap.setChecked(true);
            }
            retlist.add(gsTreeWrap);
        }
        return retlist;
    }

    @GetMapping("index/{username}")
    public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
        // Cập nhật thời gian đăng nhập
        this.userService.updateLoginTime(username);
        
        Map<String, Object> data = new HashMap<>();
        int online_count = 0;
        int new_player = 0;
        int active_player = 0;
        int today_new_payers = 0;
        int pay_count = 0;
        int pay_val = 0;
        int today_login_count = 0;
        int yesterday_create_count = 0;

        Collection<GsEntity> gsEntities = gsService.getValidGsEntity();
        if (!gsEntities.isEmpty()) {
            for (GsEntity gsEntity : gsEntities) {
                try {
                    boolean connectRet = gmLanManager.connect(gsEntity);
                    if (connectRet) {
                        gmLanManager.getMainStatParams(gsEntity.getHost(), gsEntity.getPort());
                        GsSyncRequest.getInstance().doSend();
                        RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
                        if (retMsg == null) {
                            log.error("get info error!gsEntity={}", gsEntity);
                        } else {
                            online_count += retMsg.readInt();
                            new_player += retMsg.readInt();
                            active_player += retMsg.readInt();
                            today_new_payers += retMsg.readInt();
                            pay_count += retMsg.readInt();
                            pay_val += retMsg.readInt();
                            today_login_count += retMsg.readInt();
                            yesterday_create_count += retMsg.readInt();
                            GsSyncRequest.getInstance().releaseRetMsg();
                        }
                    } else {
                        log.error("connect error!gsEntity={}", gsEntity);
                    }
                } catch (Exception e) {
                    log.error("Error connecting to game server: " + gsEntity, e);
                }
            }
        }

        //Đã lưu từ hôm qua
        float yesterday_retain = 0f;
        if (yesterday_create_count > 0) {
            yesterday_retain = today_login_count * 100f / yesterday_create_count;
        }
        data.put("online_count", online_count);
        data.put("new_player", new_player);
        data.put("active_player", active_player);
        data.put("today_new_payers", today_new_payers);
        data.put("pay_count", pay_count);
        data.put("pay_val", pay_val);
        data.put("yesterday_retain", yesterday_retain);
        //上次刷新时间
        data.put("last_refresh_time", DateFormatUtils.format(new Date(), "MM月dd日 HH时mm分ss秒"));
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = this.loginLogService.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        User param = new User();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = this.loginLogService.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new FebsResponse().success().data(data);
    }

    @GetMapping("images/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.outPng(110, 34, 4, Captcha.TYPE_ONLY_NUMBER, request, response);
    }


}
