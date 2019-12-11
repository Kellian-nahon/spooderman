package com.epita.spooderman.tokenisation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class Tokenizer {

    private var synonymHashMap: HashMap<String, String>? = null
    private var delimiters: List<String>
    private var stopWordsList: List<String>
    private var suffixList: List<String>


    init {
        synonymHashMap = HashMap()
        this.javaClass.getResourceAsStream("/synonyms_en.txt").bufferedReader().forEachLine {line ->
            val wordList: List<String> = line.split(", ")
                .map { word -> word.removeSuffix(" ")
                    word.removePrefix(" ")}
            wordList.forEach {word ->
                if (wordList.count() != 0 && !synonymHashMap!!.contains(word)) {
                    synonymHashMap!![word] = wordList[0]
                }
            }
        }

        val mapper = jacksonObjectMapper()
        val jsonStopWordsString = this.javaClass.getResource("/stop_words_en.json").readText()
        stopWordsList = mapper.readValue(jsonStopWordsString)
        val jsonDelimitersString = this.javaClass.getResource("/punctuation_delimiters_en.json").readText()
        delimiters = mapper.readValue(jsonDelimitersString)
        val jsonSuffixListString = this.javaClass.getResource("/suffix_en.json").readText()
        suffixList = mapper.readValue(jsonSuffixListString)
    }



    private fun stemming(word: String): String{
        suffixList.forEach { suffix ->
            if (word.endsWith(suffix, true))
                return word.removeSuffix(suffix)
        }
        return word
    }

    private fun changeToSynonyme(word: String): String {
        synonymHashMap!![word]?.let {
            if (word == stemming(it)) {
                return stemming(it)
            }
        }
        return word
    }

    fun tokenisation(text: String): List<String> {
        var doc = text.toLowerCase()
            .split(*delimiters.toTypedArray())
            .filter { (it != "") }
            .filter { !stopWordsList.contains(it.toLowerCase()) }
            .map { stemming(it) }
            .map { changeToSynonyme(it) }
        return doc
    }

}