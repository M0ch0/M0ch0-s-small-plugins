package io.github.mocho.bedvote.localization

enum class LocalePath(val path: String) {
    VotePassedMessage("broadcast.votePassedMessage"),
    VoteRejectMessage("broadcast.voteRejectedMessage"),
    VoteUndecidedMessage("broadcast.voteUndecidedMessage"),
    VoteBelowStipulationMessage("broadcast.voteBelowStipulationMessage"),
    VoteBossBarTitle("broadcast.voteBossBarTitle"),
    VoteToAgreeMessage("notice.voteToAgreeMessage"),
    VoteToDeclineMessage("notice.voteToDeclineMessage"),
    VoteAlreadyDoneMessage("notice.voteAlreadyDoneMessage"),
    VoteIsNotHeldMessage("notice.voteIsNotHeldMessage"),
    VoteSuggestsMessage("notice.voteSuggestsMessage"),
    VoteAgreeButtonText("notice.voteAgreeButtonText"),
    VoteDeclineButtonText("notice.voteDeclineButtonText")
}