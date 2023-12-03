package com.zhurzh.model;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;

public interface Branches {
    public boolean isActive();
    public boolean manageCallBack(Update update, AppUser appUser);
    public boolean manageText(Update update, AppUser appUser);
}
