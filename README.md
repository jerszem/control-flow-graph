## Project description
Project is an implementation of a problem crated by Komi Golov, Ilya Chernikov for jetbrains internship (https://internship.jetbrains.com/projects/1637).
It transforms abstract syntax tree of predefined statemets and expressions into control flow graph.

My implementation is inspired by continuations, which are a way to recursively construct control flow graph from the end of the program. 

## AST syntax
- Statements
  - blocks
  - assignments
  - if (else)
  - while
  - return
- Expressions
  - constants
  - variables
  - (in)equality
  - addition
  - subtraction
  - multiplication

## Sample results
- Sample program with AST:
```
Stmt.Block(
    Stmt.Assign(Expr.Var("x"), Expr.Const(0)),
    Stmt.If(
        Expr.Var("a"),
        Stmt.Assign(Expr.Var("y"), Expr.Const(5)),
        Stmt.Assign(Expr.Var("y"), Expr.Const(1))
    ),
    Stmt.Return(
        Expr.Plus(
            Expr.Mul(Expr.Var("x"), Expr.Const(2)),
            Expr.Var("y")
        )
    )
)
```
can be turned into a flowchart in Mermaid:
```mermaid
flowchart TD
    NodeAssign65b3120a["x := 0"] --> NodeCondition7106e68e
	NodeCondition7106e68e{"if a"} -- then --> NodeAssign7eda2dbb
	NodeCondition7106e68e -- else --> NodeAssign6576fe71
	NodeAssign7eda2dbb["y := 5"] --> NodeReturn76fb509a
	NodeReturn76fb509a("return (x * 2 + y)")
	NodeAssign6576fe71["y := 1"] --> NodeReturn76fb509a
```

- Program computing _gcd_ of variables `a` and `b`
```
Stmt.Block(
    Stmt.While(Expr.NEq(Expr.Var("a"), Expr.Var("b")),
        Stmt.If(Expr.Lt(Expr.Var("a"), Expr.Var("b")),
            Stmt.Assign(Expr.Var("b"),
                (Expr.Minus(Expr.Var("b"), Expr.Var("a")))),
            Stmt.Assign(Expr.Var("a"),
                Expr.Minus(Expr.Var("a"), Expr.Var("b"))),),
    ),
    Stmt.Return(Expr.Var("a"))
)
```
results in following flowchart:
```mermaid
flowchart TD
	NodeAssign433c675d["y := 0"] --> NodeAssign3f91beef
	NodeAssign3f91beef["x := 10"] --> NodeWhile1a6c5a9e
	NodeWhile1a6c5a9e{"while 0 < x"} -- do --> NodeAssign37bba400
	NodeAssign37bba400["y := (x + y)"] --> NodeWhile1a6c5a9e
	NodeWhile1a6c5a9e -- after --> NodeReturn179d3b25
	NodeReturn179d3b25("return y")
```

- A program that uses `while` to compute sum of numbers from 10 to 1 written below
```
Stmt.Block(
    Stmt.Assign(Expr.Var("y"), Expr.Const(0)),
    Stmt.Assign(Expr.Var("x"), Expr.Const(10)),
    Stmt.While(Expr.Lt(Expr.Const(0), Expr.Var("x")),
        Stmt.Assign(
            Expr.Var("y"),
            Expr.Plus(
                Expr.Var("x"),
                Expr.Var("y")
            )
        )
    ),
    Stmt.Return(Expr.Var("y"))
)
```
as a result gives the following graph:
```mermaid
flowchart TD
	NodeWhile1f17ae12{"while a != b"} -- do --> NodeCondition4d405ef7
	NodeCondition4d405ef7{"if a < b"} -- then --> NodeAssign6193b845
	NodeCondition4d405ef7 -- else --> NodeAssign2e817b38
	NodeAssign6193b845["b := (b - a)"] --> NodeWhile1f17ae12
	NodeAssign2e817b38["a := (a - b)"] --> NodeWhile1f17ae12
	NodeWhile1f17ae12 -- after --> NodeReturnc4437c4
	NodeReturnc4437c4("return a")
```

## Files
- `Expr.kt` and `Stmt.kt` contain definitions of respective interfaces.
- `Programs.kt` defines a list of sample programs.
- `Node.kt` defines `Node` interface and logic of creations of CF diagrams as well as prettyPrinter for Mermaid flowcharts.
- `Main.kt` is a sample program that prints to console CF diagrams for sample programs.