package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.authentication.ShiroHelper;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.RedisConnectException;
import cc.mrbird.febs.common.properties.GmProperties;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.monitor.service.IRedisService;
import cc.mrbird.febs.system.entity.Activity4Gm;
import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.ActivityService;
import cc.mrbird.febs.system.service.GsService;
import cc.mrbird.febs.system.service.IUserService;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import cc.mrbird.febs.template.ActivityTemplateCache;
import game.module.template.ActivityTemplate;
import lion.netty4.message.RequestByteMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
@Controller("systemView")
@Slf4j
public class ViewController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ShiroHelper shiroHelper;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private GsService gsService;
    @Autowired
    private GmLanManager gmLanManager;
    @Autowired
    private GmProperties gmProperties;
    @Autowired
    private ActivityService activityService;

    @GetMapping("login")
    @ResponseBody
    public Object login(HttpServletRequest request) {
        if (FebsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(FebsUtil.view("login"));
            return mav;
        }
    }

    @GetMapping("unauthorized")
    public String unauthorized() {
        return FebsUtil.view("error/403");
    }


    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("index")
    public String index(Model model) {
        AuthorizationInfo authorizationInfo = shiroHelper.getCurrentuserAuthorizationInfo();
        User user = super.getCurrentUser();
        User currentUserDetail = userService.findByName(user.getUsername());
        //set default gs
        try {
            String default_gs_str = redisService.get(user.getUsername() + "_gs");
            if (StringUtils.isNotBlank(default_gs_str)) {
                GsEntity gsEntity = gsService.getGsById(Integer.parseInt(default_gs_str));
                if (gsEntity != null) {
                    currentUserDetail.setGsEntity(gsEntity);
                    user.setGsEntity(gsEntity);
                }
            }
        } catch (RedisConnectException e) {
            log.error("", e);
        }
        //gs list
        List<GsEntity> gsList = gsService.getGsList();
        currentUserDetail.setPassword("It's a secret");
        model.addAttribute("user", currentUserDetail);
        model.addAttribute("gslist", gsList);
        model.addAttribute("permissions", authorizationInfo.getStringPermissions());
        model.addAttribute("roles", authorizationInfo.getRoles());
        return "index";
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "layout")
    public String layout() {
        return FebsUtil.view("layout");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "password/update")
    public String passwordUpdate() {
        return FebsUtil.view("system/user/passwordUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/profile")
    public String userProfile() {
        return FebsUtil.view("system/user/userProfile");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/avatar")
    public String userAvatar() {
        return FebsUtil.view("system/user/avatar");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/profile/update")
    public String profileUpdate() {
        return FebsUtil.view("system/user/profileUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user")
    @RequiresPermissions("user:view")
    public String systemUser() {
        return FebsUtil.view("system/user/user");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/add")
    @RequiresPermissions("user:add")
    public String systemUserAdd() {
        return FebsUtil.view("system/user/userAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/detail/{username}")
    @RequiresPermissions("user:view")
    public String systemUserDetail(@PathVariable String username, Model model) {
        resolveUserModel(username, model, true);
        return FebsUtil.view("system/user/userDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/update/{username}")
    @RequiresPermissions("user:update")
    public String systemUserUpdate(@PathVariable String username, Model model) {
        resolveUserModel(username, model, false);
        return FebsUtil.view("system/user/userUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/role")
    @RequiresPermissions("role:view")
    public String systemRole() {
        return FebsUtil.view("system/role/role");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/menu")
    @RequiresPermissions("menu:view")
    public String systemMenu() {
        return FebsUtil.view("system/menu/menu");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/dept")
    @RequiresPermissions("dept:view")
    public String systemDept() {
        return FebsUtil.view("system/dept/dept");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/gonggao")
    @RequiresPermissions("gonggao:view")
    public String gonggaoView(Model model) {
        // 发送至login server
        String lsHostName = gmProperties.getLogin().getHost();
        int lsLanPort = gmProperties.getLogin().getPort();
        model.addAttribute("lsHostName",lsHostName);
        model.addAttribute("lsLanPort",lsLanPort);
        if (StringUtils.isNoneEmpty(lsHostName) && lsLanPort > 0) {
            boolean connectRet = gmLanManager.connectLs(lsHostName, lsLanPort);
            if (connectRet) {
                gmLanManager.getLsAnnouncement(lsHostName, lsLanPort);
                GsSyncRequest.getInstance().doSend();
                // get result
                RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
                if (retMsg == null) {
                    model.addAttribute("errorMsg", "Kết nối bất thường với máy chủ đăng nhập!");
                } else {
                    String content = retMsg.readString();
                    model.addAttribute("gonggaoContent", content);
                    GsSyncRequest.getInstance().releaseRetMsg();
                }
            }else{
                model.addAttribute("errorMsg", "Kết nối bất thường với máy chủ đăng nhập!");
            }
        } else {
            model.addAttribute("gonggaoContent", "");
        }
        return FebsUtil.view("op/gonggao");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/subtitle")
    @RequiresPermissions("subtitle:view")
    public String subtitleView(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Không có máy chủ trò chơi mặc định nào được chọn!");
        }
        return FebsUtil.view("op/subtitle");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/cdk")
    @RequiresPermissions("cdk:view")
    public String cdk(Model model) {
        return FebsUtil.view("op/cdk");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/fakepay")
    @RequiresPermissions("fakepay:view")
    public String fakepayView(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Không có máy chủ trò chơi mặc định nào được chọn!");
        }
        return FebsUtil.view("op/fakepay");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/mail")
    @RequiresPermissions("mail:view")
    public String mailView(Model model) {
        User user = getCurrentUser();
        List<GsEntity> gsAll = gsService.getGsList();
        model.addAttribute("gs_list", gsAll);
        List<Integer> toSendGsList = getCurrentUser().getToSendGsIds();
        // Danh sách máy chủ trò chơi
        if (gsAll != null) {
            for (GsEntity gsEntity : gsAll) {
                if (toSendGsList != null && toSendGsList.contains(gsEntity.getId())) {
                    gsEntity.setIs_selected(true);
                } else {
                    gsEntity.setIs_selected(false);
                }
            }
        }
        return FebsUtil.view("op/mail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/recharge")
    @RequiresPermissions("recharge:view")
    public String rechargeView(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Không có máy chủ trò chơi mặc định nào được chọn!");
        }
        return FebsUtil.view("op/recharge");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "op/fenghao")
    @RequiresPermissions("fenghao:view")
    public String fenghaoView(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Không có máy chủ trò chơi mặc định nào được chọn!");
        }
        return FebsUtil.view("op/fenghao");
    }
    @GetMapping(FebsConstant.VIEW_PREFIX + "op/test")
    @RequiresPermissions("test:view")
    public String testView(Model model) {
        // Code kiểm tra, set errorMsg nếu cần
        return FebsUtil.view("op/test");  // Tức là file templates/op/test.html
    }
    
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "op/jinyan")
    @RequiresPermissions("jinyan:view")
    public String jinyanView(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Không có máy chủ trò chơi mặc định nào được chọn!");
        }
        return FebsUtil.view("op/jinyan");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/kick")
    @RequiresPermissions("kick:view")
    public String kickView(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Không có máy chủ trò chơi mặc định nào được chọn!");
        }
        return FebsUtil.view("op/kick");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/activity")
    @RequiresPermissions("activity:view")
    public String activityList(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Không có máy chủ trò chơi mặc định nào được chọn!");
        }
        return FebsUtil.view("op/activity");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/buypackage")
    @RequiresPermissions("payment:view")
    public String buyPackagePage(Model model) {
        User user = getCurrentUser();
        if(user.getGsEntity() == null) {
            model.addAttribute("errorMsg", "Vui lòng chọn máy chủ trò chơi mặc định!");
        }
        return FebsUtil.view("op/buypackage");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "op/activity/update/{actid}")
    @RequiresPermissions("activity:view")
    public String activityUpdate(@PathVariable int actid, Model model) {
        User user = getCurrentUser();
        GsEntity gsEntity = user.getGsEntity();
        // 发送至seed_server
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.getActivityById(gsEntity.getHost(), gsEntity.getPort(), actid);
            GsSyncRequest.getInstance().doSend();
        } else {
            model.addAttribute("message", "Ngoại lệ kết nối với máy chủ trò chơi!");
        }
        // params
        ActivityTemplate activityTpl = ActivityTemplateCache.getInstance().getActivityTemplate(actid);
        model.addAttribute("activityTpl", activityTpl);
        // get result
        Activity4Gm activity4Gm = new Activity4Gm();
        RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
        if (retMsg == null) {
            model.addAttribute("message", "connect to GameServer error!");
        } else {
            int count = retMsg.readInt();
            if (count == 1) {
                activity4Gm.setTemplateId(retMsg.readInt());
                Date startTime = new Date(retMsg.readLong());
                activity4Gm.setStartDateStr(DateFormatUtils.format(startTime,"yyyy-MM-dd"));
                activity4Gm.setStartTimeStr(DateFormatUtils.format(startTime,"HH:mm:ss"));
                Date endTime = new Date(retMsg.readLong());
                activity4Gm.setEndDateStr(DateFormatUtils.format(endTime,"yyyy-MM-dd"));
                activity4Gm.setEndTimeStr(DateFormatUtils.format(endTime,"HH:mm:ss"));
                activity4Gm.setIsOpen(retMsg.readInt());
                activity4Gm.setTitle(retMsg.readString());
                activity4Gm.setDescription(retMsg.readString());
                byte[] params = retMsg.readByteArray();
                switch (activity4Gm.getTemplateId()) {
                    case 1:
                        String paramStr = activityService.extractRechargeParams(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 2:
                        paramStr = activityService.extractCunsumeParams(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 3:
                        Object[] retObjs = activityService.extractLoginParams(params);
                        if (retObjs != null) {
                            activity4Gm.setParamsStr((String) (retObjs[0]));
                            activity4Gm.setValidDayCount((Integer) (retObjs[1]));
                        }
                        break;
                    case 14:
                        paramStr = activityService.extractCommonWithLevel(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 5:
                        retObjs = activityService.extractLevelGoParams(params);
                        if (retObjs != null) {
                            activity4Gm.setParamsStr((String) (retObjs[0]));
                            activity4Gm.setValidDayCount((Integer) (retObjs[1]));
                        }
                        break;

                    case 106:
                        paramStr = activityService.extractAwardDoubleParams(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 7:
                        retObjs = activityService.extractMineParams(params);
                        if (retObjs != null) {
                            activity4Gm.setParamsStr((String) (retObjs[0]));
                            activity4Gm.setValidDayCount((Integer) (retObjs[1]));
                        }
                        break;
                    case 8:
                        paramStr = activityService.extractSale(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 9:
                        paramStr = activityService.extractKaifu(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 11:
                        paramStr = activityService.extractCommonWithLevel(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 111:
                        paramStr = activityService.extractChengZhang(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 112:
                        paramStr = activityService.extractQuanMinFuLi(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 13:
                        paramStr = activityService.extractVipLiBao(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 15:
                        paramStr = activityService.extract3DayFirstRecharge(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 16:
                        paramStr = activityService.extractCommonWithLevel(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 116:
                        paramStr = activityService.extractHeroLibao(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 117:
                        paramStr = activityService.extractLotteryWheel(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 18:
                        paramStr = activityService.extractFirstRechargeDouble(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 119:
                        paramStr = activityService.extractQiZhenYiBao(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 20:
                        paramStr = activityService.extractMeiRiShouChong(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 121:
                        paramStr = activityService.extractDanBiChongZhi(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 22:
                        paramStr = activityService.extractCommonWithLevel(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 123:
                        paramStr = activityService.extractShiLianJiangLi(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 24:
                        paramStr = activityService.extractCommonWithLevel(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 124:
                        paramStr = activityService.extractChongZhiBang(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 25:
                        paramStr = activityService.extractXiaoFeiBang(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 26:
                        paramStr = activityService.extractCrossBoss(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 27:
                        paramStr = activityService.extractTianTianHaoLi(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    case 128:
                        paramStr = activityService.extractQingChunJiJin(params);
                        activity4Gm.setParamsStr(paramStr);
                        break;
                    default:
                        break;
                }
            }
            GsSyncRequest.getInstance().releaseRetMsg();
        }
        model.addAttribute("activity4Gm", activity4Gm);
        //
        List<GsEntity> gsAll = gsService.getGsList();
        model.addAttribute("gs_list", gsAll);
        List<Integer> toSendGsList = getCurrentUser().getToSendGsIds();
        // Danh sách máy chủ trò chơi
        if (gsAll != null) {
            for (GsEntity gsEntity1 : gsAll) {
                if (toSendGsList != null&& toSendGsList.contains(gsEntity1.getId())) {
                    gsEntity1.setIs_selected(true);
                } else {
                    gsEntity1.setIs_selected(false);
                }
            }
        }
        return FebsUtil.view("op/activity_update");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "query/daily_stat")
    @RequiresPermissions("query:view")
    public String dailyStat() {
        return FebsUtil.view("query/daily_stat");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "query/payRankAll")
    @RequiresPermissions("query:view")
    public String topPayPlayers() {
        return FebsUtil.view("query/pay_rank_all");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "query/dailyTopPay")
    @RequiresPermissions("query:view")
    public String dailyTopPay() {
        return FebsUtil.view("query/daily_top_pay");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "query/logItemGo")
    @RequiresPermissions("query:view")
    public String logItemGo() {
        return FebsUtil.view("query/log_item_go");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "query/payLogAll")
    @RequiresPermissions("query:view")
    public String payLogAll() {
        return FebsUtil.view("query/pay_log_all");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "query/chatContent")
    @RequiresPermissions("query:view")
    public String chatContent() {
        return FebsUtil.view("query/chatContent");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "query/playerAllBase")
    @RequiresPermissions("query:view")
    public String playerAllBase() {
        return FebsUtil.view("query/player_all");
    }

    @RequestMapping(FebsConstant.VIEW_PREFIX + "index")
    public String pageIndex() {
        return FebsUtil.view("index");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "404")
    public String error404() {
        return FebsUtil.view("error/404");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "403")
    public String error403() {
        return FebsUtil.view("error/403");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "500")
    public String error500() {
        return FebsUtil.view("error/500");
    }

    

    private void resolveUserModel(String username, Model model, Boolean transform) {
        User user = userService.findByName(username);
        model.addAttribute("user", user);
        if (transform) {
            String ssex = user.getSex();
            if (User.SEX_MALE.equals(ssex)) user.setSex("Nam");
            else if (User.SEX_FEMALE.equals(ssex)) user.setSex("Nữ");
            else user.setSex("Bảo mật");
        }
        if (user.getLastLoginTime() != null)
            model.addAttribute("lastLoginTime", DateUtil.getDateFormat(user.getLastLoginTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "op/server_list")
    public String serverListView(Model model) {
        return FebsUtil.view("op/server_list");
    }


    
    
}
