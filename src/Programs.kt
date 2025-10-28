/** Program from the problem statement.
 */
val simpleProgram: Stmt = Stmt.Block(
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

/** Simple program, that uses while and sums numbers from 10 to 1.
 */
val whileProgram: Stmt = Stmt.Block(
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

/** A program that computes gcd of variables a and b.
 */
val gcd : Stmt = Stmt.Block(
    Stmt.While(Expr.NEq(Expr.Var("a"), Expr.Var("b")),
        Stmt.If(Expr.Lt(Expr.Var("a"), Expr.Var("b")),
            Stmt.Assign(Expr.Var("b"),
                (Expr.Minus(Expr.Var("b"), Expr.Var("a")))),
            Stmt.Assign(Expr.Var("a"),
                Expr.Minus(Expr.Var("a"), Expr.Var("b"))),),
    ),
    Stmt.Return(Expr.Var("a"))
)