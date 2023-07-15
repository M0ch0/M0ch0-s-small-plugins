package io.github.mocho.autoshutdown.utils


enum class LocalePath(val path: String){
    PreShutdownNoticeMessage("broadcast.preShutdownNoticeMessage"),
    InShutdownCountdownMessage("broadcast.inShutdownCountdownMessage"),
    InShutdownProgressMessage("broadcast.inShutdownProgressMessage"),
    WhenTPSRecoveredToThreshold("broadcast.whenTPSRecoveredToThreshold"),
    PluginLoadMessage("system.pluginLoadMessage"),
    RecoveryRecoverStarted("system.recoveryRecoverStarted"),
    RecoveryRecoverRecovered("system.recoveryRecoverRecovered")
}