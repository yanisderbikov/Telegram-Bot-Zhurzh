package com.zhurzh.bot.branches.order;

import com.zhurzh.model.Branches;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;

@Component
public class CheckOrderManager implements Branches {
//    CommndsMana
    @Override
    public boolean isActive() {

        return false;
    }

    @Override
    public boolean manageCallBack(Update update, AppUser appUser) {
        return false;
    }

    @Override
    public boolean manageText(Update update, AppUser appUser) {
        return false;
    }
}
