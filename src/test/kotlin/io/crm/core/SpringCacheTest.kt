package io.crm.core

import io.crm.core.kotlin.jbuilders.rcrm
import io.crm.core.kotlin.jbuilders.wcrm
import io.smallrye.mutiny.Uni
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.annotation.ProxyCachingConfiguration
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.interceptor.CacheInterceptor
import org.springframework.cache.interceptor.CacheOperationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by xiongxl in 2022/4/26
 */
@SpringBootApplication
@EnableCaching
class SpringCacheTest(private val bookService: BookService) : CommandLineRunner {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SpringCacheTest::class.java)
    }

    @Bean
    fun cacheManager(): CacheManager {
//        return ConcurrentMapCacheManager()
        return CaffeineCacheManager()
    }

    override fun run(vararg args: String?) {
//        testCache()
        testCrm()
    }

    private fun testCrm() {
        val rc = rcrm<String, Book> {
            tier {
                reader {
                    Uni.createFrom().item(bookService.getByIsbn(it))
                }
            }
        }
        val wc = wcrm<String, Book> {
            tier {
                writer { _, book ->
                    Uni.createFrom().item(bookService.updateCache(book))
                }
            }
        }
        (0 .. 5).forEach { i ->
            val postfix = if (i % 3 == 0) {
                ThreadLocalRandom.current().nextInt(100)
            } else {
                42
            }
            val isbn = "isbn-$postfix"
            logger.info("r $isbn --> ${rc.read(isbn).await().indefinitely()}")
            logger.info("w $isbn --> ${wc.write(isbn, Book(isbn, "Book-$isbn")).await().indefinitely()}")
            logger.info("r $isbn --> ${rc.read(isbn).await().indefinitely()}")
        }
    }

    private fun testCache() {
        (0 .. 5).forEach { i ->
            val postfix = if (i % 3 == 0) {
                ThreadLocalRandom.current().nextInt(100)
            } else {
                42
            }
            val isbn = "isbn-$postfix"
            logger.info("$isbn --> ${bookService.getByIsbn(isbn)}")
        }
    }

}

data class Book(val isbn: String, val title: String)

interface BookService {
    fun getByIsbn(isbn: String): Book?
    fun updateCache(book: Book): Book
}

@Service
class CacheableBookService : BookService {
    @Cacheable("books")
    override fun getByIsbn(isbn: String): Book? {
        simulateSlowService()
        return Book(isbn, "Book-title-$isbn")
    }

    @CachePut(value = ["books"], key = "#book.isbn")
    override fun updateCache(book: Book): Book {
        simulateSlowService()
        return book
    }

    // Don't do this at home
    private fun simulateSlowService() {
        Thread.sleep(2000L)
    }
}

@Configuration(proxyBeanMethods = false)
class MyProxyCachingConfiguration : ProxyCachingConfiguration() {
    companion object {
        private val logger = LoggerFactory.getLogger(MyProxyCachingConfiguration::class.java)
    }

    @Bean
    override fun cacheInterceptor(cacheOperationSource: CacheOperationSource): CacheInterceptor {
        val interceptor = CacheInterceptor()
        interceptor.configure(errorHandler, keyGenerator, cacheResolver, cacheManager)
        interceptor.cacheOperationSource = cacheOperationSource
        logger.info("${this::class.simpleName} cacheInterceptor")
        return interceptor
    }
}

fun main(args: Array<String>) {
    runApplication<SpringCacheTest>(*args)
}
