package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.CommonUtils;
import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.GmProperties;
import cc.mrbird.febs.generator.entity.*;
import cc.mrbird.febs.system.cdk.CdkService;
import cc.mrbird.febs.system.entity.*;
import cc.mrbird.febs.system.service.ActivityService;
import cc.mrbird.febs.system.service.GsService;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import cc.mrbird.febs.template.ActivityTemplateCache;

import cc.mrbird.febs.common.utils.FebsUtil;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import game.module.template.ActivityTemplate;
import lion.common.StringConstants;
import lion.netty4.message.RequestByteMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("op")
public class OperationController extends BaseController {

    @Autowired
    private IDeptService deptService;
    @Autowired
    private GmLanManager gmLanManager;
    @Autowired
    private GmProperties gmProperties;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private GsService gsService;
    @Autowired
    private CdkService cdkService;

    @GetMapping("select/tree")
    @ControllerEndpoint(exceptionMessage = "Không thể lấy cây phòng ban")
    public List<DeptTree<Dept>> getDeptTree() throws FebsException {
        return this.deptService.findDepts();
    }

    @GetMapping("tree")
    @ControllerEndpoint(exceptionMessage = "Không thể lấy cây phòng ban")
    public FebsResponse getDeptTree(Dept dept) throws FebsException {
        List<DeptTree<Dept>> depts = this.deptService.findDepts(dept);
        return new FebsResponse().success().data(depts);
    }

