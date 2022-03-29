import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

class SimpleAssemblerTest {

    @Test
    public void simple_1() {
        String[] program = new String[]{"mov a 5","inc a","dec a","dec a","jnz a -1","inc a"};
        Map<String, Integer> out = new HashMap<>();
        out.put("a", 1);
        assertEquals(out, SimpleAssembler.interpret(program));
    }

    @Test
    public void simple_2() {
        String[] program = new String[]{"mov a -10","mov b a","inc a","dec b","jnz a -2"};
        Map<String, Integer> out = new HashMap<>();
        out.put("a", 0);
        out.put("b", -20);
        assertEquals(out, SimpleAssembler.interpret(program));
    }
/* Submit tests:
0: mov a 1
1: mov b 1
2: mov c 0
3: mov d 26
4: jnz c 2
5: jnz 1 5
10: mov c a
11: inc a
12: dec b
13: jnz b -2
14: mov b c
15: dec d
16: jnz d -6
17: mov c 18
18: mov d 11
19: inc a
20: dec d
21: jnz d -2
22: dec c
23: jnz c -5
{a=318009, b=196418, c=0, d=0}

0: mov d 100
1: dec d
2: mov b d
3: jnz b -2
4: inc d
5: mov a d
6: jnz 5 10
{a=1, b=0, d=1}

0: mov c 12
1: mov b 0
2: mov a 200
3: dec a
4: inc b
5: jnz a -2
6: dec c
7: mov a b
8: jnz c -5
9: jnz 0 1
10: mov c a
{a=409600, b=409600, c=409600}

0: mov a 5
1: inc a
2: dec a
3: dec a
4: jnz a -1
5: inc a
{a=1}

0: mov a -10
1: mov b a
2: inc a
3: dec b
4: jnz a -2
{a=0, b=-20}
 */
    @Test
    public void complex() {
        String[] program = new String[]{
                "mov a 1",
                "mov b 1",
                "mov c 0",
                "mov d 26",
                "jnz c 2",
                "jnz 1 5",
                "",
                "",
                "",
                "",
                "mov c a",
                "inc a",
                "dec b",
                "jnz b -2",
                "mov b c",
                "dec d",
                "jnz d -6",
                "mov c 18",
                "mov d 11",
                "inc a",
                "dec d",
                "jnz d -2",
                "dec c",
                "jnz c -5",
                };
        Map<String, Integer> out = new HashMap<>();
        out.put("a", 318009);
        out.put("b", 196418);
        out.put("c", 0);
        out.put("d", 0);
        assertEquals(out, SimpleAssembler.interpret(program));
    }


    @Test
    public void complex1() {
        String[] program = new String[]{
                "mov d 100",
                "dec d",
                "mov b d",
                "jnz b -2",
                "inc d",
                "mov a d",
                "jnz 5 10",
        };
        Map<String, Integer> out = new HashMap<>();
        out.put("a", 1);
        out.put("b", 0);
        out.put("d", 1);
        assertEquals(out, SimpleAssembler.interpret(program));
    }

    @Test
    public void complex2() {
        String[] program = new String[]{
                "mov c 12",
                "mov b 0",
                "mov a 200",
                "dec a",
                "inc b",
                "jnz a -2",
                "dec c",
                "mov a b",
                "jnz c -5",
                "jnz 0 1",
                "mov c a",
        };
        Map<String, Integer> out = new HashMap<>();
        out.put("a", 409600);
        out.put("b", 409600);
        out.put("c", 409600);
        assertEquals(out, SimpleAssembler.interpret(program));
    }

    @Test
    public void complex3() {
        String[] program = new String[]{
                "mov a 5",
                "inc a",
                "dec a",
                "dec a",
                "jnz a -1",
                "inc a",
        };
        Map<String, Integer> out = new HashMap<>();
        out.put("a", 1);
        assertEquals(out, SimpleAssembler.interpret(program));
    }

    @Test
    public void complex4() {
        String[] program = new String[]{
                "mov a -10",
                "mov b a",
                "inc a",
                "dec b",
                "jnz a -2",
        };
        Map<String, Integer> out = new HashMap<>();
        out.put("a", 0);
        out.put("b", -20);
        assertEquals(out, SimpleAssembler.interpret(program));
    }

}