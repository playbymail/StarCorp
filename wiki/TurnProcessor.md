#summary Server Turn processor
#labels Phase-Design

= Introduction =

The TurnProcessor reads turn files from the turns directory (fetched using the TurnFetcher) and processes them individually.  It will authenticate the player based on the supplied credentials in the turn and registers new players if the email address provided is not already on the system.

Once done processing the turn it emails the turn report to the player.