sealed interface Stmt {
    class Block(vararg val stmt: Stmt) : Stmt
    class Assign(val variable: Expr.Var, val value: Expr) : Stmt
    class If(
        val cond: Expr,
        val thenStmt: Stmt,
        val elseStmt: Stmt? = null
    ) : Stmt
    class Return(val result: Expr) : Stmt
    class While(val cond: Expr, val body: Stmt) : Stmt
}