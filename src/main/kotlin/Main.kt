import java.io.File


var totalFileScore = 0
var incompleteLinesScores: MutableList<Double> = mutableListOf()
val openBrackets = listOf('(', '[', '{', '<')
val closeBrackets = listOf(')', ']', '}', '>')
fun main(args: Array<String>) {
    File("src/main/resources/input.txt").forEachLine {
        val oldFileScore = totalFileScore
        println(it)
        calculateLineIllegalScore(it)
        if (oldFileScore == totalFileScore) {
            File("src/main/resources/incompleteLines.txt").appendText(it + "\n")
        }
    }
    println("Total file score: $totalFileScore")
    File("src/main/resources/incompleteLines.txt").forEachLine {
        println(it)
        calculateInCompleteLinesScore(it)
    }
    incompleteLinesScores.sort()
    println("The Middle Score: ${incompleteLinesScores[incompleteLinesScores.size / 2]}")
}

fun calculateLineIllegalScore(line: String) {
    val lineChars = line.toCharArray().toMutableList()
    for (i in 0 until lineChars.size - 1) {
        if (openBrackets.contains(lineChars[i]) && closeBrackets.contains(lineChars[i + 1])) {
            if (!isOpenThenCloseItemValid(lineChars[i], lineChars[i + 1])) {
                addToIllegalScore(lineChars[i + 1])
                //println("Illegal char: ${lineChars[i + 1]}")
                //println("Illegal score: $totalFileScore")
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
    val lineWithRemovedRoundBrackets = removeSelectedBrackets(line, '(', ')')
    val lineWithRemovedSquareBrackets = removeSelectedBrackets(lineWithRemovedRoundBrackets, '[', ']')
    val lineWithRemovedCurlyBrackets = removeSelectedBrackets(lineWithRemovedSquareBrackets, '{', '}')
    val lineWithRemovedAngleBrackets = removeSelectedBrackets(lineWithRemovedCurlyBrackets, '<', '>')

    println(lineWithRemovedAngleBrackets)

    val lineChar = lineWithRemovedAngleBrackets.toCharArray().toMutableList()
    var incompleteLineScore: Double = 0.0
    for (i in  lineChar.size - 1 downTo 0) {
        when (lineChar[i]) {
            '(' -> incompleteLineScore = (incompleteLineScore * 5) + 1
            '[' -> incompleteLineScore = (incompleteLineScore * 5) + 2
            '{' -> incompleteLineScore = (incompleteLineScore * 5) + 3
            '<' -> incompleteLineScore = (incompleteLineScore * 5) + 4
        }
    }
    incompleteLinesScores.add(incompleteLineScore)
}

fun removeSelectedBrackets(line: String, openBracket: Char, closeBracket: Char): String {
    val lineChar = line.toCharArray().toMutableList()
    val indicesToRemove = mutableListOf<Int>()
    val openBracketsIndices = line.indices.filter { lineChar[it] == openBracket }.toMutableList()
    val closeBracketsIndices = line.indices.filter { lineChar[it] == closeBracket }.toMutableList()
    for (close in closeBracketsIndices) {
        for (i in openBracketsIndices.size - 1 downTo 0) {
            if (openBracketsIndices[i] < close) {
                if (!indicesToRemove.contains(openBracketsIndices[i])) {
                    indicesToRemove.add(openBracketsIndices[i])
                    openBracketsIndices.removeAt(i)
                }
                if (!indicesToRemove.contains(close)) indicesToRemove.add(close)
                break
            }
        }
    }
    indicesToRemove.sortDescending()
    for (index in indicesToRemove) {
        lineChar.removeAt(index)
    }
    return lineChar.joinToString("")
}