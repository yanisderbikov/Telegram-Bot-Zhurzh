package com.zhurzh.commonjpa.entity;

import com.zhurzh.commonjpa.enums.*;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    @ElementCollection // Указывает, что это коллекция элементов
//    @CollectionTable(name = "order_references", joinColumns = @JoinColumn(name = "order_id")) // Определяет имя таблицы и столбца соединения
    private List<String> artReference;
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
            table.add(new String[]{"Имя", name});
            table.add(new String[]{"Статус", statusZhurzh.getMessage(lan)});
            table.add(new String[]{"Дедлайн", isNull(deadLine) ? unfilled() : new SimpleDateFormat("dd-MM-yyyy").format(deadLine)});
            table.add(null);
            table.add(new String[]{"Персонажи", (isNull(countOfPersons) ? unfilled() : countOfPersons.getMessage(lan))});
            table.add(new String[]{"Референсы", (artReference == null || artReference.isEmpty() ? unfilled() : String.format("Количество фалов - %s", artReference.size()))});
            table.add(new String[]{"Формат", (isNull(formatOfIllustration) ? unfilled() : formatOfIllustration.getMessage(lan))});
            table.add(new String[]{"Детализация", (isNull(detalizationOfIllustration) ? unfilled() : detalizationOfIllustration.getMessage(lan))});
            table.add(new String[]{"Фон", (isNull(backgroundOfIllustration) ? unfilled() : backgroundOfIllustration.getMessage(lan))});
            table.add(new String[]{"Рассчитанная стоимость", price});
            table.add(new String[]{"Твоя стоимость", generate()});
            table.add(new String[]{"Комментарий", commentToArt});
        }else {
            table.add(null);
            table.add(new String[]{"Name", name});
            table.add(new String[]{"Status", statusZhurzh.getMessage(lan)});
            table.add(new String[]{"Deadline", isNull(deadLine) ? unfilled() : new SimpleDateFormat("MM-dd-yyyy").format(deadLine)});
            table.add(null);
            table.add(new String[]{"Persons", (isNull(countOfPersons) ? unfilled() : countOfPersons.getMessage(lan))});
            table.add(new String[]{"Reference", (artReference == null || artReference.isEmpty() ? unfilled() : String.format("Uploaded files - %s ", artReference.size()))});
            table.add(new String[]{"Format", (isNull(formatOfIllustration) ? unfilled() : formatOfIllustration.getMessage(lan))});
            table.add(new String[]{"Detalization", (isNull(detalizationOfIllustration) ? unfilled() : detalizationOfIllustration.getMessage(lan))});
            table.add(new String[]{"Background", (isNull(backgroundOfIllustration) ? unfilled() : backgroundOfIllustration.getMessage(lan))});
            table.add(new String[]{"Calculated price", price});
            table.add(new String[]{"Your price", generate()});
            table.add(new String[]{"Comment", commentToArt});
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
        return owner.getLanguage().equals("eng") ? "Onfilled" : "Не заполнено";
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
                    return String.format("\nAround price is %s$", generate());
                } else {
                    return String.format("\nПримерная цена %s руб", generate());
                }
            } catch (Exception e) {
                throw new RuntimeException("Fail to calculate : " + e.getCause().getMessage());
            }
        }else {
            return owner.getLanguage().equals("eng") ? "Not calculated" : "Не расчитано";
        }
    }
    private String generate(){
        int min = 0;
        int max = 0;
        switch (detalizationOfIllustration){
            case DETAILED -> {
                switch (formatOfIllustration){
                    case PORTRAIT -> min = 170;
                    case HALF_BODY -> min = 210;
                    case FULL_BODY -> min = 250;
                }
            }
            case LINE_ART -> {
                switch (formatOfIllustration){
                    case PORTRAIT -> min = 100;
                    case HALF_BODY -> min = 130;
                    case FULL_BODY -> min = 185;
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
        if (owner.getLanguage().equals("ru")) {
            int cur = 90; // курс
            min *= cur;
            max += cur;
        }

        int minRound = (int) Math.ceil(min);
        int maxRound = (int) Math.ceil(max);


        if (min > max){
            return String.format("%s", minRound);
        }else {
            return String.format("%s-%s", minRound, maxRound);
        }
    }
}
