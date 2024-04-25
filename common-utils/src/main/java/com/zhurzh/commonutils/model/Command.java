package com.zhurzh.commonutils.model;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonutils.exception.CommandException;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    void execute(AppUser appUser, Update update) throws CommandException;
    boolean isExecuted(AppUser appUser);
}
