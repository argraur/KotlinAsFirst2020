@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.lang.IllegalArgumentException
import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {
    private val coeffArray = coeffs.asList().reversed()

    /**
     * Геттер: вернуть значение коэффициента при x^i,
     * если такой степени нет, throw IllegalArgumentException
     */
    fun coeff(i: Int): Double {
        if (i >= coeffArray.size) {
            throw IllegalArgumentException()
        }
        return coeffArray[i]
    }

    /**
     * Геттер: вернуть значение коэффициента при x^i
     * если такой степени нет, вернуть ноль
     */
    fun coeffOrNull(i: Int): Double {
        if (i >= coeffArray.size) {
            return 0.0
        }
        return coeffArray[i]
    }

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var result = 0.0
        for (i in coeffArray.indices) {
            result += coeffArray[i] * x.pow(i.toDouble())
        }
        return result
    }

    /**
     * Геттер: Вернуть все коэфициенты
     */
    fun getCoeffsList(x: Double): List<Double> {
        return coeffArray
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int {
        for (i in coeffArray.size - 1 downTo 0) {
            if (coeffArray[i] != 0.0) {
                return i
            }
        }
        return 0
    }

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val newCoeffs = mutableListOf<Double>()
        val maxDegree = this.degree().coerceAtLeast(other.degree())

        for (i in maxDegree downTo 0) {
            val coeff = this.coeffOrNull(i) + other.coeffOrNull(i)
            newCoeffs.add(coeff)
        }
        return Polynom(*newCoeffs.toDoubleArray())
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        val newCoeffs = mutableListOf<Double>()

        for (coeff in coeffArray) {
            newCoeffs.add(-coeff)
        }
        return Polynom(*newCoeffs.reversed().toDoubleArray())
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom {
        val newCoeffs = mutableListOf<Double>()
        val maxDegree = this.degree().coerceAtLeast(other.degree())

        for (i in maxDegree downTo 0) {
            val coeff = this.coeffOrNull(i) - other.coeffOrNull(i)
            newCoeffs.add(coeff)
        }
        return Polynom(*newCoeffs.toDoubleArray())
    }

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val maxDegree = this.degree() * other.degree()
        val newCoeffs = mutableListOf<Double>()

        for (i in 0 until maxDegree) {
            newCoeffs.add(0.0)
        }

        val thisReversed = this.coeffArray.reversed()
        val otherReversed = other.coeffArray.reversed()

        for (x in thisReversed.indices) {
            for (y in otherReversed.indices) {
                newCoeffs[x + y] += thisReversed[x] * otherReversed[y]
            }
        }

        return Polynom(*newCoeffs.toDoubleArray())
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom = TODO()

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = TODO()

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = TODO()
}
