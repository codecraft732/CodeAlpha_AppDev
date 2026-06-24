package com.example.randomquotegenerator.data

import com.example.randomquotegenerator.R

data class Quote(
    val id: Int,
    val text: String,
    val author: String,
    val category: String
)

data class Category(
    val name: String,
    val imageRes: Int
)

object QuoteRepository {
    private val quotes = listOf(
        Quote(1, "The only way to make sense out of change is to plunge into it, move with it, and join the dance.", "Alan Watts", "Wisdom"),
        Quote(2, "The only way to deal with an unfree world is to become so absolutely free that your very existence is an act of rebellion.", "Albert Camus", "Philosophy"),
        Quote(3, "Complexity is the enemy of execution.", "Tony Robbins", "Motivation"),
        Quote(4, "He who has a why to live can bear almost any how.", "Friedrich Nietzsche", "Philosophy"),
        Quote(5, "To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment.", "Ralph Waldo Emerson", "Wisdom"),
        Quote(6, "In the midst of winter, I found there was, within me, an invincible summer.", "Albert Camus", "Solitude"),
        Quote(7, "Nature does not hurry, yet everything is accomplished.", "Lao Tzu", "Nature"),
        Quote(8, "Look deep into nature, and then you will understand everything better.", "Albert Einstein", "Nature")
    )

    fun getRandomQuote(): Quote = quotes.random()

    fun getQuotesByCategory(category: String): List<Quote> = 
        quotes.filter { it.category.equals(category, ignoreCase = true) }

    fun getAllQuotes(): List<Quote> = quotes

    val categories = listOf(
        Category("Wisdom", R.drawable.wisdom),
        Category("Love", R.drawable.love),
        Category("Philosophy", R.drawable.philosophy),
        Category("Motivation", R.drawable.motivation),
        Category("Nature", R.drawable.nature),
        Category("Solitude", R.drawable.solitude)
    )
}
