@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import java.lang.IllegalArgumentException

/**
 * Класс "расписание поездов".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса хранит расписание поездов для определённой станции отправления.
 * Для каждого поезда хранится конечная станция и список промежуточных.
 * Поддерживаемые методы:
 * добавить новый поезд, удалить поезд,
 * добавить / удалить промежуточную станцию существующему поезду,
 * поиск поездов по времени.
 *
 * В конструктор передаётся название станции отправления для данного расписания.
 */
class TrainTimeTable(private val baseStationName: String) {
    private val trains = mutableMapOf<String, MutableList<Stop>>()

    /**
     * Добавить новый поезд.
     *
     * Если поезд с таким именем уже есть, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @param depart время отправления с baseStationName
     * @param destination конечная станция
     * @return true, если поезд успешно добавлен, false, если такой поезд уже есть
     */
    fun addTrain(train: String, depart: Time, destination: Stop): Boolean {
        if (train in trains) return false
        trains[train] = mutableListOf(Stop(baseStationName, depart), destination)
        return true
    }

    /**
     * Удалить существующий поезд.
     *
     * Если поезда с таким именем нет, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @return true, если поезд успешно удалён, false, если такой поезд не существует
     */
    fun removeTrain(train: String): Boolean {
        if (train !in trains) return false
        trains.remove(train)
        return true
    }

    /**
     * Добавить/изменить начальную, промежуточную или конечную остановку поезду.
     *
     * Если у поезда ещё нет остановки с названием stop, добавить её и вернуть true.
     * Если stop.name совпадает с baseStationName, изменить время отправления с этой станции и вернуть false.
     * Если stop совпадает с destination данного поезда, изменить время прибытия на неё и вернуть false.
     * Если stop совпадает с одной из промежуточных остановок, изменить время прибытия на неё и вернуть false.
     *
     * Функция должна сохранять инвариант: время прибытия на любую из промежуточных станций
     * должно находиться в интервале между временем отправления с baseStation и временем прибытия в destination,
     * иначе следует бросить исключение IllegalArgumentException.
     * Также, время прибытия на любую из промежуточных станций не должно совпадать с временем прибытия на другую
     * станцию или с временем отправления с baseStation, иначе бросить то же исключение.
     *
     * @param train название поезда
     * @param stop начальная, промежуточная или конечная станция
     * @return true, если поезду была добавлена новая остановка, false, если было изменено время остановки на старой
     */
    private fun checkStopTime(train: String, stop: Stop, index: Int? = null): Unit {
        val stops = trains[train] ?: throw IllegalArgumentException()
        if (stop.time < stops[0].time && stop.name !== this.baseStationName) throw IllegalArgumentException()
        if (stop.time > stops.last().time && stop.name !== stops.last().name) throw IllegalArgumentException()
        if (stop.name == this.baseStationName && stops[1].time < stop.time) throw IllegalArgumentException()
        if (index !== null) {
            if (index > 0) {
                if (stop.time < stops[index - 1].time) throw IllegalArgumentException()
            }
            if (index < stops.size - 1) {
                if (stop.time > stops[index + 1].time) throw IllegalArgumentException()
            }
        }
        val conflictStops = stops.filter { (it.time == stop.time && it.name !== stop.name) }
        if (conflictStops.isNotEmpty()) throw IllegalArgumentException()
    }

    fun addStop(train: String, stop: Stop): Boolean {
        val stops = trains[train] ?: throw IllegalArgumentException()
        val exactStop = stops.find { it.name == stop.name }
        if (exactStop !== null) {
            val index = stops.indexOf(exactStop)
            checkStopTime(train, stop, index)
            stops[index] = stop
            return false
        } else {
            checkStopTime(train, stop)
            stops.add(stop)
            stops.sortBy { it.time.getTimeInMinutes() }
        }
        return true
    }

    /**
     * Удалить одну из промежуточных остановок.
     *
     * Если stopName совпадает с именем одной из промежуточных остановок, удалить её и вернуть true.
     * Если у поезда нет такой остановки, или stopName совпадает с начальной или конечной остановкой, вернуть false.
     *
     * @param train название поезда
     * @param stopName название промежуточной остановки
     * @return true, если удаление успешно
     */
    fun removeStop(train: String, stopName: String): Boolean {
        val stops = trains[train] ?: throw IllegalArgumentException()
        val stop = stops.find { it.name == stopName } ?: return false
        val index = stops.indexOf(stop)
        if (index == 0 || index == stops.size - 1) return false
        stops.removeAt(index)
        return true
    }

    /**
     * Вернуть список всех поездов, упорядоченный по времени отправления с baseStationName
     */
    fun trains(): List<Train> {
        val result = mutableListOf<Train>()
        for ((name, stops) in trains) {
            result.add(Train(name, stops))
        }
        return result.sortedBy { train -> train.stops[0].time.getTimeInMinutes() }
    }

    /**
     * Вернуть список всех поездов, отправляющихся не ранее currentTime
     * и имеющих остановку (начальную, промежуточную или конечную) на станции destinationName.
     * Список должен быть упорядочен по времени прибытия на станцию destinationName
     */
    fun trains(currentTime: Time, destinationName: String): List<Train> {
        val correctTrains =
            trains.filter { (_, stops) ->
                (stops.find { it.name == destinationName } !== null && currentTime < stops[0].time)
            }
        val result = mutableListOf<Train>()

        for ((name, stops) in correctTrains) {
            result.add(Train(name, stops))
        }
        return result.sortedBy { it.stops.find { (name) -> name === destinationName }?.time?.getTimeInMinutes() }
    }

//    fun getBaseStationDepartTime(train: String): Time {
//        val baseStationStop = this.trains[train]?.get(0)
//        if (baseStationStop != null) {
//            return baseStationStop.time
//        }
//        return Time(0, 0)
//    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrainTimeTable

        if (baseStationName != other.baseStationName || trains != other.trains) return false

        return true
    }

    override fun hashCode(): Int {
        var result = baseStationName.hashCode()
        result = 31 * result + trains.hashCode()
        return result
    }

}

/**
 * Время (часы, минуты)
 */
data class Time(val hour: Int, val minute: Int) : Comparable<Time> {
    /**
     * Сравнение времён на больше/меньше (согласно контракту compareTo)
     */
    override fun compareTo(other: Time): Int {
        if (this.hour > other.hour) return 1
        if (this.hour < other.hour) return -1
        else {
            if (this.minute > other.minute) return 1
            if (this.minute < other.minute) return -1
        }
        return 0
    }

    fun getTimeInMinutes(): Int = this.hour * 60 + this.minute
}

/**
 * Остановка (название, время прибытия)
 */
data class Stop(val name: String, val time: Time)

/**
 * Поезд (имя, список остановок, упорядоченный по времени).
 * Первой идёт начальная остановка, последней конечная.
 */
data class Train(val name: String, val stops: List<Stop>) {
    constructor(name: String, vararg stops: Stop) : this(name, stops.asList())
}