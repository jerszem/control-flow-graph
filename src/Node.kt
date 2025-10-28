sealed interface Node {
    class Assign(
        val variable: Expr.Var, val value: Expr, val next: Node
    ) : Node

    class Return(val result: Expr) : Node
    class Condition(
        val cond: Expr, val nextIfTrue: Node, val nextIfFalse: Node
    ) : Node

    object Quit : Node
    class While(val cond: Expr, var body: Node, val next: Node) : Node

    /**
     * Prints to console a Mermaid flowchart starting from this node.
     * Is a wrapper of prettyPrintHelper function.
     * @see prettyPrintHelper
     */
    fun prettyPrint() {
        println("flowchart TD")
        val printedNodes : MutableSet<Node> = mutableSetOf()
        prettyPrintHelper(this, printedNodes)
    }

    /**
     * Changes Node's id to alphanumeric to use it as node id in Mermaid.
     */
    fun toAlphanumeric(): String {
        val re = Regex("[^A-Za-z0-9 ]")
        return re.replace(this.toString(), "")
    }
}

/**
 * It creates a control flow node from a node
 * @param nextNode the node, that program goes to after executing stmt
 * @param stmt an instruction, that is used to build a node
 */

// The implementation is inspired by continuation sematic.
fun createNode(stmt: Stmt, nextNode: Node): Node {
    when (stmt) {

        // As in continuations, we build the control flow graph from the end.
        is Stmt.Block -> {
            if (stmt.stmt.size == 1) {
                return createNode(stmt.stmt[0], nextNode)
            } else {
                val withoutLast = stmt.stmt.copyOfRange(0, stmt.stmt.size - 1)
                return createNode(
                    Stmt.Block(*withoutLast), createNode(stmt.stmt.last(), nextNode)
                )
            }
        }

        is Stmt.Assign -> {
            return Node.Assign(stmt.variable, stmt.value, nextNode)
        }

        is Stmt.Return -> {
            return Node.Return(stmt.result)
        }

        is Stmt.If -> {
            return when (stmt.elseStmt) {
                null -> Node.Condition(
                    stmt.cond, createNode(stmt.thenStmt, nextNode), nextNode
                )

                else -> {
                    Node.Condition(
                        stmt.cond, createNode(stmt.thenStmt, nextNode),
                        createNode(stmt.elseStmt, nextNode)
                    )
                }
            }
        }

        is Stmt.While -> {
//            We need to crate the while node with temp body, to create the body,
//            because it needs to go back to node after finishing execution of body.
            val node = Node.While(stmt.cond, Node.Quit, nextNode)
            val tempNode = Node.While(stmt.cond, createNode(stmt.body, node), nextNode)
            node.body = tempNode.body
            return node
        }
    }
}

/**
 * Constructs a control flow graph
 * It is a wrapper for recursive execution of createNode function
 * @see createNode
 */
fun createControlFlowGraph(prog: Stmt): Node {
    val quitNode = Node.Quit
    return createNode(prog, quitNode)
}

/**
 * A function that recursively prints to console Mermaid flowchart representation
 * of the control flow graph.
 * @param node starting node of the flowchart
 * @param printedNodes a set of already printed nodes
 */
fun prettyPrintHelper(node: Node, printedNodes : MutableSet<Node>) {
    if (printedNodes.contains(node)) {
        return
    } else {
        printedNodes.add(node)
    }

    print("\t")
    when (node) {
        is Node.Assign -> {
            println(
                "${(node).toAlphanumeric()}[\"${node.variable.name} := ${(node.value)}\"] "
                        + "--> ${(node.next).toAlphanumeric()}"
            )
            prettyPrintHelper(node.next, printedNodes)
        }

        is Node.Condition -> {
            println(
                "${(node).toAlphanumeric()}{\"if ${(node.cond)}\"} "
                        + "-- then --> ${(node.nextIfTrue).toAlphanumeric()}"
            )
            println("\t${(node).toAlphanumeric()} -- else --> ${(node.nextIfFalse).toAlphanumeric()}")
            prettyPrintHelper(node.nextIfTrue, printedNodes)
            prettyPrintHelper(node.nextIfFalse, printedNodes)
        }

        is Node.Quit -> {
            println("${(node).toAlphanumeric()}(quit)")
        }

        is Node.Return -> {
            println("${(node).toAlphanumeric()}(\"return ${(node.result)}\")")
        }

        is Node.While -> {
            println("${(node).toAlphanumeric()}{\"while ${(node.cond)}\"} " +
                    "-- do --> ${(node.body).toAlphanumeric()}")
            prettyPrintHelper(node.body, printedNodes)
            println("\t${(node).toAlphanumeric()} -- after --> ${(node.next).toAlphanumeric()}")
            prettyPrintHelper(node.next, printedNodes)
        }
    }
}