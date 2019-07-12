package com.example.qqlaobing.Umoney.net;

import com.example.qqlaobing.Umoney.BuildConfig;

public class NetRequestBusinessDefine {

	//是否登录，默认为否
	public static  boolean login=false;
	//是否有借款
	public static  boolean loan=false;
	public static  final int K_EXIT_APPLICATION = 2000;

	//测试环境
//    public static final String K_HOST_NAME = "http:"+BuildConfig.BASE_URL;
	//骆总
    public static final String K_HOST_NAME = "http://211.149.179.185/api";
	//钱到白卡
//	public static final String K_HOST_NAME = " http://211.149.188.95/api";

	//token
	public static  String K_TOKEN = "111111";
	//认证步骤
	public static  int STEP = 0;
    //订单ID
	public static  String oderid = "";
	public static  String oderno = "";


    // 固定值
    public static final int K_FINAL_TYPE_GENERAL = 10000;
	//登录
	public static final int K_LOGIN = K_FINAL_TYPE_GENERAL + 1;
	//注册
	public static final int K_REGISTE = K_LOGIN + 1;
	//获取验证码
	public static final int K_GETS = K_REGISTE + 1;
	//提交用户信息
	public static final int K_POSTUSER_INFO = K_GETS + 1;
	//保存工作信息
	public static final int K_WORK = K_POSTUSER_INFO + 1;
	//保存银行卡信息
	public static final int K_BANK = K_WORK + 1;
	//保存紧急联系人
	public static final int K_CONTACT = K_BANK + 1;
	//保存运营商
	public static final int K_OPERATOR = K_CONTACT + 1;
	//保存芝麻信用
	public static final int K_ZHIMA = K_OPERATOR + 1;
	//获取用户认证情况
	public static final int K_MARK = K_ZHIMA + 1;
	//订单列表
	public static final int K_ODER_LIST = K_MARK + 1;
	//订单详情
	public static final int K_ODER = K_ODER_LIST + 1;
	//首页数据
	public static final int K_HOME = K_ODER + 1;
	//获取联系方式
	public static final int K_GET_CONTACT = K_HOME + 1;
	//获取认证情况
	public static final int K_GET_AUTHINFO = K_GET_CONTACT + 1;
	//获取个人信息
	public static final int K_GET_USERINFO = K_GET_AUTHINFO + 1;
	//获取工作信息
	public static final int K_GET_WORK = K_GET_USERINFO + 1;
	//获取银行卡信息
	public static final int K_GET_BANK = K_GET_WORK + 1;
	//获取紧急联系人
	public static final int K_GET_MYCONTACT = K_GET_BANK + 1;
	//获取运营商信息
	public static final int K_GET_OPERATOR = K_GET_MYCONTACT + 1;
	//获取芝麻信用
	public static final int K_GET_ZHIMA = K_GET_OPERATOR + 1;
	//确认贷款
	public static final int K_POST_APPLY = K_GET_ZHIMA + 1;
	//优惠券
	public static final int K_GET_QUAN = K_POST_APPLY + 1;
	//获取订单列表
	public static final int K_GET_LISTS = K_GET_QUAN + 1;
	//获取额度
	public static final int K_GET_MONEY = K_GET_LISTS + 1;
	//更新
	public static final int K_UPDATA = K_GET_MONEY + 1;
	//错误详情
	public static final int K_ERROR = K_UPDATA + 1;
	//修改密码
	public static final int K_CHANGEPASSWORD = K_ERROR + 1;
	//找回密码
	public static final int K_FINDPASSWORD = K_CHANGEPASSWORD + 1;
	//获取所属银行
	public static final int K_GETBACK = K_FINDPASSWORD + 1;
	//获取银行卡列表
	public static final int K_GETBACKLIST = K_GETBACK + 1;
	//添加银行卡
	public static final int K_ADDBACK = K_GETBACKLIST + 1;
	//删除银行卡
	public static final int K_DELLETBACK= K_ADDBACK + 1;
	//动态登录
	public static final int K_LOGIN_CODE= K_DELLETBACK + 1;
	//选择产品
	public static final int K_selectProducts= K_LOGIN_CODE + 1;
	//选择产品下一步
	public static final int K_comfirmProduct= K_selectProducts + 1;
	//订单详情
	public static final int K_get= K_comfirmProduct + 1;

}
