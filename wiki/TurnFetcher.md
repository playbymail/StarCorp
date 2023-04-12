#summary Server Turn fetcher
#labels Phase-Design

= Introduction =

The TurnFetcher retrieves emails from an email account and stores valid turns into the turns folder to be processed later by the TurnsProcessor.  Any unrecognised emails are forwarded to the bugs email address.
 