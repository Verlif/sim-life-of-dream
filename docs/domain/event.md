# 事件

事件是整个游戏的核心，用来改变游戏路线的常用项目。

## 属性

- __afterBranches__ - 所属分支Key列表。当此列表存在值时，表示此事件只绑定了这些事件，也就是这个事件只会在这些分支被触发时才可能会被触发。
- __key(must)__ - 事件Key。用来标记事件的唯一键值。推荐使用`.`符号来将事件Key进行分组管理，例如`verlif.life.eating.happy`。
- __title(must)__ - 事件标题。用于事件展示。
- __desc(must)__ - 事件描述。用于事件展示。
- __condition__ - 事件触发条件（默认不需要条件）。
- __chance__ - 事件触发概率（默认概率0）。
- __exec__ - 事件触发效果。当事件开始时会被直接触发。
- __remain__ - 事件可触发次数（默认1次）。事件每触发一次，则会减少一次触发次数。当次数归零时则无法自动触发（依然可以使用效果将此事件加入触发队列并进行触发）。当`remain`为`-1`时可无限触发。

举例：

```json
{
  "afterBranches": [
    "小学"
  ],
  "key": "小学.作业忘带",
  "title": "作业忘带",
  "remain": "-1",
  "desc": "要交作业了，但是你似乎是把作业忘在家里了，现在该怎么办呢？",
  "chance": "1000 + @{role.param.attentive} * 50",
  "condition": "@{role.param.attentive} < 20",
  "exec": "kit.message 触发事件;addEventToReady 小学交作业"
}
```

## 其他

- 在使用`nextEvent`时就会直接触发返回的事件（重复的`nextEvent`同一个事件只会触发一次）。