# 标签

标签一般用于一些特殊的标记，例如标记角色`生病`，可以添加一个`生病`的标签，用于某些事件或是选项的判定。

## 属性

- name - 标签名称。
- key - 标签唯一键值。角色标签支持分组，使用`.`进行组分隔，例如`location.sichaun.mianyang`。
- value - 标签值。一般用于组合触发判定使用，例如`生病`的病重等级。
- visible - 标签是否显示。此属性用于UI展示用。
- onAdd - 在标签添加时触发效果。
- onRemove - 在标签移除时触发效果。

举例：

```json
{
  "name": "生病",
  "key": "生病",
  "value": 1,
  "visible": true,
  "onAdd": "",
  "onRemove": ""
}
```

## 其他

- 当角色没有此标签而新添加`role.tag.add`时，会触发一次`onAdd`。
- 当角色拥有此标签并移除`role.tag.remove`时，会触发一次`onRemove`。
- 在使用`role.tag.add`、`role.tag.remove`、`role.tag.up`、`role.tag.down`时，都是会对标签组进行操作的。

    - 总标签有`location.sichaun`、`location.sichaun.mianyang`、`location.sichuan.chengdu`、`location.chongqin`。
    - 角色拥有的标签有`location.sichaun.mianyang`。
    - 此时使用`role.tag.up location 2`，则会添加`location.sichaun`、`location.sichuan.chengdu`、`location.chongqin`这三个标签，之后将所有的标签值提升2。
    - 之后使用`role.tag.remove location.sichuan`，则会移除`location.sichaun`、`location.sichaun.mianyang`、`location.sichuan.chengdu`这三个标签，只留下`location.chongqin`标签。

