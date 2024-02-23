package com.zhurzh.node.bot.branches;

import com.zhurzh.commonjpa.entity.AppUser;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BranchesManagerInterface {
    void consume(Update update);
}
