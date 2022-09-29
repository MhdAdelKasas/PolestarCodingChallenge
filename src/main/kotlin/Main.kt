import java.io.File


var totalFileScore = 0
val inCompleteLinesScores: MutableList<Int> = mutableListOf()
val openBrackets = listOf('(', '[', '{', '<')
val closeBrackets = listOf(')', ']', '}', '>')
fun main(args: Array<String>) {
    /*File("src/main/resources/input.txt").forEachLine {
        val oldFileScore = totalFileScore
        println(it)
        calculateLineIllegalScore(it)
        if (oldFileScore == totalFileScore) {
            File("src/main/resources/illegalLines.txt").appendText(it + "\n")
        }
    }
    println("Total file score: $totalFileScore")*/
    File("src/main/resources/exampleInput.txt").forEachLine {
        println(it)
        calculateInCompleteLinesScore(it)
    }
    println("inCompleteLinesScores: $inCompleteLinesScores")
    inCompleteLinesScores.sort()
    println("inCompleteLinesScores: $inCompleteLinesScores")
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

fun calculateInCompleteLinesScore(line: String) {
    val lineWithRemovedRoundBrackets = countAndRemoveBrackets(line, '(', ')')
    val lineWithRemovedSquareBrackets = countAndRemoveBrackets(lineWithRemovedRoundBrackets, '[', ']')
    val lineWithRemovedCurlyBrackets = countAndRemoveBrackets(lineWithRemovedSquareBrackets, '{', '}')
    val lineWithRemovedAngleBrackets = countAndRemoveBrackets(lineWithRemovedCurlyBrackets, '<', '>')

    val lineChar = lineWithRemovedAngleBrackets.toCharArray().toMutableList()
    println(lineWithRemovedAngleBrackets)
    var incompleteLineScore = 0
    for (i in  lineChar.size - 1 downTo 0) {
        when (lineChar[i]) {
            '(' -> incompleteLineScore = (incompleteLineScore * 5) + 1
            '[' -> incompleteLineScore = (incompleteLineScore * 5) + 2
            '{' -> incompleteLineScore = (incompleteLineScore * 5) + 3
            '<' -> incompleteLineScore = (incompleteLineScore * 5) + 4
        }
    }
    inCompleteLinesScores.add(incompleteLineScore)
}

fun countAndRemoveBrackets(line: String, openBracket: Char, closeBracket: Char): String {
    val lineChar = line.toCharArray().toMutableList()
    val openBracketsCount = line.count { it == openBracket }
    val closeBracketsCount = line.count { it == closeBracket }
    if (openBracketsCount == closeBracketsCount) {
        lineChar.removeAll(listOf('<'))
        lineChar.removeAll(listOf('>'))
    } else {
        if (openBracketsCount > 1) {
            for (i in 0 until closeBracketsCount) {
                if (lineChar.indexOfLast { it == closeBracket } > lineChar.indexOfLast { it == openBracket }) {
                    lineChar.removeAt(lineChar.indexOfLast { it == openBracket })
                    lineChar.removeAt(lineChar.indexOfLast { it == closeBracket })
                } else {
                    lineChar.removeAt(lineChar.indexOfFirst { it == closeBracket })
                    lineChar.removeAt(lineChar.indexOfFirst { it == openBracket })
                }
            }
        }
    }
    return lineChar.joinToString("")
}