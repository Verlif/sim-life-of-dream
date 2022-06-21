# 分支

分支在概念上表示为一个事件方向，在实际的项目中表示为一组事件集。

## 属性

- __name__ - 分支名称。
- __key__ - 分支唯一键值。
- __chance__ - 分支触发概率。分支默认触发概率为 __0__。
- __condition__ - 分支触发条件。分支默认 __不需要条件__。
- __exec__ - 分支触发效果。

举例：

```json
{
  "name": "小学",
  "key": "小学",
  "chance": "10000",
  "condition": "@{role.info.age.value}>5 & @{role.info.age.value}<13",
  "exec": "kit.message 你开始上小学了。"
}
```

## 其他