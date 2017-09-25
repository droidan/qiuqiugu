package com.tiger.socol.gu.managers;

import android.content.Context;
import android.content.Intent;

import com.tiger.socol.gu.activity.mine.login.LoginActivity;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.utils.RealmMannager;

import java.util.List;

/**
 * 用户登陆信息管理类
 */
public class MemberMannager {

    private Member mLoginMember = null;
    private static MemberMannager single = null;

    private MemberMannager() {
        mLoginMember = getLoginUser();
    }

    public static MemberMannager getInstance() {
        if (single == null) {
            synchronized (MemberMannager.class) {
                if (single == null) {
                    single = new MemberMannager();
                }
            }
        }
        return single;
    }

    /**
     * 是否登录
     *
     * @return boolean
     */
    public boolean isLogin() {
        return mLoginMember != null;
    }

    /**
     * 获取当前登录用户信息
     * 未登录返回 null
     *
     * @return MemberEntity / null
     */
    public Member getLoginUser() {
        if (mLoginMember != null) {
            return mLoginMember;
        }

        List<Member> users = RealmMannager.getInstance().syncFindAll(Member.class);
        if (users.size() == 0)
            return null;

        // * 修改或删除 RealmResults 中任何一个对象都必须在写入事务中完成。
        // 所以这里重新new了一个
        mLoginMember = new Member(users.get(0));
        return mLoginMember;
    }

    /**
     * 保存登录用户信息
     *
     * @param entity MemberEntity
     */
    public void savaMember(Member entity) {
        // 只存储一个登录用户
        logout();

        mLoginMember = entity;
        RealmMannager.getInstance().syncSava(entity);
    }

    /**
     * 获取用户id
     *
     * @return userid
     */
    public String getUserid() {
        if (!isLogin())
            return null;

        return mLoginMember.getUserId() + "";
    }


    /**
     * 更新用户信息
     *
     * @param entity MemberEntity
     */
    public void updataUser(Member entity) {
        mLoginMember = entity;
        savaMember(entity);
    }

    /**
     * 退出登录
     */
    public void logout() {
        // 清空当前登录用户信息
        mLoginMember = null;
        RealmMannager.getInstance().syncDeleteAll(Member.class);
    }

    public static boolean checkLogin(Context context) {
        if (!MemberMannager.getInstance().isLogin()) {
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

}