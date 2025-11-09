package ru.otus.gradle.constgenerator

import com.squareup.kotlinpoet.CodeBlock
import org.gradle.api.Named
import org.gradle.api.provider.Property
import kotlin.reflect.KClass

/**
 * Constant definition
 */
interface ConstDefinition : Named {
    /**
     * Const type
     */
    val type: Property<ConstType>

    /**
     * Const value
     */
    val value: Property<String>
}

/**
 * Constant type
 */
enum class ConstType(
    internal val klass: KClass<*>,
    internal val out: (String) -> CodeBlock = { CodeBlock.of(it) }
) {
    BOOL(Boolean::class),
    INT(Int::class),
    LONG(Long::class),
    FLOAT(Float::class),
    DOUBLE(Double::class),
    STRING(String::class, { CodeBlock.of("%S", it) });

    operator fun component1(): KClass<*> = klass
    operator fun component2(): (String) -> CodeBlock = out
}
