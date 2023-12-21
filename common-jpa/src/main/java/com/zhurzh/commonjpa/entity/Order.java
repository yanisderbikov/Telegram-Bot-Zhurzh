package com.zhurzh.commonjpa.entity;

import com.zhurzh.commonjpa.enums.*;
import lombok.*;

import javax.persistence.*;
import java.util.Map;

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
    @Enumerated(EnumType.STRING)
    private CountOfPersons countOfPersons;
    private String reference;
    @Enumerated(EnumType.STRING)
    private FormatOfIllustration formatOfIllustration;
    @Enumerated(EnumType.STRING)
    private DetalizationOfIllustration detalizationOfIllustration;
    @Enumerated(EnumType.STRING)
    private BackgroundOfIllustration backgroundOfIllustration;
    private String commentToArt;
    private String price;

    @Override
    public String toString() {
        var len = owner.getLanguage();
        return
                "\nname='" + name + '\'' +
                "\ncountOfPersons=" + (isNull(countOfPersons) ? unfilled() : countOfPersons.getMessage(owner.getLanguage())) +
                "\nreference='" + reference + '\'' +
                "\nformatOfIllustration=" + (isNull(formatOfIllustration) ? unfilled() : formatOfIllustration.getMessage(owner.getLanguage())) +
                "\ndetalizationOfIllustration=" +   (isNull(detalizationOfIllustration) ? unfilled() : detalizationOfIllustration.getMessage(owner.getLanguage()))+
                "\nbackgroundOfIllustration=" +     (isNull(backgroundOfIllustration) ? unfilled() : backgroundOfIllustration.getMessage(owner.getLanguage())  )+
                "\ncommentToArt='" + commentToArt + '\'' +
                "\nyours set price is = " + price +
                        "\ncalculated price " + calculatePrice() +
                "\nstatus = " + statusZhurzh.getMessage(len);
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
                && ! isNull(reference)
                && ! isNull(formatOfIllustration)
                && ! isNull(detalizationOfIllustration)
                && ! isNull(backgroundOfIllustration)
                && ! isNull(commentToArt)
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
