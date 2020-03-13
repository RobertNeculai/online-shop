package org.fasttrackit.onlineshop.transfer.product;

import java.util.StringJoiner;

public class GetProductsRequest {
    private String partialName;
    private Integer minQuantity;

    public String getPartialName() {
        return partialName;
    }

    public void setPartialName(String partialName) {
        this.partialName = partialName;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GetProductsRequest.class.getSimpleName() + "[", "]")
                .add("partialName='" + partialName + "'")
                .add("minQuantity=" + minQuantity)
                .toString();
    }
}
