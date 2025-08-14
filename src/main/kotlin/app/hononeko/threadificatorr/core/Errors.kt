package app.hononeko.app.hononeko.threadificatorr.core


sealed class ParserError {}

sealed class StorageError {
    data object SaveError : StorageError()
    data object ReadError : StorageError()
}

sealed class NotifierError {}