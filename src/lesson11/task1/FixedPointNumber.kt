@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import lesson3.task1.length
import java.lang.Math.pow
import kotlin.math.pow

/**
 * Класс "вещественное число с фиксированной точкой"
 *
 * Общая сложность задания - сложная, общая ценность в баллах -- 20.
 * Объект класса - вещественное число с заданным числом десятичных цифр после запятой (precision, точность).
 * Например, для ограничения в три знака это может быть число 1.234 или -987654.321.
 * Числа можно складывать, вычитать, умножать, делить
 * (при этом точность результата выбирается как наибольшая точность аргументов),
 * а также сравнить на равенство и больше/меньше, преобразовывать в строку и тип Double.
 *
 * Вы можете сами выбрать, как хранить число в памяти
 * (в виде строки, целого числа, двух целых чисел и т.д.).
 * Представление числа должно позволять хранить числа с общим числом десятичных цифр не менее 9.
 */
class FixedPointNumber(val int: Int, val frac: String) : Comparable<FixedPointNumber> {
    /**
     * Точность - число десятичных цифр после запятой.
     */
    val precision: Int get() = frac.length

    /**
     * Конструктор из строки, точность выбирается в соответствии
     * с числом цифр после десятичной точки.
     * Если строка некорректна или цифр слишком много,
     * бросить NumberFormatException.
     *
     * Внимание: этот или другой конструктор можно сделать основным
     */
    constructor(s: String) : this(s.split(".")[0].toInt(), s.toString().split(".")[1])

    /**
     * Конструктор из вещественного числа с заданной точностью
     */
    constructor(d: Double, p: Int) : this(d.toString().split(".")[0].toInt(), d.toString().split(".")[1].substring(0, p))

    /**
     * Конструктор из целого числа (предполагает нулевую точность)
     */
    constructor(i: Int) : this(i, "")

    /**
     * Сложение.
     *
     * Здесь и в других бинарных операциях
     * точность результата выбирается как наибольшая точность аргументов.
     * Лишние знаки отрбрасываются, число округляется по правилам арифметики.
     */
    operator fun plus(other: FixedPointNumber): FixedPointNumber {
        var newInt = int + other.int
        var newFrac = ""
        var frac1 = if (frac.isNotEmpty()) frac.toInt() else 0
        var frac2 = if (other.frac.isNotEmpty()) other.frac.toInt() else 0
        val mainPrecision = precision.coerceAtLeast(other.precision)
        when {
            precision > other.precision -> frac2 *= 10.0.pow(precision - other.precision).toInt()
            other.precision > precision -> frac1 *= 10.0.pow(other.precision - precision).toInt()
        }
        newFrac = (frac1 + frac2).toString()
        if (newFrac.length < mainPrecision) {
            newFrac = "0".repeat(mainPrecision - newFrac.length) + newFrac
        } else if (newFrac.length > mainPrecision) {
            newInt += newFrac[0].digitToInt()
            newFrac = newFrac.replaceFirst("1", "")
        }
        return FixedPointNumber(newInt, newFrac)
    }

    /**
     * Смена знака
     */
    operator fun unaryMinus(): FixedPointNumber = FixedPointNumber(-int, frac)

    /**
     * Вычитание
     */
    operator fun minus(other: FixedPointNumber): FixedPointNumber {
        var newInt = int - other.int
        var newFracInt = 0
        var newFrac = ""
        var frac1 = if (frac.isNotEmpty()) frac.toInt() else 0
        var frac2 = if (other.frac.isNotEmpty()) other.frac.toInt() else 0
        val mainPrecision = precision.coerceAtLeast(other.precision)
        when {
            precision > other.precision -> frac2 *= 10.0.pow(precision - other.precision).toInt()
            other.precision > precision -> frac1 *= 10.0.pow(other.precision - precision).toInt()
        }
        newFracInt = frac1 - frac2
        if (newInt < 0) {
            if (newFracInt > 0) {
                newFrac = (10.0.pow(mainPrecision).toInt() - newFracInt).toString()
                newInt++
            } else {
                newFrac = (-newFracInt).toString()
            }
        } else {
            if (newFracInt < 0) {
                newFrac = (10.0.pow(mainPrecision).toInt() + newFracInt).toString()
                newInt--
            } else {
                newFrac = newFracInt.toString()
            }
        }
        if (newFrac.length < mainPrecision) {
            newFrac = "0".repeat(mainPrecision - newFrac.length) + newFrac
        }
        return FixedPointNumber(newInt, newFrac)
    }

    /**
     * Умножение
     */
    operator fun times(other: FixedPointNumber): FixedPointNumber = TODO()

    /**
     * Деление
     */
    operator fun div(other: FixedPointNumber): FixedPointNumber = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean {
        return if (other is FixedPointNumber) {
            int == other.int && frac == other.frac
        } else {
            false
        }
    }

    /**
     * Сравнение на больше/меньше
     */
    override fun compareTo(other: FixedPointNumber): Int {
        return if (int == other.int) {
            when {
                "0.$frac".toDouble() == "0.${other.frac}".toDouble() -> 0
                "0.$frac".toDouble() > "0.${other.frac}".toDouble() -> 1
                else -> -1
            }
        } else {
            when {
                int > other.int -> 1
                else -> -1
            }
        }
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String = "$int" + if (frac.isNotEmpty()) ".$frac" else ""

    /**
     * Преобразование к вещественному числу
     */
    fun toDouble(): Double = "$int.$frac".toDouble()

    /**
     * Хэш-код
     */
    override fun hashCode(): Int {
        var result = int
        result = 31 * result + frac.hashCode()
        return result
    }
}