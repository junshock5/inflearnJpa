package com.junshock.jpatest.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "category_item", // n:m일때 객체는 collection 두개다 들고있어야 하기에 중간에 맵핑이 필요하다. 필드 추가 못하여 실무에서 사용 x
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")) //item쪽으로 들어가는 칼럼
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent") // 양방향 관계
    private List<Category> child = new ArrayList<>();

    // ==연관관계 메서드== //
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
