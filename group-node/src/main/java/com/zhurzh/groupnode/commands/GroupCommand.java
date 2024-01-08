package com.zhurzh.groupnode.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface GroupCommand {
    void execute(Update update);

}
