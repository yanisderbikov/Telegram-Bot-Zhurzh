package com.zhurzh.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailParams { //Data Transfer Object (DTO) - один из шаблонов
//            «проектирования, используется для передачи данных между подсистемами приложения.

    // с помощью этого сразу будет создаваться объект в джаве, а не JSON объект

    private String id;
    private String emailTo;
}
