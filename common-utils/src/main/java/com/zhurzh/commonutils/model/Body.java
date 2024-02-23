package com.zhurzh.commonutils.model;

import com.zhurzh.commonjpa.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@AllArgsConstructor
public class Body {
    private AppUser appUser;
    private Update update;
}
