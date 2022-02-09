@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import ru.spbstu.wheels.defaultCopy
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

    private val coeffArray: List<Double>

    /**
     * Игнорируем старшие коэффициенты, равные нулю и приводим аргументы к виду List<Double>
     */

    init {
        val list = coeffs.toMutableList()
        while (list[0] == 0.0) {
            if (list.size != 1)
                list.removeFirstOrNull()
            else
                break
        }
        this.coeffArray = list.reversed()
    }

    /**
     * Методы для функции деления и взятия остатка
     */

    private fun polyDegree(p: DoubleArray): Int {
        for (i in p.size - 1 downTo 0) {
            if (p[i] != 0.0) return i
        }
        return 0
    }

    /**
     * Сдвиг вправо на *places* символов
     */

    private fun shiftRight(p: DoubleArray, places: Int = 1, md: Int): DoubleArray {
        if (places <= 0) return p
        val pd = polyDegree(p)
        if (pd + places >= p.size) {
            return p
        }
        val d = p.copyOf()
        for (i in pd downTo 0) {
            d[i + places] = d[i]
            d[i] = 0.0
        }
        return d
    }

    /**
     * Умножить весь массив на число
     */

    private fun mulitpyCoeffs(list: DoubleArray, i: Double = 1.0) {
        for (x in list.indices) list[x] *= i
    }

    /**
     * Поэлементное вычитание
     */

    private fun substractCoeffs(list: DoubleArray, other: DoubleArray) {
        for (x in list.indices) list[x] -= other[x]
    }

    /**
     * привести входной массив делителя к форме делимого
     * Пример: [-3, 1] -> [-3, 1, 0, 0], если максимальная степень делимого x^3
     */

    private fun normalize(x: List<Double>, y: List<Double>): DoubleArray {
        val res = DoubleArray(x.size)
        for (i in y.indices) {
            res[i] = y[i]
        }
        return res
    }


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
        val maxDegree = this.degree() + other.degree()
        val newCoeffs = DoubleArray(maxDegree + 1)

        val thisReversed = this.coeffArray.reversed()
        val otherReversed = other.coeffArray.reversed()

        for (x in thisReversed.indices) {
            for (y in otherReversed.indices) {
                newCoeffs[x + y] += thisReversed[x] * otherReversed[y]
            }
        }

        return Polynom(*newCoeffs)
    }

    private fun division(other: Polynom, returnRem: Boolean? = false): Polynom {
        var thisDegree = this.degree()
        var otherDegree = other.degree()

        val thisCoeffs = this.coeffArray.toDoubleArray()
        val otherCoeffs = normalize(this.coeffArray, other.coeffArray)

        if (thisDegree > otherDegree) {
            val newCoeffs = DoubleArray(thisCoeffs.size)
            while (thisDegree >= otherDegree) {
                val t = shiftRight(otherCoeffs, thisDegree - otherDegree, thisCoeffs.size)
                newCoeffs[thisDegree - otherDegree] += thisCoeffs[thisDegree] / t[thisDegree]
                mulitpyCoeffs(t, newCoeffs[thisDegree - otherDegree])
                substractCoeffs(thisCoeffs, t)
                thisDegree = polyDegree(thisCoeffs)
            }
            println(thisCoeffs.joinToString(separator = ","))
            if (returnRem == true) {
                return Polynom(*thisCoeffs.reversedArray())
            }
            return Polynom(*newCoeffs.reversedArray())
        }
        return Polynom(0.0)
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */

    operator fun div(other: Polynom) = division(other, false)

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = division(other, true)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Polynom

        if (coeffArray.hashCode() != other.coeffArray.hashCode()) return false

        return true
    }

    override fun hashCode(): Int = coeffArray.hashCode()

}
