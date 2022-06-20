# 分支

分支在概念上表示为一个事件方向，在实际的项目中表示为一组事件集。

## 属性

- name - 分支名称。
- key - 分支唯一键值。
- chance - 分支触发概率。
- condition - 分支触发条件。
- exec - 分支触发效果。

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