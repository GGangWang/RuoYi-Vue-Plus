package com.cprams.system.service;

import cn.dev33.satoken.secure.BCrypt;
import com.cprams.common.constant.CacheConstants;
import com.cprams.common.constant.Constants;
import com.cprams.common.constant.UserConstants;
import com.cprams.common.core.domain.entity.SysUser;
import com.cprams.common.core.domain.model.RegisterBody;
import com.cprams.common.core.service.LogininforService;
import com.cprams.common.enums.UserType;
import com.cprams.common.exception.user.CaptchaException;
import com.cprams.common.exception.user.CaptchaExpireException;
import com.cprams.common.exception.user.UserException;
import com.cprams.common.utils.MessageUtils;
import com.cprams.common.utils.ServletUtils;
import com.cprams.common.utils.StringUtils;
import com.cprams.common.utils.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 注册校验方法
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysRegisterService {

    private final ISysUserService userService;
    private final ISysConfigService configService;
    private final LogininforService asyncService;

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        HttpServletRequest request = ServletUtils.getRequest();
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        // 校验用户类型是否存在
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid(), request);
        }

        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            throw new UserException("user.register.save.error", username);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(BCrypt.hashpw(password));
        sysUser.setUserType(userType);
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        asyncService.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"), request);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid, HttpServletRequest request) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            asyncService.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.expire"), request);
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            asyncService.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.error"), request);
            throw new CaptchaException();
        }
    }
}
