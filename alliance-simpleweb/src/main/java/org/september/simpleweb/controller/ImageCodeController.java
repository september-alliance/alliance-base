package org.september.simpleweb.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.september.core.util.ImageCodeUtil;
import org.september.core.util.StringExtUtil;
import org.september.simpleweb.auth.PublicMethod;
import org.september.simpleweb.model.ResponseVo;
import org.september.simpleweb.utils.SessionHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;

@PublicMethod
@Controller
@RequestMapping("/imagecode")
public class ImageCodeController {

	public static final String KEY_PREFIX = "image_vertify_code";

    public static final int EXPIRE_TIME = 300;
    
    /**
     * 获取一个图片验证码，并且将图片验证码放入到缓存中
     * @param response
     * @param id
     * @throws IOException
     */
    @RequestMapping("/get")
    public void get(HttpServletResponse response) throws IOException {
    	Object[] images = ImageCodeUtil.createImage();
        String code = (String) images[0];
        code = code.toLowerCase();
        SessionHelper.getSession().setAttribute(KEY_PREFIX, code);
        BufferedImage image = (BufferedImage) images[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

    @ResponseBody
    @RequestMapping(value = "/getCode")
    public ResponseVo<String> getCode() throws Exception {
    	String code = (String)SessionHelper.getSession().getAttribute(KEY_PREFIX);
        return ResponseVo.<String> BUILDER().setData(code).setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
    }
    
    public static boolean verify(String toVerifyCode){
    	String code = (String)SessionHelper.getSession().getAttribute(KEY_PREFIX);
//    	SessionHelper.getSession().removeAttribute(KEY_PREFIX);
    	return StringExtUtil.safeEqualsIgnoreCase(code, toVerifyCode);
    }
    
    public static void expireCodeFromSession() {
    	SessionHelper.getSession().removeAttribute(KEY_PREFIX);
    }
}
