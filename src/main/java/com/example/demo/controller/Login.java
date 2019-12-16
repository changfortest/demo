package com.example.demo.controller;

import com.certificate.dto.common.Result;
import com.certificate.dto.common.ResultContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

/**
 * @author yanghua
 * @version 1.0.0
 */
@RestController
@RequestMapping(path = "/user")
public class Login {
    //dev_yanghua做的改动

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(path = "/login")
    public Result login(String username, String password, HttpSession session) {
        Object obj = checkLock(session, username);
        if (obj!=null) {
            return (ResultContent)obj;
        }

        if (!"private".equals(password)) {
            //新增登录失败记录
            addFailNum(session, username);
            return new ResultContent(1, "用户名或密码错误", " ");
        }
        //清空登录失败记录
        cleanFailNum(session, username);
        return new ResultContent(0, "登陆成功", " ");
    }

    //dev_yanghau做的改动咯

    /**
     * 校验用户登录失败次数
     *
     * @param session
     * @param username
     * @return
     */
    public Object checkLock(HttpSession session, String username) {
        Object o = session.getServletContext().getAttribute(username);

        if (o!=null) {
            HashMap<String, Object> map = (HashMap<String, Object>) o;
            int num = (int) map.get("num");
            Date date = (Date) map.get("lastDate");
            long timeDifference = ((new Date().getTime() - date.getTime()) / 60 / 1000);
            if (num >= 3 && timeDifference < 30) {
                System.out.println("账号已被锁定,请于"+(30-timeDifference)+"分钟后再试!");
                return new ResultContent(1,"账号已被锁定,请于"+(30-timeDifference)+"分钟后再试!","");
            }
        }
        return null;
    }

    /**
     * 新增用户登录失败次数
     *
     * @param session
     * @param username
     */
    public void addFailNum(HttpSession session, String username) {
        Object o = session.getServletContext().getAttribute(username);
        HashMap<String, Object> map = null;
        int num = 0;
        if (o == null) {
            map = new HashMap<String, Object>();
        } else {
            map = (HashMap<String, Object>) o;
            num = (int) map.get("num");
            Date date = (Date) map.get("lastDate");
            long timeDifference = ((new Date().getTime() - date.getTime()) / 60 / 1000);
            if (timeDifference >= 30) {
                num = 0;
            }
            if (num>=2){
                System.out.println("输入错误超过5次账号将被锁定,您还有"+(5-num)+"次机会!");
            }
        }
        map.put("num", num + 1);
        map.put("lastDate", new Date());
        session.getServletContext().setAttribute(username, map);
    }

    /**
     * 清理用户登录失败的记录
     *
     * @param session
     * @param username
     */
    public void cleanFailNum(HttpSession session, String username) {
        session.getServletContext().removeAttribute(username);
    }
}