    @PostMapping
    @RequiresPermissions("dept:add")
    @ControllerEndpoint(operation = "Thêm phòng ban mới", exceptionMessage = "Không thể thêm phòng ban")
    public FebsResponse addDept(@Valid Dept dept) {
        this.deptService.createDept(dept);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{deptIds}")
    @RequiresPermissions("dept:delete")
    @ControllerEndpoint(operation = "Xóa phòng ban", exceptionMessage = "Không thể xóa phòng ban")
    public FebsResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws FebsException {
        String[] ids = deptIds.split(StringPool.COMMA);
        this.deptService.deleteDepts(ids);
        return new FebsResponse().success();
    }

    @PostMapping("gonggaoUpdate")
    @RequiresPermissions("gonggao:update")
    @ControllerEndpoint(operation = "Cập nhật thông báo", exceptionMessage = "Cập nhật thông báo thất bại")
    public FebsResponse updateGonggao(GonggaoConfig gonggaoConfig) throws FebsException {
        // Gửi đến máy chủ đăng nhập
        String lsHostName = gonggaoConfig.getLsHostName();
        int lsLanPort = gonggaoConfig.getLsLanPort();
        if(!StringUtils.isBlank(gonggaoConfig.getGonggaoContent())) {
            boolean connectRet = gmLanManager.connectLs(lsHostName, lsLanPort);
            if (connectRet) {
                gmLanManager.announcement(lsHostName, lsLanPort, gonggaoConfig.getGonggaoContent());
            }
        }
        //
        gmProperties.getLogin().setHost(lsHostName);
        gmProperties.getLogin().setPort(lsLanPort);
        return new FebsResponse().success();
    }

    @PostMapping("subtitleSend")
    @RequiresPermissions("subtitle:send")
    @ControllerEndpoint(operation = "Gửi thông báo chạy", exceptionMessage = "Không gửi được thông báo chạy")
    public FebsResponse subtitleSend(SubtitleConfig subtitleConfig) throws FebsException {
        try {
            // Validate input
            if (StringUtils.isBlank(subtitleConfig.getSubtitleContent())) {
                return new FebsResponse().fail().message("Nội dung không được để trống");
            }
            if (subtitleConfig.getRepeatCount() < 1) {
                return new FebsResponse().fail().message("Số lần không hợp lệ");
            }

            // Kiểm tra game server
            User user = getCurrentUser();
            if (user.getGsEntity() == null) {
                return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!");
            }

            // Kết nối tới game server
            GsEntity gsEntity = user.getGsEntity();
            boolean connectRet = gmLanManager.connect(gsEntity);
            if (!connectRet) {
                return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi!");
            }

            // Log request
            log.info("Sending subtitle - Content: {}, RepeatCount: {}, Server: {}:{}", 
                    subtitleConfig.getSubtitleContent(),
                    subtitleConfig.getRepeatCount(),
                    gsEntity.getHost(),
                    gsEntity.getPort());

            // Gửi thông báo
            gmLanManager.subtitle(gsEntity.getHost(), 
                                gsEntity.getPort(),
                                subtitleConfig.getSubtitleContent(),
                                subtitleConfig.getRepeatCount());

            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "Gửi thông báo thất bại";
            log.error(message, e);
            return new FebsResponse().fail().message(message);
        }
    }

    @PostMapping("cdkGenerate")
    @RequiresPermissions("cdk:generate")
    @ControllerEndpoint(operation = "Tạo CDK", exceptionMessage = "Không tạo được CDK")
    public FebsResponse cdkGenerate(@RequestParam(value = "cnt") int cnt, @RequestParam(value = "area") int area, @RequestParam(value =
            "awardId") int awardId, @RequestParam(value = "cdkName") String cdkName, @RequestParam(value = "isReuse") int isReuse) throws FebsException {
        // Gửi đến máy chủ trò chơi
        if (cnt > 0 && awardId > 0 && StringUtils.isNotEmpty(cdkName)) {
            Set<String> cdkSet = CommonUtils.getCdkLength15(cnt);
            List<Cdk> cdks = new ArrayList<Cdk>();
            for (String cdkStr : cdkSet) {
                Cdk cdk = new Cdk();
                cdk.setPlatform(1);
                cdk.setArea(area);
                cdk.setCdk(cdkStr);
                cdk.setCdkName(cdkName);
                cdk.setAwardId(awardId);
                cdk.setReuse(isReuse);
                cdks.add(cdk);
            }
            cdkService.batchSaveCdk(cdks);
            return new FebsResponse().success();
        } else {
            return new FebsResponse().fail().message("Các tham số được truyền vào không chính xác!");
        }
    }

    @PostMapping("fakepaySend")
    @RequiresPermissions("fakepay:send")
    @ControllerEndpoint(operation = "Gửi nạp tiền mô phỏng", exceptionMessage = "Không thể gửi khoản nạp tiền mô phỏng")
    public FebsResponse fakepaySend(FakepayConfig fakepayConfig) throws FebsException {
        log.info("fake pay!req={}", fakepayConfig);
        //Xác định xem gs có trống không
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định! ");
        }
        // Gửi đến máy chủ trò chơi
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.fakepay(gsEntity.getHost(), gsEntity.getPort(), fakepayConfig.getPlayerId(), fakepayConfig.getPayMoney(), "charge328");
            return new FebsResponse().success();
        } else {
            return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi!" + gsEntity.getHost() + ":" + gsEntity.getPort());
        }
    }

    @PostMapping("fenghaoSend")
    @RequiresPermissions("fenghao:send")
    @ControllerEndpoint(operation = "Cấm tài khoản và tắt tiếng", exceptionMessage = "Không thể cấm tài khoản")
    public FebsResponse fenghaoSend(FenghaoConfig fenghaoConfig) throws FebsException {
        log.info("fenghao op:{}", fenghaoConfig);
        //Xác định xem gs có trống không
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định! ");
        }
        GsEntity gsEntity = user.getGsEntity();
        int opType = fenghaoConfig.getOpType();
        switch (opType) {
            case 1:
                // Đánh giá tính hợp lệ của tham số
                if (StringUtils.isBlank(fenghaoConfig.getEndTime())) {
                    return new FebsResponse().fail().message("Vui lòng chọn ngày kết thúc! ");
                }
                try {
                    Date endTime = DateUtils.parseDate(fenghaoConfig.getEndTime(), "yyyy-MM-dd");
                    boolean connectRet = gmLanManager.connect(gsEntity);
                    if (connectRet) {
                        gmLanManager.fengHao(gsEntity.getHost(), gsEntity.getPort(), fenghaoConfig.getPlayerId(), endTime.getTime());
                        return new FebsResponse().success();
                    } else {
                        return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi! " + gsEntity.getHost() + ":" + gsEntity.getPort());
                    }
                } catch (ParseException e) {
                    log.error("", e);
                    return new FebsResponse().fail().message("Định dạng ngày không bình thường! " + fenghaoConfig.getEndTime());
                }
            case 2:
                boolean connectRet = gmLanManager.connect(gsEntity);
                if (connectRet) {
                    gmLanManager.jieFeng(gsEntity.getHost(), gsEntity.getPort(), fenghaoConfig.getPlayerId());
                    return new FebsResponse().success();
                } else {
                    return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi! " + gsEntity.getHost() + ":" + gsEntity.getPort());
                }
            case 3:
                // Đánh giá tính hợp lệ của tham số
                if (StringUtils.isBlank(fenghaoConfig.getEndTime())) {
                    return new FebsResponse().fail().message("Vui lòng chọn ngày kết thúc! ");
                }
                try {
                    Date endTime = DateUtils.parseDate(fenghaoConfig.getEndTime(), "yyyy-MM-dd");
                    connectRet = gmLanManager.connect(gsEntity);
                    if (connectRet) {
                        gmLanManager.jinYan(gsEntity.getHost(), gsEntity.getPort(), fenghaoConfig.getPlayerId(), endTime.getTime());
                        return new FebsResponse().success();
                    } else {
                        return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi! " + gsEntity.getHost() + ":" + gsEntity.getPort());
                    }
                } catch (ParseException e) {
                    log.error("", e);
                    return new FebsResponse().fail().message("Định dạng ngày không bình thường! " + fenghaoConfig.getEndTime());
                }
            case 4:
                connectRet = gmLanManager.connect(gsEntity);
                if (connectRet) {
                    gmLanManager.jinYanCancel(gsEntity.getHost(), gsEntity.getPort(), fenghaoConfig.getPlayerId());
                    return new FebsResponse().success();
                } else {
                    return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi! " + gsEntity.getHost() + ":" + gsEntity.getPort());
                }
            default:
                break;
        }
        return new FebsResponse().fail().message("Hoạt động bất thường! ");
    }

    @PostMapping("kickSend")
    @RequiresPermissions("fenghao:send")
    @ControllerEndpoint(operation = "Đá nhé", exceptionMessage = "Không thể đá ngoại tuyến")
    public FebsResponse kickSend(int playerId) throws FebsException {
        log.info("kick player req:{}", playerId);
        //Xác định xem gs có trống không
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định! ");
        }
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.kick(gsEntity.getHost(), gsEntity.getPort(), playerId);
            return new FebsResponse().success();
        } else {
            return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi! " + gsEntity.getHost() + ":" + gsEntity.getPort());
        }
    }

    @GetMapping("activity_reset/{actid}")
    @RequiresPermissions("activity:reset")
    public FebsResponse activity_reset(@NotNull(message = "{required}") @PathVariable int actid) {
        log.info("reset activity!id={}", actid);
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!");
        }
        GsEntity gsEntity = user.getGsEntity();
        // 发送至seed_server
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.resetActivityByType(gsEntity.getHost(), gsEntity.getPort(), actid);
            GsSyncRequest.getInstance().doSend();
        } else {
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
        }
        // get result
        RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
        if (retMsg == null) {
            log.error("get info error!gsEntity={}", gsEntity);
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường");
        } else {
            GsSyncRequest.getInstance().releaseRetMsg();
            return new FebsResponse().success();
        }
    }

    @PostMapping("activityUpdate")
    @RequiresPermissions("activity:update")
    @ControllerEndpoint(operation = "Hoạt động chỉnh sửa", exceptionMessage = "Chỉnh sửa hoạt động không thành công")
    public FebsResponse activityUpdate(Activity4Gm activity4Gm, Model model) {
        log.info("activity update!req={}", activity4Gm);
        List<Integer> selectGsIds = activity4Gm.getSelected_gs();
        if(selectGsIds != null) {
            selectGsIds.removeIf(gsId -> gsId == null);
        }
        if (CollectionUtils.isEmpty(selectGsIds)) {
            return new FebsResponse().fail().message("Bạn chưa chọn bất kỳ máy chủ trò chơi nào!");
        }
        getCurrentUser().setToSendGsIds(selectGsIds);
        ActivityService.ActivityRetMsg retMsg = null;
        switch (activity4Gm.getTemplateId()) {
            case 1:
                retMsg = activityService.rechargeAward(activity4Gm, model);
                break;
            case 2:
                retMsg = activityService.consumeAward(activity4Gm, model);
                break;
            case 3:
                retMsg = activityService.loginAward(activity4Gm, model);
                break;
            case 14:
                retMsg = activityService.buildCommonWithLevel(activity4Gm, model);
                break;
            case 5:
                retMsg = activityService.levelGoAward(activity4Gm, model);
                break;
            case 106:
                retMsg = activityService.awardDoubleAward(activity4Gm, model);
                break;
            case 7:
                retMsg = activityService.mineAward(activity4Gm, model);
                break;
            case 8:
                retMsg = activityService.sale(activity4Gm, model);
                break;
            case 9:
                retMsg = activityService.kaifu(activity4Gm, model);
                break;
            case 11:
                retMsg = activityService.buildCommonWithLevel(activity4Gm, model);
                break;
            case 111:
                retMsg = activityService.chengZhangJiJin(activity4Gm, model);
                break;
            case 112:
                retMsg = activityService.quanMinFuLi(activity4Gm, model);
                break;
            case 13:
                retMsg = activityService.vipLiBao(activity4Gm, model);
                break;
            case 15:
                retMsg = activityService.threeDayFirstRecharge(activity4Gm, model);
                break;
            case 16:
                retMsg = activityService.buildCommonWithLevel(activity4Gm, model);
                break;
            case 116:
                retMsg = activityService.buildHeroLibao(activity4Gm, model);
                break;
            case 117:
                retMsg = activityService.buildLotteryWheel(activity4Gm, model);
                break;
            case 18:
                retMsg = activityService.buildFirstRechargeDouble(activity4Gm, model);
                break;
            case 119:
                retMsg = activityService.buildQiZhenYiBao(activity4Gm, model);
                break;
            case 20:
                retMsg = activityService.buildCommon(activity4Gm, model);
                break;
            case 121:
                retMsg = activityService.buildDanBiChongZhi(activity4Gm, model);
                break;
            case 22:
                retMsg = activityService.buildCommonWithLevel(activity4Gm, model);
                break;
            case 123:
                retMsg = activityService.buildShiLianJiangLi(activity4Gm, model);
                break;
            case 24:
                retMsg = activityService.buildCommonWithLevel(activity4Gm, model);
                break;
            case 124:
                retMsg = activityService.buildChongZhiBang(activity4Gm, model);
                break;
            case 25:
                retMsg = activityService.buildXiaoFeiBang(activity4Gm, model);
                break;
            case 26:
                retMsg = activityService.buildCrossBoss(activity4Gm, model);
                break;
            case 27:
                retMsg = activityService.buildTianTianHaoLi(activity4Gm, model);
                break;
            case 128:
                retMsg = activityService.buildQingNianJiJin(activity4Gm, model);
                break;
            default:
                break;
        }
        //Xác định xem có nên đặt lại hoạt động hay không
        String isresetStr = activity4Gm.getIsReset();
        if (isresetStr != null && isresetStr.equals("on")) {
            if (getCurrentUser().getToSendGsIds() != null) {
                List<Integer> gsIdList = getCurrentUser().getToSendGsIds();
                List<GsEntity> gsAll = gsService.getGsList();
                for (GsEntity gsEntity : gsAll) {
                    if (gsIdList.contains(gsEntity.getId())) {
                        boolean connectRet = gmLanManager.connect(gsEntity);
                        if (connectRet) {
                            gmLanManager.resetActivityByType(gsEntity.getHost(), gsEntity.getPort(), activity4Gm.getTemplateId());
                        } else {
                            log.error("Kết nối bất thường với máy chủ trò chơi! {}:{}", gsEntity.getHost(), gsEntity.getPort());
                        }
                    }
                }
            }
        }
        return new FebsResponse().success();
    }

    @GetMapping("activityList")
    @RequiresPermissions("activity:view")
    public FebsResponse activityList() {
        log.info("get activity list!");
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!");
        }
        List<ActivityTemplate> activityTpls = ActivityTemplateCache.getInstance().getActivityTemplates();
        activityTpls.sort((o1, o2) -> {
            if (o1.getType() < o2.getType()) {
                return 1;
            } else if (o1.getType() > o2.getType()) {
                return -1;
            } else {
                if (o1.getId() > o2.getId()) {
                    return 1;
                } else if (o1.getId() < o2.getId()) {
                    return -1;
                }
            }
            return 0;
        });
        GsEntity gsEntity = user.getGsEntity();
        // 发送至seed_server
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.getAllActivityBase(gsEntity.getHost(), gsEntity.getPort());
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
            } else {
                int count = retMsg.readInt();
                Map<Integer, Activity4Gm> configuredMap = new HashMap<>();
                for (int i = 0; i < count; i++) {
                    Activity4Gm aBean = new Activity4Gm();
                    int isOpen = retMsg.readInt();
                    int templateId = retMsg.readInt();
                    long startTime = retMsg.readLong();
                    long endTime = retMsg.readLong();
                    String title = retMsg.readString();
                    aBean.setTitle(title);
                    aBean.setIsOpen(isOpen);
                    aBean.setTemplateId(templateId);
                    aBean.setStartTime(new Date(startTime));
                    aBean.setEndTime(new Date(endTime));
                    configuredMap.put(templateId, aBean);
                }
                GsSyncRequest.getInstance().releaseRetMsg();
                //ret
                List<Activity4View> retlist = new ArrayList<>();
                for (ActivityTemplate activityTemplate : activityTpls) {
                    Activity4Gm activity4Gm = configuredMap.get(activityTemplate.getId());
                    Activity4View activity4View;
                    if (activity4Gm != null) {
                        Date startTime = activity4Gm.getStartTime();
                        Date endTime = activity4Gm.getEndTime();
                        if (activityTemplate.getType() == 3) {
                            startTime = null;
                            endTime = null;
                        }
                        activity4View = new Activity4View(activityTemplate.getId(), activityTemplate.getName(), startTime, endTime,
                                activity4Gm.getIsOpen() > 0 ? true : false, true);
                    } else {
                        activity4View = new Activity4View(activityTemplate.getId(), activityTemplate.getName(),
                                null, null, null, false);
                    }
                    retlist.add(activity4View);
                }
                Map<String, Object> dataTable = getDataTable(retlist);
                return new FebsResponse().success().data(dataTable);
            }
        } else {
            log.error("connect error!gsEntity={}", gsEntity);
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
        }
    }


    @PostMapping("mailSend")
    @RequiresPermissions("mail:send")
    @ControllerEndpoint(operation = "Gửi tệp đính kèm email", exceptionMessage = "Không gửi được tệp đính kèm email")
    public FebsResponse mailSend(MailParams mailParams) throws FebsException {
        log.info("send mail!req={}",mailParams);
        List<Integer> selected_gs = mailParams.getSelected_gs();
        Byte addressee = mailParams.getAddressee();
        String receiveId = mailParams.getReceiveId();
        String sender = mailParams.getSender();
        String mailTitle = mailParams.getMailTitle();
        String mailCont = mailParams.getMailCont();
        String attch = mailParams.getAttch();
        String validityStr = mailParams.getValidity();
        int validity = 30; // Default value
        
        // Tính số ngày còn lại từ ngày hết hạn
        try {
            Date endDate = DateUtils.parseDate(validityStr, "yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            
            // Làm tròn thời gian hiện tại lên phút
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            now = calendar.getTime();
            
            // Làm tròn thời gian kết thúc lên ngày
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            endDate = calendar.getTime();
            
            // Tính số ngày còn lại
            long diffInMillies = endDate.getTime() - now.getTime();
            // Chuyển đổi milliseconds thành ngày, làm tròn xuống vì đã tính đến cuối ngày
            validity = (int)(diffInMillies / (1000L * 60 * 60 * 24)) + 1;
            // Đảm bảo tối thiểu 1 ngày
            validity = Math.max(1, validity);
            log.info("Validity calculation: endDate={}, now={}, diff_days={}", endDate, now, validity);
            if (validity <= 0) {
                return new FebsResponse().fail().message("Thời gian hết hạn phải lớn hơn thời gian hiện tại!");
            }
        } catch (ParseException e) {
            log.error("Error parsing date: {}", validityStr, e);
            return new FebsResponse().fail().message("Định dạng thời gian không hợp lệ!");
        }

        if(selected_gs != null) {
            selected_gs.removeIf(gsId -> gsId == null);
        }
        if (CollectionUtils.isEmpty(selected_gs)) {
            return new FebsResponse().fail().message("Bạn chưa chọn bất kỳ máy chủ trò chơi nào!");
        }
        // Đặt máy chủ đã chọn
        getCurrentUser().setToSendGsIds(selected_gs);
        if (addressee != 1 && addressee != 2) {
            return new FebsResponse().fail().message("Vui lòng chọn hình thức giao hàng!");
        }

        if (addressee == 1 && StringUtils.isEmpty(receiveId)) {
            // Gửi đến người chơi được chỉ định
            return new FebsResponse().fail().message("Vui lòng thiết lập ID người chơi để nhận email!");
        }

        if (StringUtils.isEmpty(sender)) {
            return new FebsResponse().fail().message("Vui lòng đặt người gửi email!");
        }

        if (StringUtils.isEmpty(mailTitle)) {
            return new FebsResponse().fail().message("Vui lòng đặt tiêu đề email!");
        }

        if (StringUtils.isEmpty(mailCont)) {
            return new FebsResponse().fail().message("Vui lòng đặt nội dung email!");
        }
        receiveId = receiveId.trim();
        sender = sender.trim();
        mailTitle = mailTitle.trim();
        mailCont = mailCont.trim();
        attch = attch.trim();
        //Người nhận có hợp lệ không?
        if (addressee == 1) {
            String[] playerIdList = receiveId.split("\n");
            for (String aReceiverId : playerIdList) {
                if (!StringUtils.isNumeric(aReceiverId)) {
                    return new FebsResponse().fail().message("Id người chơi không hợp lệ !player_id=" + aReceiverId);
                }
            }
        }
        //Xác định xem tệp đính kèm có hợp lệ hay không
        String[] awardStrList = StringUtils.split(attch, StringConstants.SEPARATOR_HENG);
        for (String awardStrPair : awardStrList) {
            String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_SHU);
            if (!StringUtils.isNumeric(awardPairList[0])) {
                return new FebsResponse().fail().message("Id chỗ dựa không hợp lệ !itemid=" + awardPairList[0]);
            }
            int itemTplId = Integer.parseInt(awardPairList[0]);
//            boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
//            if (!itemTplExist) {
//                return new FebsResponse().fail().message("道具id不存在!id=" + itemTplExist);
//            }
            if (!StringUtils.isNumeric(awardPairList[1])) {
                return new FebsResponse().fail().message("Số lượng đạo cụ không hợp lệ !num=" + awardPairList[1]);
            }
        }

        MailItem mailItem = MailData.createMailItem(addressee, receiveId, sender, mailTitle, mailCont, attch, validity);
        //mailService.saveMailItem(mailItem);
        // 发送至seed_server
        if (getCurrentUser().getToSendGsIds() != null) {
            List<Integer> gsIdList = getCurrentUser().getToSendGsIds();
            List<GsEntity> gsAll = gsService.getGsList();
            Set<String> sendedGsAll = new HashSet<String>();
            for (GsEntity gsEntity : gsAll) {
                String gsId = gsEntity.getHost() + ":" + gsEntity.getPort();
                if (gsIdList.contains(gsEntity.getId()) && !sendedGsAll.contains(gsId)) {
                    boolean connectRet = gmLanManager.connect(gsEntity);
                    if (connectRet) {
                        gmLanManager.sendMailItem2Gs(gsEntity.getHost(), gsEntity.getPort(), mailItem);
                        sendedGsAll.add(gsId);
                    } else {
                        return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
                    }
                }
            }
        }
        return new FebsResponse().success();
    }

    @PostMapping("op/subtitleSend")
    @RequiresPermissions("subtitle:send")
    @ResponseBody
    public FebsResponse subtitleSend(String subtitleContent, Integer repeatCount) {
        try {
            // Validate input
            if (StringUtils.isBlank(subtitleContent)) {
                return new FebsResponse().fail().message("Nội dung không được để trống");
            }
            if (repeatCount == null || repeatCount < 1) {
                return new FebsResponse().fail().message("Số lần không hợp lệ");
            }

            // Kiểm tra game server
            User user = getCurrentUser();
            if (user.getGsEntity() == null) {
                return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!");
            }

            // Kết nối tới game server
            GsEntity gsEntity = user.getGsEntity();
            boolean connectRet = gmLanManager.connect(gsEntity);
            if (!connectRet) {
                return new FebsResponse().fail().message("Không kết nối được với máy chủ trò chơi!");
            }

            // Gửi thông báo
            gmLanManager.subtitle(gsEntity.getHost(), gsEntity.getPort(), 
                                subtitleContent, repeatCount);

            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "Gửi thông báo thất bại";
            log.error(message, e);
            return new FebsResponse().fail().message(message);
        }
    }
    
    @GetMapping("server/all")
    public List<Map<String, Object>> getAllServers() {
        List<GsEntity> list = gsService.getGsList();
        List<Map<String, Object>> ret = new ArrayList<>();
        for (GsEntity gs : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", gs.getId());
            m.put("name", gs.getName());
            ret.add(m);
        }
        return ret;
    }
    // Controller example
    @GetMapping("server/list")
    @RequiresPermissions("server:view")
    @ResponseBody
    public Map<String, Object> getAllServerInfoFromGame(
            @RequestParam(value = "serverIds", required = false) List<Integer> serverIds,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> result = new HashMap<>();
        Map<Integer, ServerInfoBean> allServerInfo = new HashMap<>();
        
        List<GsEntity> allServers = gsService.getGsList();
        if (allServers.isEmpty()) {
            result.put("code", 404);
            result.put("message", "Không có máy chủ trò chơi nào được cấu hình!");
            return result;
        }
        
        for (GsEntity gs : allServers) {
            try {
                boolean connected = gmLanManager.connect(gs);
                if (!connected) {
                    log.warn("Không kết nối được với server: {}:{}", gs.getHost(), gs.getPort());
                    continue;
                }
                
                gmLanManager.getServerList(gs.getHost(), gs.getPort());
                GsSyncRequest.getInstance().doSend();
                RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
                
                if (retMsg != null && retMsg.getMsgCode() == 10074) {
                    int serverCount = retMsg.readInt();
                    for (int i = 0; i < serverCount; i++) {
                        ServerInfoBean serverInfo = new ServerInfoBean();
                        serverInfo.setId(retMsg.readInt());
                        serverInfo.setName(retMsg.readString());
                        serverInfo.setIp(retMsg.readString());
                        serverInfo.setPort(retMsg.readInt());
                        serverInfo.setPortSsl(retMsg.readInt());
                        serverInfo.setStatus(retMsg.readInt());
                        serverInfo.setLanPort(retMsg.readInt());
                        serverInfo.setHttpUrl(retMsg.readString());
                        long createdTime = retMsg.readLong();
                        if (createdTime > 0) {
                            serverInfo.setTime_open(new Date(createdTime));
                        }
                        
                        gmLanManager.getPlayerFromServer(gs.getHost(), gs.getPort());
                        GsSyncRequest.getInstance().doSend();
                        RequestByteMessage userStatMsg = GsSyncRequest.getInstance().getRetMsg();
                        if (userStatMsg != null && userStatMsg.getMsgCode() == 10071) {
                            serverInfo.setTotalUser(userStatMsg.readInt());
                            serverInfo.setUserOnline(userStatMsg.readInt());
                            GsSyncRequest.getInstance().releaseRetMsg();
                        } else {
                            serverInfo.setTotalUser(-1);
                            serverInfo.setUserOnline(-1);
                        }
                        
                        allServerInfo.put(serverInfo.getId(), serverInfo);
                    }
                    GsSyncRequest.getInstance().releaseRetMsg();
                } else {
                    log.warn("Không nhận được phản hồi hợp lệ từ server {}:{}", gs.getHost(), gs.getPort());
                }
            } catch (Exception e) {
                log.error("Lỗi khi xử lý server {}:{}", gs.getHost(), gs.getPort(), e);
            }
        }
        
        Stream<ServerInfoBean> filteredStream = allServerInfo.values().stream();
        if (serverIds != null && !serverIds.isEmpty()) {
            filteredStream = filteredStream.filter(server -> serverIds.contains(server.getId()));
        }
        if (StringUtils.isNotBlank(name)) {
            String lowerName = name.toLowerCase();
            filteredStream = filteredStream.filter(server -> server.getName() != null && server.getName().toLowerCase().contains(lowerName));
        }
        if (status != null) {
            filteredStream = filteredStream.filter(server -> server.getStatus() != null && server.getStatus().equals(status));
        }
        
        List<ServerInfoBean> filteredServers = filteredStream.collect(Collectors.toList());
        int total = filteredServers.size();
        
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, total);
        List<ServerInfoBean> paginatedServers = (start < end) ? filteredServers.subList(start, end) : new ArrayList<>();
        
        result.put("code", 200);
        result.put("message", "OK");
        result.put("count", total);
        result.put("data", paginatedServers);
        return result;
    }
    
    @GetMapping("cms/server/list")
    @ResponseBody
    public Map<String, Object> getAllServerInfoFromGameCMS(
            @RequestParam(value = "serverIds", required = false) List<Integer> serverIds,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> result = new HashMap<>();
        Map<Integer, ServerInfoBean> allServerInfo = new HashMap<>();
        
        List<GsEntity> allServers = gsService.getGsList();
        if (allServers.isEmpty()) {
            result.put("code", 404);
            result.put("message", "Không có máy chủ trò chơi nào được cấu hình!");
            return result;
        }
        
        for (GsEntity gs : allServers) {
            try {
                boolean connected = gmLanManager.connect(gs);
                if (!connected) {
                    log.warn("Không kết nối được với server: {}:{}", gs.getHost(), gs.getPort());
                    continue;
                }
                
                gmLanManager.getServerList(gs.getHost(), gs.getPort());
                GsSyncRequest.getInstance().doSend();
                RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
                
                if (retMsg != null && retMsg.getMsgCode() == 10074) {
                    int serverCount = retMsg.readInt();
                    for (int i = 0; i < serverCount; i++) {
                        ServerInfoBean serverInfo = new ServerInfoBean();
                        serverInfo.setId(retMsg.readInt());
                        serverInfo.setName(retMsg.readString());
                        serverInfo.setIp(retMsg.readString());
                        serverInfo.setPort(retMsg.readInt());
                        serverInfo.setPortSsl(retMsg.readInt());
                        serverInfo.setStatus(retMsg.readInt());
                        serverInfo.setLanPort(retMsg.readInt());
                        serverInfo.setHttpUrl(retMsg.readString());
                        long createdTime = retMsg.readLong();
                        if (createdTime > 0) {
                            serverInfo.setTime_open(new Date(createdTime));
                        }
                        
                        gmLanManager.getPlayerFromServer(gs.getHost(), gs.getPort());
                        GsSyncRequest.getInstance().doSend();
                        RequestByteMessage userStatMsg = GsSyncRequest.getInstance().getRetMsg();
                        if (userStatMsg != null && userStatMsg.getMsgCode() == 10071) {
                            serverInfo.setTotalUser(userStatMsg.readInt());
                            serverInfo.setUserOnline(userStatMsg.readInt());
                            GsSyncRequest.getInstance().releaseRetMsg();
                        } else {
                            serverInfo.setTotalUser(-1);
                            serverInfo.setUserOnline(-1);
                        }
                        
                        allServerInfo.put(serverInfo.getId(), serverInfo);
                    }
                    GsSyncRequest.getInstance().releaseRetMsg();
                } else {
                    log.warn("Không nhận được phản hồi hợp lệ từ server {}:{}", gs.getHost(), gs.getPort());
                }
            } catch (Exception e) {
                log.error("Lỗi khi xử lý server {}:{}", gs.getHost(), gs.getPort(), e);
            }
        }
        
        Stream<ServerInfoBean> filteredStream = allServerInfo.values().stream();
        if (serverIds != null && !serverIds.isEmpty()) {
            filteredStream = filteredStream.filter(server -> serverIds.contains(server.getId()));
        }
        if (StringUtils.isNotBlank(name)) {
            String lowerName = name.toLowerCase();
            filteredStream = filteredStream.filter(server -> server.getName() != null && server.getName().toLowerCase().contains(lowerName));
        }
        if (status != null) {
            filteredStream = filteredStream.filter(server -> server.getStatus() != null && server.getStatus().equals(status));
        }
        
        List<ServerInfoBean> filteredServers = filteredStream.collect(Collectors.toList());
        int total = filteredServers.size();
        
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, total);
        List<ServerInfoBean> paginatedServers = (start < end) ? filteredServers.subList(start, end) : new ArrayList<>();
        
        result.put("code", 200);
        result.put("message", "OK");
        result.put("count", total);
        result.put("data", paginatedServers);
        return result;
    }
    
    
}
