@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Фабричный метод для создания комплексного числа из строки вида x+yi
 */
fun Complex(s: String): Complex {
    var str = s
    val isReNegative = str.startsWith("-")
    if (isReNegative) {
        str = str.replaceFirst("-", "")
    }
    when {
        s.contains("+") -> {
            return Complex(((if (isReNegative) "-" else "") + str.split("+")[0]).toDouble(), str.split("+")[1].removeSuffix("i").toDouble())
        }
        s.contains("-") -> {
            return Complex(((if (isReNegative) "-" else "") + str.split("-")[0]).toDouble(), ("-" + str.split("-")[1]).removeSuffix("i").toDouble())
        }
    }
    throw IllegalArgumentException("Wrong input syntax. Syntax should be: x+yi")
}

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(re + other.re, im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-re, -im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex = Complex(re * other.re - im * other.im, re * other.im + im * other.re)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex = Complex(
        (re * other.re + im * other.im) / (other.re * other.re + other.im * other.im),
        (other.re * im - re * other.im) / (other.re * other.re + other.im * other.im)
    )

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean =
        if (other is Complex) {
            re == other.re && im == other.im
        } else {
            false
        }

    /**
     * Преобразование в строку
     */
    override fun toString(): String = "${re}+${im}i"

    /**
     * Хэш-код
     */
    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }
}
