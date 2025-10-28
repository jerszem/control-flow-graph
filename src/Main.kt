fun main() {
    val progs : Array<Stmt> = arrayOf(simpleProgram, gcd, whileProgram)
    
    for (prog in progs) {
        val controlFlow = createControlFlowGraph(prog)
        controlFlow.prettyPrint()
        println()
    }
}