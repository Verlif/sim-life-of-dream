# 标签

标签一般用于一些特殊的标记，例如标记角色`生病`，可以添加一个`生病`的标签，用于某些事件或是选项的判定。

## 属性

- __name(must)__ - 标签名称。
- __key__ - 标签唯一键值。角色标签支持分组，使用`.`进行组分隔，例如`location.sichaun.mianyang`。默认为标签名称。
- __value__ - 标签值。一般用于组合触发判定使用，例如`生病`的病重等级。默认`0`。
- __visible__ - 标签是否显示。此属性用于UI展示用。默认 __可见__。
- __onAdd__ - 在标签添加时触发效果。
- __onTurn__ - 在每个新回合都会触发的效果。
- __onRemove__ - 在标签移除时触发效果。
- __order__ - 排序权重。权重越大，显示排序越 __靠后__，权重相同时随机排序。默认 __20__。

举例：

```json
{
  "name": "生病",
  "key": "生病",
  "value": 1,
  "visible": true,
  "onAdd": "",
  "onTurn": "",
  "onRemove": "",
  "order": 1
}
```

最简写法：

```json
{
  "name": "生病"
}
```

## 其他

- 当角色没有此标签而新添加`role.tag.add`时，会触发一次`onAdd`。
- 当角色拥有此标签并移除`role.tag.remove`时，会触发一次`onRemove`。
- 在使用`role.tag.add`、`role.tag.remove`、`role.tag.up`、`role.tag.down`时，都是会对标签组进行操作的。

    - 总标签有`location.sichaun`、`location.sichaun.mianyang`、`location.sichuan.chengdu`、`location.chongqin`。
    - 角色拥有的标签有`location.sichaun.mianyang`。
    - 此时使用`role.tag.up location 2`，则会添加`location.sichaun`、`location.sichuan.chengdu`、`location.chongqin`
      这三个标签，之后将所有的标签值提升2。
    - 之后使用`role.tag.remove location.sichuan`，则会移除`location.sichaun`、`location.sichaun.mianyang`
      、`location.sichuan.chengdu`这三个标签，只留下`location.chongqin`标签。

