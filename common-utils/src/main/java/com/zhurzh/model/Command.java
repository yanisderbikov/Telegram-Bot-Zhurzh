package com.zhurzh.model;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.exception.CommandException;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    void execute(Update update) throws CommandException;
}
