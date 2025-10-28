sealed interface Expr {
    class Const(val value: Int) : Expr {
        override fun toString() = "$value"
    }

    class Var(val name: String) : Expr {
        override fun toString() = name
    }

    class Eq(val left: Expr, val right: Expr) : Expr {
        override fun toString() = "$left == $right"
    }

    class NEq(val left: Expr, val right: Expr) : Expr {
        override fun toString() = "$left != $right"
    }

    class Lt(val left: Expr, val right: Expr) : Expr {
        override fun toString() = "$left < $right"
    }

    class Plus(val left: Expr, val right: Expr) : Expr {
        override fun toString() = "($left + $right)"
    }

    class Minus(val left: Expr, val right: Expr) : Expr {
        override fun toString() = "($left - $right)"
    }

    class Mul(val left: Expr, val right: Expr) : Expr {
        override fun toString() = "$left * $right"
    }
}