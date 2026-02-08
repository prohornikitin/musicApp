package afc.musicapp.domain.logic.pure.logger

enum class LogLevel(val priority: Int) {
    DEBUG(0),
    INFO(2),
    WARNING(4),
    ERROR(6),
}