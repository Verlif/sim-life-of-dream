package idea.verlif.lifeofdream.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Verlif
 */
public class DescUtil {

    /**
     * 条件描述判断。<br/>
     * 1 + 2 * 3 - 4 / 2 < 3 * 5; 1 = 2; 3 > 3 + 4 / 1 * 2
     *
     * @param desc 条件描述
     * @return 条件是否成立
     */
    public static boolean test(String desc) {
        if (desc == null) {
            return true;
        }
        String[] lines = desc.split(";");
        for (String line : lines) {
            String[] ss = line.split("&");
            int count = 0;
            for (String s : ss) {
                String atom = s.trim();
                if (judge(atom)) {
                    count++;
                }
            }
            if (count == ss.length) {
                return true;
            }
        }
        return false;
    }

    /**
     * 原子条件判断。<br/>
     * 1 + 2 * 3 - 4 / 2 < 3 * 5
     *
     * @param atom 原子条件描述
     * @return 条件是否成立
     */
    public static boolean judge(String atom) {
        if (atom == null) {
            return true;
        }
        StringBuilder lsb = new StringBuilder();
        StringBuilder rsb = new StringBuilder();
        char j = 'a';
        char[] chars = atom.replace(" ", "").toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '<' || c == '>' || c == '=') {
                j = c;
                rsb.append(chars, i + 1, chars.length - i - 1);
                break;
            }
            lsb.append(c);
        }
        // 过滤非法语句
        if (j == 'a') {
            return false;
        }
        int left = result(lsb.toString());
        int right = result(rsb.toString());
        switch (j) {
            case '<':
                return left < right;
            case '>':
                return left > right;
            case '=':
                return left == right;
            default:
                return false;
        }
    }

    /**
     * 数字简单运算求值。仅支持加减乘除，不支持括号<br/>
     * 1+2*3-4/2
     *
     * @param desc 运算描述
     * @return 运算结果
     */
    public static int result(String desc) {
        if (desc == null || desc.trim().length() == 0) {
            return 0;
        }
        char[] chars = desc.toCharArray();
        // 由加减法进行分组
        StringBuilder sb = new StringBuilder();
        List<String> group = new ArrayList<>();
        List<Character> am = new ArrayList<>();
        for (char c : chars) {
            if (c == '+' || c == '-') {
                group.add(sb.toString());
                sb.setLength(0);
                am.add(c);
            } else {
                sb.append(c);
            }
        }
        if (sb.length() == 0) {
            group.add("0");
        } else {
            group.add(sb.toString());
        }
        sb.setLength(0);
        // 做乘除运算
        char multiTemp = 'a', plusTemp = 'a';
        int fi = 0;
        for (int i = 0; i < group.size(); i++) {
            String s = group.get(i);
            int result = 0;
            // 每组处理
            char[] gs = s.toCharArray();
            for (char g : gs) {
                if (g == '*' || g == '/') {
                    int t = Integer.parseInt(sb.toString());
                    sb.setLength(0);
                    if (multiTemp == '*') {
                        result *= t;
                    } else if (multiTemp == '/') {
                        result /= t;
                    } else {
                        result = t;
                    }
                    multiTemp = g;
                } else {
                    sb.append(g);
                }
            }
            // 尾部处理
            if (sb.length() > 0) {
                int t = Integer.parseInt(sb.toString());
                if (multiTemp == '*') {
                    result *= t;
                } else if (multiTemp == '/') {
                    result /= t;
                } else {
                    result = t;
                }
                sb.setLength(0);
            }
            // 重置
            multiTemp = 'a';
            // 做加减运算
            if (plusTemp == '+') {
                fi += result;
            } else if (plusTemp == '-') {
                fi -= result;
            } else if (plusTemp != 'e') {
                fi = result;
            }
            if (i < am.size()) {
                plusTemp = am.get(i);
            } else {
                plusTemp = 'e';
            }
        }
        return fi;
    }
}
