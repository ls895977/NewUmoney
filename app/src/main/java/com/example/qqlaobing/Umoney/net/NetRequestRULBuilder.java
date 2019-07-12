package com.example.qqlaobing.Umoney.net;

public class NetRequestRULBuilder {

    /**
     * 方 法 名: buildRequestUrl 功能描述: 拼装网络请求的接口 参数说明: @param aReqType 网络请求的类型 返 回
     * 值: String 接口的url
     *  * 创  建  者: zhonglz
     * 创建日期: 2018年6月22日
     */
    public static String buildRequestUrl(int aReqType) {
        String urlString = "";
        switch (aReqType) {
            case NetRequestBusinessDefine.K_GETS:
                urlString=NetRequestBusinessDefine.K_HOST_NAME + "/public/getSmsVerify";
                break;
            case NetRequestBusinessDefine.K_LOGIN:
                urlString=NetRequestBusinessDefine.K_HOST_NAME + "/public/login";
                break;
            case NetRequestBusinessDefine.K_POSTUSER_INFO:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/saveInfo";
                break;
            case NetRequestBusinessDefine.K_BANK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/savebankinfo";
                break;
            case NetRequestBusinessDefine.K_CONTACT:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/saveContact";
                break;
            case NetRequestBusinessDefine.K_OPERATOR:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/saveTmobile";
                break;
            case NetRequestBusinessDefine.K_ZHIMA:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/saveZmf";
                break;
            case NetRequestBusinessDefine.K_MARK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/index/user/authStatus";
                break;
            case NetRequestBusinessDefine.K_ODER_LIST:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/loan/lists";
                break;
            case NetRequestBusinessDefine.K_ODER:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/loan/get";
                break;
            case NetRequestBusinessDefine.K_REGISTE:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/public/register";
                break;
            case NetRequestBusinessDefine.K_HOME:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/home/dashbord";
                break;
            case NetRequestBusinessDefine.K_WORK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/saveWork";
                break;
            case NetRequestBusinessDefine.K_GET_CONTACT:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/contact";
                break;
            case NetRequestBusinessDefine.K_GET_AUTHINFO:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/authInfo";
                break;
            case NetRequestBusinessDefine.K_GET_BANK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/bank";
                break;
            case NetRequestBusinessDefine.K_GET_MYCONTACT:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/contact";
                break;
            case NetRequestBusinessDefine.K_GET_OPERATOR:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/tmobile";
                break;
            case NetRequestBusinessDefine.K_GET_WORK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/work";
                break;
            case NetRequestBusinessDefine.K_GET_ZHIMA:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/zmfInfo";
                break;
            case NetRequestBusinessDefine.K_GET_USERINFO:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/Info";
                break;
            case NetRequestBusinessDefine.K_POST_APPLY:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/applyConfirm";
                break;
            case NetRequestBusinessDefine.K_GET_QUAN:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/coupons";
                break;
            case NetRequestBusinessDefine.K_GET_LISTS:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/loan/lists";
                break;
            case NetRequestBusinessDefine.K_GET_MONEY:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/loan/applyLoan";
                break;
            case NetRequestBusinessDefine.K_UPDATA:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/public/checkforupdate";
                break;
            case NetRequestBusinessDefine.K_ERROR:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/Identity/errlog";
                break;
            case NetRequestBusinessDefine.K_CHANGEPASSWORD:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/modify_password";
                break;
            case NetRequestBusinessDefine.K_FINDPASSWORD:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/public/forget";
                break;
            case NetRequestBusinessDefine.K_GETBACK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/bank/validBanks";
                break;
            case NetRequestBusinessDefine.K_GETBACKLIST:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/bank/lists";
                break;
            case NetRequestBusinessDefine.K_ADDBACK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/bank/update";
                break;
            case NetRequestBusinessDefine.K_DELLETBACK:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/bank/delete";
                break;
            case NetRequestBusinessDefine.K_LOGIN_CODE:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/public/codeLogin";
                break;
            case NetRequestBusinessDefine.K_selectProducts:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/selectProducts";
                break;
            case NetRequestBusinessDefine.K_comfirmProduct:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/user/comfirmProducts";
                break;
            case NetRequestBusinessDefine.K_get:
                urlString = NetRequestBusinessDefine.K_HOST_NAME + "/loan/get";
                break;
            default:
                break;
        }
        return urlString;
    }
}
