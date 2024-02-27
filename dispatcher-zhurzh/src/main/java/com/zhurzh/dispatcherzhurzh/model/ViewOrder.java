package com.zhurzh.dispatcherzhurzh.model;

import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonjpa.enums.StatusZhurzh;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ViewOrder extends Order {
    private String calculatedPrice;

    public ViewOrder(Order order) {
        super(order.getId(), order.getName(), order.getOwner(), order.getIsFinished(),
                order.getStatusZhurzh(), order.getDeadLine(), order.getCountOfPersons(),
                order.getArtReference(), order.getFormatOfIllustration(),
                order.getDetalizationOfIllustration(), order.getBackgroundOfIllustration(),
                order.getCommentToArt(), order.getPrice());
        this.calculatedPrice = calculatePrice();
    }

    public boolean isStatusEditable() {
        // Ваша логика определения, можно ли изменять статус
        // Например, вернуть true, если статус "Unseen" или "Seen"
        return this.getStatusZhurzh() == StatusZhurzh.UNSEEN || this.getStatusZhurzh() == StatusZhurzh.SEEN;
    }
}
