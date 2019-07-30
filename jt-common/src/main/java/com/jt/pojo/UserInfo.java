package com.jt.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@TableName("tb_user_info")
public class UserInfo extends BasePojo{
    private Integer userid;//用户id
    private String username;//用户名
	private String sex;//性别
	private String Status;//婚姻状况
	private Date  birthdayYear;//表示年
	private Date birthdayMonth;//表示月
	private Date birthdayDay;//表示天
	private Integer num;//身份证
	private Double income;//收入
	private String education;//学历
	private String trade;//行业
	private String name;//真实名字
	private String site;//地址
}
