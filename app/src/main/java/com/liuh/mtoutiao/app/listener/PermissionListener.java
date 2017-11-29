package com.liuh.mtoutiao.app.listener;

import java.util.List;

/**
 * Author:liuh
 * Date: 2017/11/29 17:00
 * Description:申请权限的回调
 */

public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
