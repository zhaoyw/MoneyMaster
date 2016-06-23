package org.yinzhao.moneymaster.category;

import org.hibernate.annotations.GenericGenerator;
import org.yinzhao.moneymaster.enums.DealType;

import javax.persistence.*;

@Entity
@Table(name = "Category")
public class DealCategory {

    private long id;

    private DealType type;

    private String name;

    public DealCategory() {

    }

    public DealCategory(DealType type, String name) {
        this.type = type;
        this.name = name;
    }

    private void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId() {
        return id;
    }

    @Enumerated(EnumType.ORDINAL)
    public DealType getType() {
        return type;
    }

    public void setType(DealType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
