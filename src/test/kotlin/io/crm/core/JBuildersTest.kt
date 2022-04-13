package io.crm.core

import io.crm.core.kotlin.jbuilders.rcrm
import io.smallrye.mutiny.Uni
import org.junit.jupiter.api.Test

/**
 * Created by xiongxl in 2022/4/13
 */
class JBuildersTest {
    data class MyValue(val id: Int = 0, val name: String = "name")

    @Test
    fun testJBuilders() {
        val rc = rcrm<String, MyValue> {
            tier {
                reader {
                    Uni.createFrom().item(MyValue(1, "n1"))
                }
            }
            tier {
                reader { Uni.createFrom().item(MyValue(2, "n2")) }
            }
        }
        val value = rc.read("hi").await().indefinitely()
        println("value is $value")
    }
}