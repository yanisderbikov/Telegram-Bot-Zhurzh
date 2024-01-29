package com.zhurzh.commonjpa.entity;

import com.zhurzh.commonjpa.enums.*;
import com.zhurzh.commonjpa.service.CurrencyConverter;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@ToString
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private AppUser owner;
    @Builder.Default
    private Boolean isFinished = false;
    @Builder.Default
    private StatusZhurzh statusZhurzh = StatusZhurzh.UNSEEN;

    private Date deadLine;
    @Enumerated(EnumType.STRING)
    private CountOfPersons countOfPersons;
    @Column(columnDefinition = "TEXT")
    private String artReference;
    @Enumerated(EnumType.STRING)
    private FormatOfIllustration formatOfIllustration;
    @Enumerated(EnumType.STRING)
    private DetalizationOfIllustration detalizationOfIllustration;
    @Enumerated(EnumType.STRING)
    private BackgroundOfIllustration backgroundOfIllustration;
    @Column(columnDefinition = "TEXT")
    private String commentToArt;
    private String price;

    @Override
    public String toString(){
        List<String[]> table = new ArrayList<>();
        var lan = owner.getLanguage();
        if (lan.equals("ru")) {
            table.add(null);
            table.add(new String[]{"Имя", isNull(name) ? unfilled() : name});
            table.add(new String[]{"Статус", statusZhurzh.getMessage(lan)});
            table.add(new String[]{"Дедлайн", isNull(deadLine) ? unfilled() : new SimpleDateFormat("dd-MM-yyyy").format(deadLine)});
            table.add(null);
            table.add(new String[]{"Персонажи", (isNull(countOfPersons) ? unfilled() : countOfPersons.getMessage(lan))});
            table.add(new String[]{"Референсы", (artReference == null || artReference.isEmpty() ? unfilled() : "Загружены")});
            table.add(new String[]{"Формат", (isNull(formatOfIllustration) ? unfilled() : formatOfIllustration.getMessage(lan))});
            table.add(new String[]{"Детализация", (isNull(detalizationOfIllustration) ? unfilled() : detalizationOfIllustration.getMessage(lan))});
            table.add(new String[]{"Фон", (isNull(backgroundOfIllustration) ? unfilled() : backgroundOfIllustration.getMessage(lan))});
            table.add(new String[]{"Рассчитанная стоимость", calculatePrice() });
            table.add(new String[]{"Твоя стоимость", isNull(price) ? unfilled() : price});
            table.add(new String[]{"Комментарий", isNull(commentToArt) ? unfilled() : commentToArt});
        }else {
            table.add(null);
            table.add(new String[]{"Name", isNull(name) ? unfilled() : name});
            table.add(new String[]{"Status", statusZhurzh.getMessage(lan)});
            table.add(new String[]{"Deadline", isNull(deadLine) ? unfilled() : new SimpleDateFormat("MM-dd-yyyy").format(deadLine)});
            table.add(null);
            table.add(new String[]{"Persons", (isNull(countOfPersons) ? unfilled() : countOfPersons.getMessage(lan))});
            table.add(new String[]{"Reference", (artReference == null || artReference.isEmpty() ? unfilled() : "Uploaded")});
            table.add(new String[]{"Format", (isNull(formatOfIllustration) ? unfilled() : formatOfIllustration.getMessage(lan))});
            table.add(new String[]{"Detalization", (isNull(detalizationOfIllustration) ? unfilled() : detalizationOfIllustration.getMessage(lan))});
            table.add(new String[]{"Background", (isNull(backgroundOfIllustration) ? unfilled() : backgroundOfIllustration.getMessage(lan))});
            table.add(new String[]{"Calculated price", calculatePrice()});
            table.add(new String[]{"Your price", isNull(price) ? unfilled() : price});
            table.add(new String[]{"Comment", isNull(commentToArt) ? unfilled() : commentToArt});
        }

        return convertToTelegramFormat2(table);
    }

    private static String convertToTelegramFormat2(List<String[]> table) {
        StringBuilder sb = new StringBuilder();
        for (String[] strings : table) {
            if (strings == null) {
                sb.append("\n");
                continue;
            }
            sb.append(String.format("%n<b>%s:</b> %s", strings[0], strings[1]));
        }
        return sb.toString();
    }


    private boolean isNull(Object o){
        return o == null;
    }
    private String unfilled(){
        return owner.getLanguage().equals("eng") ? "Unfilled" : "Не заполнено";
    }
    public boolean isAllFilled(){
        return
                isAllFilledExceptPrice()
                && ! isNull(price)
                ;

    }
    public boolean isAllFilledExceptPrice(){
        return
                   ! isNull(name)
                && ! isNull(owner)
                && ! isNull(countOfPersons)
                && ! (artReference == null || artReference.isEmpty())
                && ! isNull(formatOfIllustration)
                && ! isNull(detalizationOfIllustration)
                && ! isNull(backgroundOfIllustration)
                && ! isNull(commentToArt)
                && ! isNull(deadLine)
                ;
    }
    public String calculatePrice() {
        if (isAllFilledExceptPrice()) {
            try {
                if (owner.getLanguage().equals("eng")) {
                    return String.format("%s", generate());
                } else {
                    return String.format("%s", generate());
                }
            } catch (Exception e) {
                throw new RuntimeException("Fail to calculate : " + e.getCause().getMessage());
            }
        }else {
            return owner.getLanguage().equals("eng") ? "Not calculated" : "Не расчитано";
        }
    }
    private String generate(){
        double min = 1;
        double max = 1;
        switch (detalizationOfIllustration){
            case DETAILED -> {
                switch (formatOfIllustration){
                    case PORTRAIT -> min = 170;
                    case HALF_BODY -> min = 210;
                    case FULL_BODY -> min = 250;
                }
            }
            case LINE_ART_SHADING -> {
                switch (formatOfIllustration){
                    case PORTRAIT -> min = 100;
                    case HALF_BODY -> min = 130;
                    case FULL_BODY -> min = 185;
                };
                min *= 1.3;
            }
            case CLASSICAL -> {
                switch (formatOfIllustration){
                    case PORTRAIT -> min = 85;
                    case HALF_BODY -> min = 130;
                    case FULL_BODY -> min = 155;
                }
            }
            case BLACK_AND_WHITE_SKETCH -> {
                switch (formatOfIllustration){
                    case PORTRAIT -> min = 55;
                    case HALF_BODY -> min = 70;
                    case FULL_BODY -> min = 100;
                }
            }
        }
        switch (countOfPersons){
            case TWO -> min *= 1.50;
            case MORE_THAN_TWO -> min *= 1.90;
        }
        switch (backgroundOfIllustration){
            case SIMPLE_WITH_ELEMENTS_OF_BLUR -> {
                min *= 1.10;
                max = min + 50;
            }
            case DETAILED -> {
                min += 50;
            }
        }
        var isRu = owner.getLanguage().equals("ru");
        if (isRu) {
            double cur = CurrencyConverter.getActualCurrency(); // курс
            min *= 0.7;
            max *= 0.7;
            min = Math.ceil(min * cur / 1000) * 1000;
            max = Math.ceil(max * cur / 1000) * 1000;;
        }

        int minRound = (int) Math.ceil(min);
        int maxRound = (int) Math.ceil(max);

        var out = "";
        if (min > max){
            out = String.format("%s", minRound);
        }else {
            out = String.format("%s-%s", minRound, maxRound);
        }
        out += isRu ? " Руб" : " USD";
        return out;
    }
}
