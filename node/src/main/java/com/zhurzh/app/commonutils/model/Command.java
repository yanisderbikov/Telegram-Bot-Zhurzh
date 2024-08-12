package com.zhurzh.app.commonutils.model;

import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonutils.exception.CommandException;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    void execute(AppUser appUser, Update update) throws CommandException;
    boolean isExecuted(AppUser appUser);
}
