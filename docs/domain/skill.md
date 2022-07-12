# 技能

技能是依附于角色的自定义属性值，由资源包提供。

## 属性

- __name(must) - 技能名称。
- __key__ - 技能唯一键值。默认为技能名称。
- __level__ - 技能当前等级。默认值为`0`。
- __value__ - 技能值。默认值为`0`。
- __next__ - 技能升级所需的技能值。默认值为`20`。
- __onAdd__ - 获得技能时触发效果。
- __onRemove__ - 失去技能时触发效果。
- __autoRemove__ - 当技能等级小于`0`或技能值为负数时，自动移除此技能。默认值为`false`。

举例：

```json
{
  "name": "蛋炒饭",
  "key": "蛋炒饭",
  "level": 0,
  "value": 0,
  "next": 20
  "onAdd": "kit.message 你现在会做蛋炒饭了。",
  "onRemove": "kit.message 你已经忘记怎样做蛋炒饭了。",
  "autoRemove": true
}
```

最简写法：

```json
{
  "name": "蛋炒饭"
}
```

## 其他

- 当通过`role.skill.up`进行技能值提升时，当提升后的技能值满足`next`要求，会自动升级，此时技能值会扣除`next`数值，随后`next`变为两倍。
- 当使用`role.skill.set`时，会自动升级或降级。