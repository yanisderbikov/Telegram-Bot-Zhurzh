package com.zhurzh.app.commonutils.model;

import com.zhurzh.app.commonjpa.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@AllArgsConstructor
public class Body {
    private AppUser appUser;
    private Update update;
}
