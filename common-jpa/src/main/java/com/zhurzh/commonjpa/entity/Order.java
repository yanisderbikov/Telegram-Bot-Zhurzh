package com.zhurzh.commonjpa.entity;

import com.zhurzh.commonjpa.enums.BackgroundOfIllustration;
import com.zhurzh.commonjpa.enums.CountOfPersons;
import com.zhurzh.commonjpa.enums.DetalizationOfIllustration;
import com.zhurzh.commonjpa.enums.FormatOfIllustration;
import lombok.*;

import javax.persistence.*;

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
                "\nyours set price is = " + price;
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
}
