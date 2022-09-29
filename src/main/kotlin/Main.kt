import java.io.File


var totalFileScore = 0

val openBrackets = listOf('(', '[', '{', '<')
val closeBrackets = listOf(')', ']', '}', '>')
fun main(args: Array<String>) {
    File("src/main/resources/input.txt").forEachLine {
        println(it)
        calculateLineIllegalScore(it)
    }
    println("Total file score: $totalFileScore")
}

fun calculateLineIllegalScore(line: String) {
    val lineChars = line.toCharArray().toMutableList()
    for (i in 0 until lineChars.size - 1) {
        if (openBrackets.contains(lineChars[i]) && closeBrackets.contains(lineChars[i + 1])) {
            if (!isOpenThenCloseItemValid(lineChars[i], lineChars[i + 1])) {
                addToIllegalScore(lineChars[i + 1])
                println("Illegal char: ${lineChars[i + 1]}")
                println("Illegal score: $totalFileScore")
            }
            lineChars.removeAt(i)
            lineChars.removeAt(i)
            break
        }
    }
    if (doesContainCloseBracket(lineChars)) {
        calculateLineIllegalScore(lineChars.joinToString(""))
    }
}

fun isOpenThenCloseItemValid(openBracket: Char, closeBracket: Char): Boolean {
    return when (openBracket) {
        '(' -> closeBracket == ')'
        '[' -> closeBracket == ']'
        '{' -> closeBracket == '}'
        '<' -> closeBracket == '>'
        else -> false
    }
}

private fun addToIllegalScore(char: Char) {
    when (char) {
        ')' -> totalFileScore += 3
        ']' -> totalFileScore += 57
        '}' -> totalFileScore += 1197
        '>' -> totalFileScore += 25137
    }
}

fun doesContainCloseBracket(lineChars: MutableList<Char>): Boolean {
    for (i in 0 until lineChars.size) {
        if (closeBrackets.contains(lineChars[i])) {
            return true
        }
    }
    return false
}
