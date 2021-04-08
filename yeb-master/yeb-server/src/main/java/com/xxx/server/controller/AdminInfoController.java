package com.xxx.server.controller;

import com.xxx.server.pojo.Admin;
import com.xxx.server.service.AdminService;
import com.xxx.server.utils.RespBean;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 个人中心
 *
 * @author bing  @create 2021/1/20-下午5:14
 */
@RestController
@Slf4j
public class AdminInfoController {
    /**
     * 时间格式化
     */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");

    /**
     * 图片保存路径，自动从yml文件中获取数据
     * 示例： E:/images/
     */
    @Value("${file-save-path}")
    private String fileSavePath;


    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        if (adminService.updateById(admin)) {
            // 更新成功重新设置 authentication 对象
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, null,
                    authentication.getAuthorities()));
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        System.out.println(info);
        String pass = (String) info.get("pass");
        System.out.println(info.get("pass"));
        System.out.println(pass);
        String oldPass = (String) info.get("oldPass");
        System.out.println(oldPass);
        Integer adminId = (Integer) info.get("adminId");
        System.out.println(adminId);
        return adminService.updateAdminPassword(oldPass, pass, adminId);
    }

    @ApiOperation(value = "修改用户头像")
    @PostMapping("/admin/avatar")
    public RespBean updateUserAvatar(MultipartFile file, HttpServletRequest request, Integer id, Authentication authentication) throws IOException {
        /**
         *  2.文件保存目录  E:/images/2020/03/15/
         *  如果目录不存在，则创建
         */
        File dir = new File(fileSavePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        log.info("图片上传，保存位置：" + fileSavePath);
        //3.给文件重新设置一个名字
        //后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        //4.创建这个新文件
        File newFile = new File(fileSavePath  + newFileName);
        //5.复制操作
        file.transferTo(newFile);
        //协议 :// ip地址 ：端口号 / 文件目录(/images/2020/03/15/xxx.jpg)
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/img/"  + newFileName;
        log.info("图片上传，访问URL：" + url);
            return adminService.updateUserAvatar(url, id, authentication);
    }
}