# 规则

规则是用于对故事开端做补充的项目，在触发条件允许的情况下每回合触发一次。

## 属性

- __name__ - 规则名称。
- __key__ - 规则唯一键值。
- __desc__ - 规则描述。规则描述并不会在触发时展示，若需要展示信息请在触发效果中添加。
- __condition__ - 规则触发条件。
- __chance__ - 规则触发概率。
- __exec__ - 规则触发效果。

举例：

```json
{
  "name": "年龄提升",
  "key": "年龄提升",
  "desc": "每回合+1年龄",
  "exec": "role.info.age.up 1;kit.message \"你又涨了一岁，现在 @{role.info.age.value} 岁了。\""
}
```

## 其他