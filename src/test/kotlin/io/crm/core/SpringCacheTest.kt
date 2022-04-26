package io.crm.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by xiongxl in 2022/4/26
 */
@SpringBootApplication
class SpringCacheTest(private val bookService: BookService) : CommandLineRunner {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SpringCacheTest::class.java)
    }

    override fun run(vararg args: String?) {
        (0 .. 5).forEach { _ ->
            val isbn = "isbn-${ThreadLocalRandom.current().nextInt(100)}"
            logger.info("$isbn --> ${bookService.getByIsbn(isbn)}")
        }
    }

}

data class Book(val isbn: String, val title: String)

interface BookService {
    fun getByIsbn(isbn: String): Book?
}

@Service
class CacheableBookService : BookService {
    override fun getByIsbn(isbn: String): Book? {
        simulateSlowService()
        return Book(isbn, "Book-title-$isbn")
    }

    // Don't do this at home
    private fun simulateSlowService() {
        Thread.sleep(2000L)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringCacheTest>(*args)
}
