/**
 * @title MailAuthenticator.java
 * @package test
 * @description 
 * @author YuezhouLi
 * @update 2012-7-2 下午4:01:04
 * @version V1.0
 */
package com.hisoft.hscloud.mail.util;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-7-2 下午4:01:04
 */
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * 服务器邮箱登录验证
 * 
 * @author MZULE
 * 
 */
public class MailAuthenticator extends Authenticator {
	/**
     * 用户名（登录邮箱）
     */
    private String username;
    /**
     * 密码
     */
    private String password;
 
    /**
     * 初始化邮箱和密码
     * 
     * @param username 邮箱
     * @param password 密码
     */
    public MailAuthenticator(String username, String password) {
    this.username = username;
    this.password = password;
    }
 
    String getPassword() {
    return password;
    }
 
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(username, password);
    }
 
    String getUsername() {
    return username;
    }
 
    public void setPassword(String password) {
    this.password = password;
    }
 
    public void setUsername(String username) {
    this.username = username;
    }
}