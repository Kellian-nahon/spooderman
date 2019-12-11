package com.epita.spooderman.vectorisation


class Vectorizer {

    fun Vectorize(words: List<String>): HashMap<String, Pair<Float, List<Int>>> {
        var dict = HashMap<String, Pair<Float, List<Int>>>()
        words.forEach { word ->
            if(!dict.containsKey(word)) {
                dict[word] = getNbOccurencesAndPositionInList(words, word)
            }

        }
        return dict
    }

    private fun getNbOccurencesAndPositionInList(words: List<String>, word:String): Pair<Float, List<Int>> {

        var nbOccurrences = 0
        var positions = ArrayList<Int>()
        for (i in 0 until words.count()) {
            if (words[i] == word) {
                nbOccurrences += 1
                positions.add(i)
            }
        }
        return Pair(nbOccurrences.div(words.count().toFloat()), positions)
    }

}