package com.lg.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员
 * </p>
 *
 * @author liuguo
 * @since 2021-04-15
 */
@Data
@TableName("ums_member")
public class UmsMember extends Model<UmsMember> {

    private static final long serialVersionUID = 1L;

    //id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //会员等级id
    @TableField("level_id")
    private Long levelId;

    //用户名
    @TableField("username")
    private String username;

    //密码
    @TableField("password")
    private String password;

    //昵称
    @TableField("nickname")
    private String nickname;

    //手机号码
    @TableField("mobile")
    private String mobile;

    //邮箱
    @TableField("email")
    private String email;

    //头像
    @TableField("header")
    private String header;

    //性别
    @TableField("gender")
    private Integer gender;

    //生日
    @TableField("birth")
    private Date birth;

    //所在城市
    @TableField("city")
    private String city;

    //职业
    @TableField("job")
    private String job;

    //个性签名
    @TableField("sign")
    private String sign;

    //用户来源
    @TableField("source_type")
    private Integer sourceType;

    //积分
    @TableField("integration")
    private Integer integration;

    //成长值
    @TableField("growth")
    private Integer growth;

    //启用状态
    @TableField("status")
    private Integer status;

    //注册时间
    @TableField("create_time")
    private Date createTime;

    //社交用户的唯一id
    @TableField("social_uid")
    private String socialUid;

    //访问令牌
    @TableField("access_token")
    private String accessToken;

    //访问令牌的时间
    @TableField("expires_in")
    private String expiresIn;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}