#### 添加新的物品类型
```java
@JsonSubTypes({
        @JsonSubTypes.Type(value = Equip.class),
        @JsonSubTypes.Type(value = NormalItem.class),
})
public abstract class AbstractItem implements Comparable<AbstractItem> {
    
}
```
- 继承上述类，并添加jackson 的@JsonSubTypes.Type
- 在ItemType中添加枚举
- 继承AbstractItemCreator<T extends AbstractItem>创建相应的创建器

创建出来的物品数量超过其堆叠数的会在Container.addItem中自动变成多件物品。