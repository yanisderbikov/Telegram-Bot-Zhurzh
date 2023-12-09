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
                "\ncountOfPersons=" + countOfPersons.getMessage(len) +
                "\nreference='" + reference + '\'' +
                "\nformatOfIllustration=" + formatOfIllustration.getMessage(len) +
                "\ndetalizationOfIllustration=" + detalizationOfIllustration.getMessage(len) +
                "\nbackgroundOfIllustration=" + backgroundOfIllustration.getMessage(len) +
                "\ncommentToArt='" + commentToArt + '\'' +
                "\nyours set price is = " + price;
    }
}
