# 西米的梦境师计划

__西米的梦境师计划__ 是一个开放的文字游戏加载器，通过载入外部的资源包来生成特定的故事模板，并通过资源包中的项目信息来推进故事进展。

## 举个例子

- 《MBTI性格测试》、交互式小说这类选择式文字叙述是最简单编写的（我是指资源包），基本上只需要写`故事`、`事件`与`分支`即可。
- 《Lifeline（生命线）》、《A Dark Room》、《人生重开模拟器》这类文字游戏就需要用到`标签`。
- 文字模拟类游戏就可能会用到`道具`、`规则`与`技能`，这取决于编写者。

## 特点

- __开放世界__。开放给所有玩家的一个故事世界，只要你会写`json`，你就可以书写你的故事。
- __存档迁移__。存档信息包括在数个`json`文件中，只需要保存这些存档文件即可在不同设备或是不同平台间同步存档。
- __高度自定义的资源包__。在资源包中的项目概率、执行条件与执行效果都是动态的，例如条件可以使用以下方式进行书写：

  `@{role.info.age.value}>12 & @{role.attr.mood.value}>50;@{role.tag.has happy}`

  这表示了当角色年龄大于12且角色心情大于50时，或者角色带有`happy`标签时生效。

## 缺点

- 没有图片，是的，没法加入图片。
- 没有音效和背景音乐，因为这只是一个框架。

## 项目

资源包加载的所有数据类型都被称为 __项目__，只有项目才可以动态加载。

项目列表：

- [__故事开端__](docs/domain/story.md)。每个存档只能有一个故事开端，用来作为存档开始的背景描述，并进行初始设定。
- [__分支__](docs/domain/branch.md)。分支表示了一串带有共同特点的事件的集合，大分支例如`上小学`、`疫情`等，小分支例如`学车`、`疾病`等。
- [__事件__](docs/domain/event.md)。基础触发项目，可以绑定在分支中，也可以直接独立存在。例如`路遇同学`、`被鱼刺卡住`等。
- [__选项__](docs/domain/option.md)。选项是基于事件的，但一个选项可以绑定多个事件。并且一个选项可以提供多个结果，但这些结果只会有一个被触发，由结果的条件与概率来决定。
- [__道具__](docs/domain/item.md)。提供给玩家使用的项目，用来主动触发效果。
- [__规则__](docs/domain/rule.md)。规则表示了每回合需要执行的效果，用来配合故事开端来形成一个世界背景。也可以配合标签来形成buff。
- [__标签__](docs/domain/tag.md)。标签是角色所属的项目，一般是用作项目条件判断。
- [__技能__](docs/domain/skill.md)。技能是角色拥有的属性，提供了等级规范。

[资源包](docs/资源包编写.md) 编写时可以参考 [数据编写说明](docs/数据编写说明.md) 与 [变量与执行描述](docs/变量与执行描述.md)。

在阅读完以上文档后，创作者可以参考 [项目创建演示](docs/项目创建演示.md) 中的实例来入门。

## 未来计划

- [ ] 2.0 正式版
- [ ] Android端
- [ ] 命令行端
- [ ] Windows端

## 使用

使用流程为：

1. 添加资源包
2. 新建游戏对象
3. 开始游戏
4. 进行操作

这里用命令行简单演示一下：

```java
public class main {

    public void simulation() {
        // 添加资源包
        PackManager pm = PackManager.getInstance();
        pm.loadFromFile(new File("packs\\Demo-RockPaperScissors.json"));
        pm.loadFromFile(new File("packs\\Demo-default.json"));
        List<Pack> packs = pm.getPacks();
        if (packs.size() > 0) {
            // 新建游戏
            Game game = Game.newGame(packMap.values());
            // 设定信息处理器，这很重要，因为这是用来输出所有文字的工具
            GAME_RUNNER.setMessageKit(System.out::println);
            // 开始游戏
            GAME_RUNNER.start(game);
            System.out.println();
            int i = 1;
            while (true) {
                if (!GAME_RUNNER.isFinish()) {
                    System.out.print("----------------------------------------- " + i++ + " -----------------------------------------\n" +
                            "1. 显示当前事件\n" +
                            "2. 跳过当前事件\n" +
                            "3. 下一回合\n" +
                            "4. 显示角色属性\n" +
                            "5. 显示背包\n" +
                            "6. 显示世界信息\n" +
                            "你的选择是：");
                    int opt = SCANNER.nextInt();
                    System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓\n");
                    select(opt);
                    System.out.println("\n↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
                } else {
                    break;
                }
            }
        }
    }
}
```

### 依赖

**请注意，1.X版本为预览版，2.0+版本才是稳定版。**

1. 添加Jitpack仓库源

   [![Release](https://jitpack.io/v/Verlif/sim-life-of-dream.svg)](https://jitpack.io/#Verlif/sim-life-of-dream)

    1. maven

   ```xml
   <repositories>
      <repository>
          <id>jitpack.io</id>
          <url>https://jitpack.io</url>
      </repository>
   </repositories>
   ```

    2. Gradle

   ```text
   allprojects {
     repositories {
         maven { url 'https://jitpack.io' }
     }
   }
   ```

2. 添加依赖

    1. maven

   ```xml
      <dependencies>
          <dependency>
              <groupId>com.github.Verlif</groupId>
              <artifactId>sim-life-of-dream</artifactId>
              <version>最新版本号</version>
          </dependency>
      </dependencies>
   ```

    2. Gradle

   ```text
   dependencies {
     implementation 'com.github.Verlif:sim-life-of-dream:最新版本号'
   }
   ```
